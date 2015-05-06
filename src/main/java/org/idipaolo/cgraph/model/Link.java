package org.idipaolo.cgraph.model;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequenceFactory;

import java.util.ArrayList;

/**
 * Created by Igor on 17/04/2015.
 */
public class Link extends LineString
{

    private Area area;
    private ArrayList<Node> nodes;
    private int addedNodes = 0;

    public Link(Node n1,Node n2, GeometryFactory factory) {
        super(null, factory);

        Coordinate[] coordinates = new Coordinate[2];
        coordinates[0] = n1.getPosition().getCoordinate();
        coordinates[1] = n2.getPosition().getCoordinate();

        CoordinateSequence coordinateSequence = CoordinateArraySequenceFactory.instance().create(coordinates);

        nodes = new ArrayList<Node>(2);

    }

    public void addNode(Node n)
    {
        this.nodes.add(n);
    }

    public Node getNode(int i)
    {
        return this.nodes.get(i);
    }


}
