package org.idipaolo.cgraph;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import org.apache.commons.collections15.MultiMap;
import org.apache.commons.collections15.multimap.MultiHashMap;
import org.idipaolo.cgraph.model.Link;
import org.idipaolo.cgraph.model.Node;

import java.util.Collection;
import java.util.List;

/**
 * Created by Igor on 14/05/2015.
 */
public class GraphGenerator {

    public Graph<Node,Link> generate(List<Link> links, List<Node> nodes)
    {

        //Add all nodes
        Graph<Node,Link> graph = new DirectedSparseGraph<Node, Link>();

        for(Node n: nodes)
        {
            graph.addVertex(n);
        }

        for(Link l: links)
        {
            graph.addEdge(l,l.getNode(0),l.getNode(1));
        }


        //Add edges


        return graph;
    }


}
