package org.idipaolo.cgraph;

import com.vividsolutions.jts.geom.GeometryFactory;
import org.idipaolo.cgraph.model.Area;
import org.idipaolo.cgraph.model.Obstacle;

/**
 * Created by Igor on 05/05/2015.
 */
public class LinkAdder {

    private Area area;
    private Area newArea;
    private GeometryFactory geometryFactory;

    public LinkAdder(Area area,GeometryFactory geometryFactory)
    {
        this.area = area;
        this.geometryFactory = geometryFactory;
        this.newArea = new Area();


    }

    public void AddLinkAdder()
    {
        // Get all the senders
        int size = area.getLinks().size();

        Node[] senders;
        Node[] receivers;


        // Get all the receivers

        //For each sender check if the receiver can receive interference


        //Generate link if needed

        //Add link to the area

    }

    protected boolean checkObstacles(Link l1)
    {

    }

}
