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
        options.addOption("os","Obstacle Size",true,"Maximum size of the obstacle");
        options.addOption("as","Area Size",true,"The size of the Area in meters");
        options.addOption("rd","Rounds",true,"The number of the rounds of montecarlo simulations");
        options.addOption("bd","Beamwidth",true,"The beamwidth of each link");

        CommandLineParser parser = new BasicParser();
        CommandLine cmd = parser.parse( options, args);

        //Geometry

        GeometryFactory geometryFactory = new GeometryFactory();

        //Poisson test
        int areaSize = Integer.valueOf(cmd.getOptionValue("as", "10"));
        double beamWidth = Double.valueOf(cmd.getOptionValue("bd","10"));
        double linkDensity = Double.valueOf(cmd.getOptionValue("ld","0.1"));
        double obstacleDensity = Double.valueOf(cmd.getOptionValue("od","0.25"));
        int rounds = Integer.valueOf(cmd.getOptionValue("rd","50"));



        long averageNumLinks = Math.round(linkDensity*Math.pow(areaSize,2));
        long averageNumObstacles = Math.round(obstacleDensity*Math.pow(areaSize,2));

        Configuration.getInstance().setAreaSize(areaSize);
        Configuration.getInstance().setBeamwidth(Math.toRadians(beamWidth));

        PoissonDistribution linksDistribution = new PoissonDistribution(averageNumLinks);
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
            int links = linksDistribution.sample();
            int obstacles = obstaclesDistribution.sample();

            //Create Area
            Area area = new Area();

            //Place links and steer antennas
            LinkPlacer linkPlacer = new LinkPlacer(geometryFactory,area);
            linkPlacer.place(links);

            //Place obstacles
            ObstaclePlacer obstaclePlacer = new ObstaclePlacer(geometryFactory,area);
            obstaclePlacer.place(obstacles);

            //Build lines checking the receivers and transmitters that can see each other


            // Discard lines that intersect obstacles

            //With the remaining lines build the graph

            //Count all stats about this graph

        }


    }

}
