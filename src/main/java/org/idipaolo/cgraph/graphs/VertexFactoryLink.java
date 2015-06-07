package org.idipaolo.cgraph.graphs;

import org.apache.commons.collections15.Factory;
import org.idipaolo.cgraph.model.Link;

/**
 * Created by Igor on 07/06/2015.
 */
public class VertexFactoryLink implements Factory<Link> {
    @Override
    public Link create() {
        return new Link(null,null,null);
    }
}
