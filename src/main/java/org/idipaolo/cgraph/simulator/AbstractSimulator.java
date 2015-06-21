package org.idipaolo.cgraph.simulator;

import edu.uci.ics.jung.graph.Graph;
import org.idipaolo.cgraph.graphs.ConflictGraph;
import org.idipaolo.cgraph.graphs.TopologyGraph;
import org.idipaolo.cgraph.model.Link;
import org.idipaolo.cgraph.model.Node;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Igor on 13/06/2015.
 */
abstract public class AbstractSimulator implements Simulator
{
    private HashMap<Link,Boolean>[] slots;
    private int slotsNumber;

    protected void intializeSlots(TopologyGraph topologyGraph,int slotsNumber)
    {
        Collection<Node> nodes = topologyGraph.getVertices();
        //slots = new HashMap<Link,Boolean>[10];

    }


}
