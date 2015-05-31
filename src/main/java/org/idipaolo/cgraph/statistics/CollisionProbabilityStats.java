package org.idipaolo.cgraph.statistics;

import edu.uci.ics.jung.graph.Graph;
import org.idipaolo.cgraph.Configuration;
import org.idipaolo.cgraph.model.Link;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Igor on 31/05/2015.
 */
public class CollisionProbabilityStats {

    public CollisionProbabilityStats()
    {

    }

    public Double calculate(Graph<Link,Link> conflictGraph, List<Link> allLinks)
    {
        Double numLinks = allLinks.size()*1.0;

        //Count non blocked links
        Collection<Link> links = conflictGraph.getVertices();
        Iterator<Link> it = links.iterator();

        Double workingLinks = 0.0;

        while(it.hasNext())
        {
            Link l = it.next();
            if(!l.isBlocked())
            {
                workingLinks++;
            }

        }


        return workingLinks/numLinks;
    }

}
