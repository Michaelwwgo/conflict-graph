package org.idipaolo.cgraph.channel;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import org.idipaolo.cgraph.graphs.TopologyGraph;
import org.idipaolo.cgraph.model.Area;
import org.idipaolo.cgraph.model.Link;
import org.idipaolo.cgraph.model.Node;
import org.idipaolo.cgraph.model.Obstacle;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;

/**
 * Created by Igor on 24/06/2015.
 */
public class InterferenceCalculator {

    private Area area;
    private List<Node> txNodes;
    private double txPower = 2.5e-03;
    private GeometryFactory geometryFactory;
    private Antenna antenna;
    private double m_referenceDistance = 1.0;
    private double m_referenceLoss = 46.67777;

    private double m_obs_exponent = 4.5;
    private double m_no_obs_exponent = 2.5;

    private double low_threshold = -110.0;

    public InterferenceCalculator(Area area,GeometryFactory geometryFactory)
    {
        this.area = area;
        this.geometryFactory = geometryFactory;
        this.antenna = new Antenna();
    }

    public double getReceiversInterference()
    {
        List<Node> nodes = new ArrayList<Node>();

        for(Link l: area.getLinks())
        {
            nodes.add(l.getNode(1));
        }

        return getTotAvgInterference(nodes);
    }

    public double getTotAvgInterference(List<Node> nodes)
    {
        List<Double> avgInterferences = new ArrayList<Double>();

        for(Node n: nodes)
        {
            avgInterferences.add(getAvgInterference(n));
        }

        double sum = 0;
        int calculated = 0;

        for(Double d: avgInterferences)
        {
                sum += dBmToWatt(d);
                calculated++;
        }

        System.out.println(wattTodBm(sum/calculated));

        return wattTodBm(sum/calculated);
    }

    public double getAvgInterference(Node node)
    {
        // prendo tutti i trasmettitori
        // e genero link verso il ricevitore

        List<Link> links = area.getLinks();

        txNodes = new ArrayList<Node>();

        List<Double> rxPowers = new ArrayList<Double>();

        for(Link l: links)
        {
            // Non deve essere lo stesso trasmettitore
            //Link l = new Link()

            if(l.getNode(1) != node)
            {
                double rxPower = 0;

                //Aggiungo la potenza trasmessa
                rxPower += wattTodBm(this.txPower);

                Link link = new Link(l.getNode(0),node,this.geometryFactory);


                double x1 = l.getNode(0).getPosition().getCoordinate().getOrdinate(0);
                double y1 = l.getNode(0).getPosition().getCoordinate().getOrdinate(1);

                double x2 = node.getPosition().getCoordinate().getOrdinate(0);
                double y2 = node.getPosition().getCoordinate().getOrdinate(1);

                double orientation = Math.atan2(y2-y1,x2-x1);
                //Guadagno antenna del trasmettitore

                double txGain = antenna.getGainDb(orientation,l.getNode(0).getOrientation());
                double rxGain = antenna.getGainDb(orientation,node.getOrientation());


                //Guadagno antenna del ricevente

                //Pathloss -> un bordello
                double pathLoss = calculatePathLoss(link);
                rxPower += rxGain;
                rxPower += txGain;
                rxPower += pathLoss;

                rxPowers.add(rxPower);
            }



        }

        double sum = 0;

        int calculated = 0;


        for(Double v : rxPowers)
        {
            calculated += 1;
            sum += dBmToWatt(v);
        }

        dBmToWatt(1);

        return wattTodBm(sum/calculated);
    }

    protected double calculatePathLoss(Link link)
    {

        //Controllo se ci sono ostacoli e quanti ostacoli ci sono
        int obsNumber = 0;

        for(Obstacle obs: area.getObstacles())
        {
            if(intersect(obs,link.getLineString()))
            {
                obsNumber++;
            }
        }

        double pathLossGain = 0;

        pathLossGain -= obsNumber*(30);


        //Calcolo con la prima interesezione con l'ostacolo

        double distance = link.getLineString().getLength();

        if(obsNumber > 0)
        {
            pathLossGain -= 10 * m_obs_exponent * Math.log10 (distance / m_referenceDistance);
        }
        else
        {
            pathLossGain -= 10*m_no_obs_exponent * Math.log10(distance/ m_referenceDistance);
        }

        pathLossGain -= m_referenceLoss;

        return pathLossGain;
    }

    protected double wattTodBm(double watt)
    {
        return 10.0*Math.log10(watt);
    }

    protected double dBmToWatt(double db)
    {
        return Math.pow(10,db/10);
    }


    public static boolean intersect(LineString l1,LineString l2)
    {

        Coordinate coo1 = l1.getCoordinateN(0);
        Coordinate coo2 = l1.getCoordinateN(1);

        Coordinate coo3 = l2.getCoordinateN(0);
        Coordinate coo4 = l2.getCoordinateN(1);

        double x1 = coo1.x;
        double y1 = coo1.y;

        double x2 = coo2.x;
        double y2 = coo2.y;

        double x3 = coo3.x;
        double y3 = coo3.y;

        double x4 = coo4.x;
        double y4 = coo4.y;

        double a1, a2, b1, b2, c1, c2;
        double r1, r2 , r3, r4;
        double denom;

        // Compute a1, b1, c1, where line joining points 1 and 2
        // is “a1 x + b1 y + c1 = 0″.
        a1=y2-y1;
        b1=x1-x2;
        c1 = (x2*y1)-(x1*y2);

        // Compute r3 and r4.
        r3 = ((a1 * x3) + (b1 * y3) + c1);
        r4 = ((a1 * x4) + (b1 * y4) + c1);

        // Check signs of r3 and r4. If both point 3 and point 4 lie on
        // same side of line 1, the line segments do not intersect.
        if ((r3 != 0) && (r4 != 0) && same_sign(r3,r4))
        {
            return false;
        }

        // Compute a2, b2, c2
        a2 = y4-y3;
        b2 = x3-x4;
        c2 = (x4*y3)-(x3*y4);

        // Compute r1 and r2
        r1 = (a2 * x1) + (b2 * y1) + c2;
        r2 = (a2 * x2) + (b2 * y2) + c2;

        // Check signs of r1 and r2. If both point 1 and point 2 lie
        // on same side of second line segment, the line segments do
        // not intersect.
        if ((r1 != 0) && (r2 != 0) && (same_sign(r1, r2))){
            return false;
        }

        //Line segments intersect: compute intersection point.
        denom = (a1*b2)-(a2*b1);

        //Overlap
        if (denom == 0) {
            return true;
        }

        // lines_intersect
        return true;
    }

    public static boolean same_sign(double a, double b){

        return (( a * b) >= 0);
    }

}
