package org.idipaolo.cgraph.model;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

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

        n1.getPosition();
        n2.getPosition();

        super(points, factory);
        nodes = new ArrayList<Node>(2);

    }

    public addNode(Node n)
    {

    }

    public Node getNode(int i)
    {
        return this.nodes.get(i);
    }


}
