package org.idipaolo.cgraph;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import org.idipaolo.cgraph.model.Area;
import org.idipaolo.cgraph.model.Link;
import org.idipaolo.cgraph.model.Node;
import org.idipaolo.cgraph.model.Obstacle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Igor on 05/05/2015.
 */
public class LinkAdder {

    private Area area;
    private double beamwidth;
    private GeometryFactory geometryFactory;

    public LinkAdder(Area area,GeometryFactory geometryFactory)
    {
        this.area = area;
        this.geometryFactory = geometryFactory;
        this.beamwidth = Configuration.getInstance().getBeamwidth();

    }

    public List<Link> AddLinkAdder()
    {
        // Get all the senders
        int size = area.getLinks().size();

        Node[] senders = new Node[size];

        int i = 0;

        for(Link l: area.getLinks())
        {
            senders[i] = l.getNode(0);
            i++;
        }


        // Get all the receivers

        Node[] receivers = new Node[size];

        i = 0;

        for(Link l: area.getLinks())
        {
            receivers[i] = l.getNode(1);
            i++;
        }

        ArrayList<Link> links = new ArrayList<Link>();

        //Build a set of links between senders and receivers

        for(i = 0; i < size;i++)
        {
            for(int j = 0; j < size;j++)
            {
                double x1 = senders[i].getPosition().getCoordinate().getOrdinate(0);
                double y1 = senders[i].getPosition().getCoordinate().getOrdinate(1);

                double x2 = receivers[j].getPosition().getCoordinate().getOrdinate(0);
                double y2 = receivers[j].getPosition().getCoordinate().getOrdinate(1);

                double orientation = Math.atan2(y2-y1,x2-x1);

                double sender_orientation = senders[i].getOrientation();
                double receiver_orientation = receivers[j].getOrientation();

                boolean sender_or_ok = sender_orientation < orientation + this.beamwidth/2 && sender_orientation > orientation - this.beamwidth/2;
                boolean receiver_or_ok = receiver_orientation < orientation +Math.PI+ this.beamwidth/2 && receiver_orientation > orientation + Math.PI -this.beamwidth/2;

                if(sender_or_ok && receiver_or_ok)
                {
                    Link l = new Link(senders[i],receivers[j],geometryFactory);
                    links.add(l);
                }



            }
        }

        System.out.print("Link in piu': ");
        System.out.println(links.size());

        //Remove all links that intersect obstacles

        for(Iterator<Link> iterator = links.iterator(); iterator.hasNext();)
        {
            Link l = iterator.next();

            if(intersectObstacles(l))
            {
                iterator.remove();
            }
        }

        System.out.print("Link rimanenti: ");
        System.out.println(links.size());

        //Add link to the area

        // Genera il grafo dai link

        return links;
    }

    protected boolean intersectObstacles(Link l1)
    {
        LineString lineString = l1.getLineString();

        List<Obstacle> obstacles = this.area.getObstacles();

        //int i = 1;
        //System.out.println(obstacles.size());
        for(Obstacle obs: obstacles)
        {
            //System.out.println(lineString.getLength());

            if(intersect(obs,lineString))
            {
                return true;
            }

        }

        return false;
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
