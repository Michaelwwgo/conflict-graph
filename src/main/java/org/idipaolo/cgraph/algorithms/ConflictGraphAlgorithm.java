package org.idipaolo.cgraph.algorithms;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import org.idipaolo.cgraph.model.Link;
import org.idipaolo.cgraph.model.Node;
import sun.security.provider.certpath.Vertex;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Igor on 27/05/2015.
 */
public class ConflictGraphAlgorithm {

    public Graph<Link,Link> getGraph(Graph<Node,Link> graph)
    {

        Graph<Link,Link> resultGraph = new DirectedSparseGraph<Link, Link>();
        Integer edgeCounter = 0;

        HashMap<Node,Link> senderLinks = new HashMap<Node,Link>();
        HashMap<Node,Link> receiverLinks = new HashMap<Node, Link>();

        //Get the links surviving
        Collection<Link> edges = graph.getEdges();
        Iterator<Link> it = edges.iterator();

        while(it.hasNext())
        {
            Link l = it.next();

            if(l.isProper())
            {
                resultGraph.addVertex(l);
                senderLinks.put(l.getNode(0),l);
                receiverLinks.put(l.getNode(1),l);
            }
        }

        it = edges.iterator();

        while(it.hasNext())
        {
            Link l = it.next();

            if(!l.isProper())
            {
                Link txLink = senderLinks.get(l.getNode(0));
                Link rxLink = receiverLinks.get(l.getNode(1));

                if(txLink != null &&  rxLink != null)
                {
                    resultGraph.addEdge(l,txLink,rxLink);
                    ++edgeCounter;
                }
            }

        }


        System.out.println(edgeCounter);

        return resultGraph;

    }

}
