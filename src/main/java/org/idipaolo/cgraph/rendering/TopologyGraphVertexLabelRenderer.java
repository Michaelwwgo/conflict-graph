package org.idipaolo.cgraph.rendering;


import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import org.idipaolo.cgraph.model.Link;
import org.idipaolo.cgraph.model.Node;

/**
 * Created by Igor on 31/05/2015.
 */
public class TopologyGraphVertexLabelRenderer implements Renderer.VertexLabel<Node,Link> {

    @Override
    public void labelVertex(RenderContext<Node, Link> renderContext, Layout<Node, Link> layout, Node node, String s) {

    }

    @Override
    public Position getPosition() {
        return null;
    }

    @Override
    public void setPosition(Position position) {

    }

    @Override
    public void setPositioner(Positioner positioner) {

    }

    @Override
    public Positioner getPositioner() {
        return null;
    }
}
