package org.idipaolo.cgraph.rendering;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;
import javafx.scene.canvas.GraphicsContext;
import org.idipaolo.cgraph.Configuration;
import org.idipaolo.cgraph.model.Obstacle;

import java.awt.*;
import java.util.List;


/**
 * Created by Igor on 31/05/2015.
 */
public class ObstacleRenderer {

    private double areaSize;

    public ObstacleRenderer()
    {
        this.areaSize = Configuration.getInstance().getAreaSize();
    }

    public void drawObstacles(Layout layout, List<Obstacle> obstacles, Graphics2D graphics)
    {

        Double width = layout.getSize().getWidth();
        Double height = layout.getSize().getHeight();

        graphics.setColor(Color.BLACK);
        Stroke oldStroke = graphics.getStroke();

        graphics.setStroke(new BasicStroke(3));

        for(Obstacle o: obstacles)
        {


            int x1 =(int)((o.getStartPoint().getX()/areaSize)*width);
            int y1 = (int)((o.getStartPoint().getY()/areaSize)*height);
            int x2 = (int)((o.getEndPoint().getX()/areaSize)*width);
            int y2 = (int)((o.getEndPoint().getY()/areaSize)*height);
            graphics.drawLine(x1,y1,x2,y2);
        }

        graphics.setStroke(oldStroke);
    }

}
