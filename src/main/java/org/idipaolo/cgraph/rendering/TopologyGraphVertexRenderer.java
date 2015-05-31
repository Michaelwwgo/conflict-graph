package org.idipaolo.cgraph.rendering;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;
import org.idipaolo.cgraph.Configuration;
import org.idipaolo.cgraph.model.Link;
import org.idipaolo.cgraph.model.Node;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * Created by Igor on 30/05/2015.
 */
public class TopologyGraphVertexRenderer implements Renderer.Vertex<Node, Link> {

    private Integer vertexSize;
    private Integer beamwidthLineSize;
    private Color receiverColor;
    private Color transmitterColor;
    private Double areaSize;
    private Double beamwidth;

    public TopologyGraphVertexRenderer()
    {
        vertexSize = 10;
        beamwidthLineSize = 20;
        receiverColor = new Color(255,0,0);
        transmitterColor = new Color(0,0,255);
        areaSize = Configuration.getInstance().getAreaSize();
        beamwidth = Configuration.getInstance().getBeamwidth();
    }

    @Override
    public void paintVertex(RenderContext<Node, Link> renderContext, Layout<Node, Link> layout, Node node) {
        GraphicsDecorator graphicsContext = renderContext.getGraphicsContext();

        Double width = layout.getSize().getWidth();
        Double height = layout.getSize().getHeight();

        Point2D center = layout.transform(node);

        center.setLocation((node.getPosition().getX()/areaSize)*width,
                (node.getPosition().getY()/areaSize)*height);

        Shape shape = null;
        Double orientation1 = node.getOrientation() - beamwidth/2;
        Double orientation2 = node.getOrientation() + beamwidth/2;

        if(node.isReceiver()) {
            shape = new Ellipse2D.Float((int)center.getX()-5, (int)center.getY()-5, 10, 10);
            graphicsContext.setPaint(receiverColor);

            graphicsContext.drawLine((int)center.getX(),(int)center.getY(),
                    (int)center.getX()+(int)(Math.cos(orientation1)*beamwidthLineSize),(int)center.getY()+(int)(Math.sin(orientation1)*beamwidthLineSize));
            graphicsContext.drawLine((int)center.getX(),(int)center.getY(),
                    (int)center.getX()+(int)(Math.cos(orientation2)*beamwidthLineSize),(int)center.getY()+(int)(Math.sin(orientation2)*beamwidthLineSize));

        } else {
            shape = new Ellipse2D.Float((int) center.getX()-5, (int) center.getY()-5, 10, 10);
            graphicsContext.setPaint(transmitterColor);
            graphicsContext.drawLine((int) center.getX(), (int) center.getY(),
                    (int) center.getX() + (int) (Math.cos(orientation1) * beamwidthLineSize), (int) center.getY() + (int) (Math.sin(orientation1) * beamwidthLineSize));
            graphicsContext.drawLine((int)center.getX(),(int)center.getY(),
                    (int)center.getX()+(int)(Math.cos(orientation2)*beamwidthLineSize),(int)center.getY()+(int)(Math.sin(orientation2)*beamwidthLineSize));
        }

        graphicsContext.fill(shape);
        layout.setLocation(node,center);
        layout.lock(node,true);


    }



}
