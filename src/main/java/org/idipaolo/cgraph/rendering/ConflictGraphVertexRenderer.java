package org.idipaolo.cgraph.rendering;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.BasicRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;
import org.idipaolo.cgraph.model.Link;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * Created by Igor on 30/05/2015.
 */
public class ConflictGraphVertexRenderer implements Renderer.Vertex<Link, Link> {
    @Override public void paintVertex(RenderContext<Link, Link> rc,
                                      Layout<Link, Link> layout, Link vertex) {
        GraphicsDecorator graphicsContext = rc.getGraphicsContext();
        Point2D center = layout.transform(vertex);
        Shape shape = null;
        Color color = null;
        if(vertex.isBlocked()) {
            shape = new Ellipse2D.Float((int)center.getX()-5, (int)center.getY()-5, 10, 10);
            color = new Color(255, 0, 0);
        } else {
            shape = new Ellipse2D.Float((int) center.getX() - 5, (int) center.getY() - 5, 10, 10);

            color = new Color(0, 0, 255);
        }
        graphicsContext.setPaint(color);
        graphicsContext.fill(shape);
    }
}
