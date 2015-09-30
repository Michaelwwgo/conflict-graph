package org.idipaolo.cgraph;



import agape.algos.MIS;
import agape.algos.Coloring;
import com.csvreader.CsvWriter;
import com.vividsolutions.jts.geom.GeometryFactory;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.*;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import org.apache.commons.cli.*;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.math3.distribution.PoissonDistribution;

import org.idipaolo.cgraph.algorithms.ConflictGraphAlgorithm;
import org.idipaolo.cgraph.channel.InterferenceCalculator;
import org.idipaolo.cgraph.csv.TopologyExporter;
import org.idipaolo.cgraph.graphs.DirectedGraphFactoryLinkLink;
import org.idipaolo.cgraph.graphs.EdgeFactoryLink;
import org.idipaolo.cgraph.graphs.TopologyGraph;
import org.idipaolo.cgraph.graphs.VertexFactoryLink;
import org.idipaolo.cgraph.model.Area;
import org.idipaolo.cgraph.model.Link;
import org.idipaolo.cgraph.model.Node;
import org.idipaolo.cgraph.model.Obstacle;
import org.idipaolo.cgraph.rendering.ConflictGraphVertexRenderer;
import org.idipaolo.cgraph.rendering.ObstacleRenderer;
import org.idipaolo.cgraph.rendering.TopologyGraphVertexRenderer;
import org.idipaolo.cgraph.statistics.CollisionProbabilityStats;
import org.idipaolo.cgraph.viewer.TopologyVisualizationViewer;

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
        options.addOption("fn","Filename",true,"Filename");
        options.addOption("sd","Seed",true,"Seed number");
        options.addOption("fl","Link file",true,"File to store link topology");
        options.addOption("fo","Obstacle file",true,"File to store obstacle topology");
        options.addOption("fi","Interference File", true,"File interference");
        options.addOption("fb","Blocked link File",true,"Blocked file");

        CommandLineParser parser = new BasicParser();
        CommandLine cmd = parser.parse( options, args);

        //Geometry

        GeometryFactory geometryFactory = new GeometryFactory();

        //Poisson test
        int areaSize = Integer.valueOf(cmd.getOptionValue("as", "10"));
        double beamWidth = Double.valueOf(cmd.getOptionValue("bd","10"));
        double linkDensity = Double.valueOf(cmd.getOptionValue("ld","0.1"));
        double obstacleDensity = Double.valueOf(cmd.getOptionValue("od","0.25"));
        double obstacleMaxSize = Double.valueOf(cmd.getOptionValue("os","1"));
        int rounds = Integer.valueOf(cmd.getOptionValue("rd","50"));
        String outputFile = cmd.getOptionValue("fn","stats.csv");
        String outputLinkFile = cmd.getOptionValue("fl",null);
        String outputObstacleFile = cmd.getOptionValue("fo",null);
        String outputInterferenceFile = cmd.getOptionValue("fi",null);
        String outputBlockedFile = cmd.getOptionValue("fb",null);
        long seed = Long.valueOf(cmd.getOptionValue("sd", "0"));

        long averageNumLinks = Math.round(linkDensity*Math.pow(areaSize,2));
        long averageNumObstacles = Math.round(obstacleDensity*Math.pow(areaSize,2));

        Configuration.getInstance().setAreaSize(areaSize);
        Configuration.getInstance().setBeamwidth(Math.toRadians(beamWidth));
        Configuration.getInstance().setObstacleMaxSize(obstacleMaxSize);
        Configuration.getInstance().setSeed(seed);

