package org.idipaolo.cgraph;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateArrays;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequenceFactory;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.idipaolo.cgraph.model.Area;
import org.idipaolo.cgraph.model.Link;
import org.idipaolo.cgraph.model.Node;

/**
 * Created by Igor on 17/04/2015.
 */
public class LinkPlacer {

    private GeometryFactory geometryFactory;
    private Area area;

    private static double minDistance = 1;
    private static double maxDistance = 10;

    private UniformRealDistribution xDistribution;
    private UniformRealDistribution yDistribution;

    private NodeFactory nodeFactory;

    public LinkPlacer(GeometryFactory geometryFactory,Area area)
    {
        this.geometryFactory = geometryFactory;
        this.area = area;
        this.nodeFactory = new NodeFactory();

        double areaSize = Configuration.getInstance().getAreaSize();

        this.xDistribution = new UniformRealDistribution(0,areaSize);
        this.yDistribution = new UniformRealDistribution(0,areaSize);

    }

    public void place(int linkNumber)
    {
        for(int i = 0; i < linkNumber; i++)
        {


            double distance = 0;
            Coordinate[] coordinates = new Coordinate[2];
            LineString lineString;

            do
            {
                double x1 = xDistribution.sample();
                double y1 = yDistribution.sample();

                Coordinate c1 = new Coordinate(x1,y1);

                double x2 = xDistribution.sample();
                double y2 = yDistribution.sample();

                Coordinate c2 = new Coordinate(x2,y2);

                coordinates[0] = c1;
                coordinates[1] = c2;

                lineString = geometryFactory.createLineString(coordinates);
                distance = lineString.getLength();


                System.out.println(distance);


            }while(distance < minDistance || distance > maxDistance);

            Link link = new Link(this.area);
            double orientation = getOrientation(coordinates);

            Node sender = nodeFactory.createNode();
            sender.setPosition(geometryFactory.createPoint(coordinates[0]));
            sender.setOrientation(orientation);

            Node receiver = nodeFactory.createNode();
            receiver.setPosition(geometryFactory.createPoint(coordinates[1]));
            receiver.setOrientation(orientation+Math.PI);

            //link.setSender(sender);
            //link.setReceiver(receiver);

            //area.addLink(link);

        }
    }

    protected double getOrientation(Coordinate[] coordinates)
    {
        double x = coordinates[1].x - coordinates[0].x;
        double y = coordinates[1].y - coordinates[0].y;

        double rad = Math.atan2(y,x);
        return rad;
    }

}
