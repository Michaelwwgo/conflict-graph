package org.idipaolo.cgraph.model;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * Created by Igor on 17/04/2015.
 */
public class Obstacle extends LineString{

    public Obstacle(CoordinateSequence points, GeometryFactory factory) {
        super(points, factory);
    }
}
