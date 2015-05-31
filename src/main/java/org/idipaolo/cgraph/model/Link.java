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
public class Link
{

    private Area area;
    private ArrayList<Node> nodes = new ArrayList<Node>(2);
    private int addedNodes = 0;

    private LineString lineString;
    private boolean proper;

    private boolean blocked;

    public boolean isProper() {
        return proper;
    }

    public void setProper(boolean proper) {
        this.proper = proper;
    }


    public Link(Node n1,Node n2, GeometryFactory factory) {

        nodes.add(n1);
        nodes.add(n2);

        addedNodes = 2;
        setBlocked(false);

        Coordinate[] coordinates = new Coordinate[2];
        coordinates[0] = n1.getPosition().getCoordinate();
        coordinates[1] = n2.getPosition().getCoordinate();

        CoordinateSequence coordinateSequence = CoordinateArraySequenceFactory.instance().create(coordinates);
        lineString = factory.createLineString(coordinateSequence);


    }

    public Node getNode(int i)
    {
        return this.nodes.get(i);
    }

    public LineString getLineString()
    {
        return lineString;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Link))
        {

            return false;
        }

        Link t = (Link) obj;

        if(t.getNode(0).equals(this.getNode(0)) && t.getNode(1).equals(this.getNode(1)))
        {
            return true;
        }

        return false;

    }
}
