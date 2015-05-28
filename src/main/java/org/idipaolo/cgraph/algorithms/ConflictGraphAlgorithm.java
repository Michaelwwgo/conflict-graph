package org.idipaolo.cgraph.algorithms;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import org.idipaolo.cgraph.model.Link;
import org.idipaolo.cgraph.model.Node;
import sun.security.provider.certpath.Vertex;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Igor on 27/05/2015.
 */
public class ConflictGraphAlgorithm {

    public Graph<Link,Node> getGraph(Graph<Node,Link> graph)
    {

        Graph<Link,Node> resultGraph = new UndirectedSparseGraph<Link, Node>();

        //Get the links surviving
        Collection<Link> edges = graph.getEdges();
        Iterator<Link> it = edges.iterator();

        while(it.hasNext())
        {
            Link l = it.next();
            resultGraph.addVertex(l);
        }

        Collection<Node> vertices = graph.getVertices();
        Iterator<Node> nodeIterator = vertices.iterator();

        while(nodeIterator.hasNext())
        {
            Node n = nodeIterator.next();

            if(n.isReceiver())
            {
                System.out.println(graph.getInEdges(n).size());
            }
        }


        //ogni link semplice è un nodo

        return resultGraph;

    }

}
