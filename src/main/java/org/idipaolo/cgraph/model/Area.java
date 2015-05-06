package org.idipaolo.cgraph.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor on 18/04/2015.
 */
public class Area {

    private List<Obstacle> obstacles;
    private List<Link> links;

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public List<Link> getLinks() {
        return links;
    }


    public Area()
    {
        obstacles = new ArrayList<Obstacle>();
        links = new ArrayList<Link>();
    }

    public void addObstacle(Obstacle obstacle)
    {
        obstacles.add(obstacle);
    }

    public void addLink(Link link)
    {
        links.add(link);
    }

}
