package org.idipaolo.cgraph.viewer;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import org.idipaolo.cgraph.model.Obstacle;
import org.idipaolo.cgraph.rendering.ObstacleRenderer;

import java.awt.*;
import java.util.List;


/**
 * Created by Igor on 31/05/2015.
 */
public class TopologyVisualizationViewer extends VisualizationViewer {

    public List<Obstacle> getObstacleList() {
        return obstacleList;
    }

    public void setObstacleList(List<Obstacle> obstacleList) {
        this.obstacleList = obstacleList;
    }

    private List<Obstacle> obstacleList;


    @Override
    protected void renderGraph(Graphics2D g2d) {
        super.renderGraph(g2d);

        ObstacleRenderer obstacleRenderer = new ObstacleRenderer();
        obstacleRenderer.drawObstacles(this.getGraphLayout(),obstacleList,g2d);

    }

    public TopologyVisualizationViewer(Layout layout) {
        super(layout);
    }
}