//        PoissonDistribution linksDistribution = new PoissonDistribution(averageNumLinks);
//        PoissonDistribution obstaclesDistribution = new PoissonDistribution(averageNumObstacles);
//
//        if(seed != 0)
//        {
//            //linksDistribution.reseedRandomGenerator(seed+0x1);
//            //obstaclesDistribution.reseedRandomGenerator(seed+0x2);
//        }

        CsvWriter csvOutput = null;
        CsvWriter csvBlocked = null;

        try {
            // use FileWriter constructor that specifies open for appending
             csvOutput = new CsvWriter(new FileWriter(outputFile, true), ',');
             if(outputBlockedFile != null)
             {
                 csvBlocked = new CsvWriter(new FileWriter(outputBlockedFile, true), ',');
             }

            // else assume that the file already has the correct header line
        }
        catch(Exception e)
        {
            System.out.println("Hello my dear, something wrong happened! Do you have write " +
                    "permission? The CsvWriter is asking for it!");
            System.exit(1);
        }


        for(int j = 0; j < rounds; j++)
        {
            //int links = linksDistribution.sample() +1;
            //int obstacles = obstaclesDistribution.sample();

            int links = (int)averageNumLinks;
            int obstacles = (int)averageNumObstacles;
            System.out.println(links);
            System.out.println(obstacles);

            Integer[] integersDistribution = new Integer[12];

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

            List<Link> linksList = area.getLinks();
            ArrayList<Node> nodesList = new ArrayList<Node>();

            for(int i = 0; i < 12; ++i)
            {
                integersDistribution[i] = 0;
            }

            for(Link l: linksList)
            {
                nodesList.add(l.getNode(0));
                nodesList.add(l.getNode(1));
            }

            GraphGenerator graphGenerator = new GraphGenerator();
            Graph<Node,Link> graph = graphGenerator.generate(remainingLinks, nodesList);

            //showTopologyGraph(graph,area.getObstacles(), "Topology graph");
            TopologyExporter topologyExporter = new TopologyExporter();

            if(outputLinkFile != null)
            {
                topologyExporter.writeLinks(area.getLinks(),outputLinkFile);
            }

            if(outputObstacleFile != null)
            {
                topologyExporter.writeObstacles(area.getObstacles(), outputObstacleFile);
            }

            double inDegreeSum = 0;
            for(Node n: nodesList)
            {
                inDegreeSum += graph.inDegree(n);
                if(graph.inDegree(n) >= 0 && graph.inDegree(n) < 10 && n.isReceiver())
                {
                    integersDistribution[graph.inDegree(n)] += 1;
                }

                if(graph.inDegree(n) >= 10)
                {
                    integersDistribution[10] += 1;
                }
            }


            boolean isFirstLink = false;

            for(int i = 0; i < remainingLinks.size() && !isFirstLink; i++ )
            {
                if(remainingLinks.get(i).getNode(0) == linksList.get(0).getNode(0) &&
                        remainingLinks.get(i).getNode(1) == linksList.get(0).getNode(1) &&
                        graph.inDegree(remainingLinks.get(i).getNode(1)) == 1)
                {
                    isFirstLink = true;
                }
            }

            InterferenceCalculator interferenceCalculator = new InterferenceCalculator(area,geometryFactory,outputInterferenceFile);
            double interferenceVal = interferenceCalculator.getReceiversInterference();

            //Conflict graph
            ConflictGraphAlgorithm conflictGraphAlgorithm = new ConflictGraphAlgorithm();
            Graph<Link,Link> conflictGraph = conflictGraphAlgorithm.getGraph(graph,linksList);

            //Coloring algorithm
            //MIS<Link,Link> mis = new MIS<Link, Link>(new DirectedGraphFactoryLinkLink(),
            //        new VertexFactoryLink(),new EdgeFactoryLink());
            //int misSize = mis.maximumIndependentSetMoonMoser(conflictGraph).size();

            int misSize = 0;
            System.out.println("Maximum independent set: "+misSize);

            //showConflictGraph(conflictGraph, "Conflict graph");
            //Coloring<Link,Link> coloring = new Coloring<Link, Link>(new DirectedGraphFactoryLinkLink());
            //int chromaticNumber = coloring.chromaticNumberBjorklundHusfeldt(conflictGraph);

            int chromaticNumber = 0;
            System.out.println("Chromatic number: "+chromaticNumber);


            CollisionProbabilityStats collisionProbabilityStats = new CollisionProbabilityStats();
            double colProb = collisionProbabilityStats.calculate(conflictGraph, linksList);

            //Count all stats about this graph
            try {

                // write out a few records
                csvOutput.write(String.valueOf(j)); // Row
                csvOutput.write(String.valueOf(links));
                csvOutput.write(String.valueOf(obstacles));
                csvOutput.write(String.valueOf((isFirstLink ? 1:0)));
                csvOutput.write(String.valueOf(colProb));
                csvOutput.write(String.valueOf(interferenceVal)); // interference level
                csvOutput.endRecord();

            }
            catch(Exception e)
            {
                System.out.println("Hello my dear, something wrong happened! Do you have write " +
                        "permission? The CsvWriter is asking for it!");

                System.exit(1);
            }

            if(outputBlockedFile != null)
            {
                Collection<Link> conflict_vertices = conflictGraph.getVertices();
                //List<Link> v = Collections.synchronizedList( new ArrayList<Link>());
                //v.addAll(conflict_vertices);
                //System.out.println(v.size());
                //Collections.sort(v);

                for(Link l: conflict_vertices)
                {
                    try {
                        csvBlocked.write(String.valueOf(l.getNode(0).getUid()));
                        csvBlocked.write(String.valueOf(l.getNode(1).getUid()));
                        csvBlocked.write(String.valueOf((l.isBlocked() ? 1 : 0)));
                        csvBlocked.endRecord();
                        csvBlocked.flush();
                    }
                    catch(Exception e)
                    {
                        System.out.println("Hello my dear, something wrong happened! Do you have write " +
                                "permission? The CsvWriter is asking for it!");

                        System.exit(1);
                    }
                }
            }

        }

        csvOutput.close();
        if(outputBlockedFile != null)
        {
            csvBlocked.close();
        }


    }

    public static void showTopologyGraph(Graph graph,List<Obstacle> obstacles ,String graphName)
    {
        Layout mVisualizer = new FRLayout(graph);
        Renderer mRenderer = new BasicRenderer();
        TopologyVisualizationViewer viewer = new TopologyVisualizationViewer(mVisualizer);
        viewer.setObstacleList(obstacles);

        mRenderer.setVertexRenderer(new TopologyGraphVertexRenderer());

        viewer.setRenderer(mRenderer);
        viewer.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Node,Link>());
        viewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        //viewer.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

        viewer.setBackground(Color.WHITE);
        JPanel panel = new JPanel();
        panel.add(viewer);

        JFrame jf = new JFrame(graphName);
        jf.getContentPane().add(panel);
        jf.setSize(1000,800);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);

    }

    public static void showConflictGraph(Graph graph, String graphName)
    {
        Layout mVisualizer = new FRLayout(graph);
        Renderer mRenderer = new BasicRenderer();

        mRenderer.setVertexRenderer(new ConflictGraphVertexRenderer());
        VisualizationViewer viewer = new VisualizationViewer(mVisualizer);

        viewer.setRenderer(mRenderer);
        viewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        //viewer.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

        viewer.setBackground(Color.WHITE);
        JPanel panel = new JPanel();
        panel.add(viewer);

        JFrame jf = new JFrame(graphName);
        jf.getContentPane().add(panel);
        jf.setSize(1000,800);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
    }


}
