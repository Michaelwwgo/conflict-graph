package org.idipaolo.cgraph.graphs;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.Factory;
import org.idipaolo.cgraph.model.Link;

/**
 * Created by Igor on 07/06/2015.
 */
public class DirectedGraphFactoryLinkLink implements Factory<Graph<Link,Link>> {

    @Override
    public Graph<Link, Link> create() {
        return new DirectedSparseGraph<Link, Link>();
    }
}
