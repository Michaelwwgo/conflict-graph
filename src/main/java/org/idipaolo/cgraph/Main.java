package org.idipaolo.cgraph;


import com.csvreader.CsvWriter;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.cli.*;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.idipaolo.cgraph.model.Area;
import org.idipaolo.cgraph.model.Link;
import org.idipaolo.cgraph.model.Node;
import org.idipaolo.cgraph.model.Obstacle;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.util.*;
import java.util.List;

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

        String outputFile = "stats.csv";
        CsvWriter csvOutput;

        try {
            // use FileWriter constructor that specifies open for appending
             csvOutput = new CsvWriter(new FileWriter(outputFile, true), ',');
        }
        catch(Exception e)
        {
            System.out.println("Hello my dear, something wrong happened! Do you have write " +
                    "permission? The CsvWriter is asking for it!");
        }

            // if the file didn't already exist then we need to write out the header line
            if (!alreadyExists)
            {
                csvOutput.write("id");
                csvOutput.write("name");
                csvOutput.endRecord();
            }
            // else assume that the file already has the correct header line

            // write out a few records
            csvOutput.write("1");
            csvOutput.write("Bruce");
            csvOutput.endRecord();

            csvOutput.write("2");
            csvOutput.write("John");
            csvOutput.endRecord();

        for(int j = 0; j < rounds; j++)
        {
            int links = linksDistribution.sample() +1;
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

            System.out.print("Link nell'area: ");
            System.out.println(area.getLinks().size());
            System.out.print("Ostacoli nell'area: ");
            System.out.println(area.getObstacles().size());

            LinkAdder linkAdder = new LinkAdder(area,geometryFactory);
            List<Link> remainingLinks = linkAdder.AddLinkAdder();

            java.util.List<Link> linksList = area.getLinks();
            ArrayList<Node> nodesList = new ArrayList<Node>();
            for(Link l: linksList)
            {
                nodesList.add(l.getNode(0));
                nodesList.add(l.getNode(1));
            }

            GraphGenerator graphGenerator = new GraphGenerator();
            Graph<Node,Link> graph = graphGenerator.generaGrafo(remainingLinks, nodesList);

            double inDegreeSum = 0;
            for(Node n: nodesList)
            {
                inDegreeSum += graph.inDegree(n);
            }


            //With the remaining lines build the graph

            //Count all stats about this graph

        }


    }

}
