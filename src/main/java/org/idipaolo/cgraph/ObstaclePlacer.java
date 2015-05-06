package org.idipaolo.cgraph;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequenceFactory;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.idipaolo.cgraph.model.Area;
import org.idipaolo.cgraph.model.Obstacle;

/**
 * Created by Igor on 17/04/2015.
 */
public class ObstaclePlacer {

    private GeometryFactory geometryFactory;
    private Area area;

    private static double obstacleMaxSize = 1;


    private UniformRealDistribution obstacleSizeDistribution;
    private UniformRealDistribution obstacleOrientationDistribution;

    private UniformRealDistribution xDistribution;
    private UniformRealDistribution yDistribution;

    public ObstaclePlacer(GeometryFactory geometryFactory,Area area)
    {

        this.geometryFactory = geometryFactory;
        this.area = area;

        this.obstacleSizeDistribution = new UniformRealDistribution(0,obstacleMaxSize);
        this.obstacleOrientationDistribution = new UniformRealDistribution(0,2*Math.PI);

        double areaSize = Configuration.getInstance().getAreaSize();

        this.xDistribution = new UniformRealDistribution(0,areaSize);
        this.yDistribution = new UniformRealDistribution(0,areaSize);

    }

    public void place(int obstaclesNumber)
    {
        Coordinate[] coordinates = new Coordinate[2];
        LineString lineString;



        for(int i = 0; i < obstaclesNumber;i++)
        {

            double orientation = obstacleOrientationDistribution.sample();
            double size = obstacleSizeDistribution.sample();

            double xdiff = Math.cos(orientation)*(size/2.0);
            double ydiff = Math.sin(orientation)*(size/2.0);

            double xcenter = xDistribution.sample();
            double ycenter = yDistribution.sample();

            coordinates[0] = new Coordinate(xcenter+xdiff,ycenter+ydiff);
            coordinates[1] = new Coordinate(xcenter-xdiff,ycenter-ydiff);

            CoordinateSequence sequence= CoordinateArraySequenceFactory.instance().create(coordinates);
            Obstacle obstacle = new Obstacle(sequence,geometryFactory);

            //System.out.println(obstacle.getLength());

            this.area.addObstacle(obstacle);

        }
    }



}
