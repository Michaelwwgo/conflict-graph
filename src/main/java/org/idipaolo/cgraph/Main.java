package org.idipaolo.cgraph;


import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import org.apache.commons.cli.*;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.idipaolo.cgraph.model.Area;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Igor on 17/04/2015.
 */
public class Main {

    public static void main(String[] args) throws ParseException {

        //Command line

        Options options = new Options();
        options.addOption("ld","Link Density",true,"Indicates the link density");
        options.addOption("od","Obstacle Density",true,"Indicates the link density");
        options.addOption("as","Area Size",true,"The size of the Area in meters");
        options.addOption("rd","Rounds",true,"The number of the rounds of montecarlo simulations");

        CommandLineParser parser = new BasicParser();
        CommandLine cmd = parser.parse( options, args);

        //Geometry

        GeometryFactory geometryFactory = new GeometryFactory();

        //Poisson test
        int areaSize = 10;
        int beamwidth = 10;
        int averageNumNodes = 10;
        int averageNumObstacles = 10;
        int rounds = 50;

        Configuration.getInstance().setAreaSize(areaSize);
        Configuration.getInstance().setBeamwidth(Math.toRadians(beamwidth));

        PoissonDistribution nodesDistribution = new PoissonDistribution(averageNumNodes);
        PoissonDistribution obstaclesDistribution = new PoissonDistribution(averageNumObstacles);


        // The Layout<V, E> is parameterized by the vertex and edge types
//        Layout<Integer, String> layout = new CircleLayout(sgv.g);
//        layout.setSize(new Dimension(300,300)); // sets the initial size of the space
//        // The BasicVisualizationServer<V,E> is parameterized by the edge types
//        VisualizationServer<Integer,String> vv =
//                new BasicVisualizationServer<Integer,String>(layout);
//        vv.setPreferredSize(new Dimension(350,350)); //Sets the viewing area size

        //JFrame frame = new JFrame("Simple Graph View");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.getContentPane().add(vv);
        //frame.pack();
        //frame.setVisible(true);
        for(int j = 0; j < rounds; j++)
        {
            int nodes = nodesDistribution.sample();
            int obstacles = obstaclesDistribution.sample();

            //Create Area
            Area area = new Area();

            //Place links and steer antennas
            LinkPlacer linkPlacer = new LinkPlacer(geometryFactory,area);
            linkPlacer.place(20);

            //Place obstacles
            ObstaclePlacer obstaclePlacer = new ObstaclePlacer(geometryFactory,area);
            obstaclePlacer.place(20);

            //Build lines checking the receivers and transmitters that can see each other

            // Discard lines that intersect obstacles

            //With the remaining lines build the graph

            //Count all stats about this graph

        }


    }

}
