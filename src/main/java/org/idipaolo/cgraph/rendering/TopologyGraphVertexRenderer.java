package org.idipaolo.cgraph.rendering;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;
import org.idipaolo.cgraph.model.Link;
import org.idipaolo.cgraph.model.Node;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * Created by Igor on 30/05/2015.
 */
public class TopologyGraphVertexRenderer implements Renderer.Vertex<Node, Link> {
    @Override
    public void paintVertex(RenderContext<Node, Link> renderContext, Layout<Node, Link> layout, Node node) {
        GraphicsDecorator graphicsContext = renderContext.getGraphicsContext();

        Double height = layout.getSize().getHeight();
        Double width = layout.getSize().getWidth();

        Point2D center = layout.transform(node);
        center.setLocation(node.getPosition().getX(),node.getPosition().getY());
        Shape shape = null;
        Color color = null;
        if(node.isReceiver()) {
            shape = new Ellipse2D.Float((int)center.getX()-10, (int)center.getY()-10, 20, 20);
            color = new Color(255, 0, 0);
        } else {
            shape = new Ellipse2D.Float((int) center.getX() - 10, (int) center.getY() - 10, 20, 20);

            color = new Color(0, 0, 255);
        }
        graphicsContext.setPaint(color);
        graphicsContext.fill(shape);
    }



}
