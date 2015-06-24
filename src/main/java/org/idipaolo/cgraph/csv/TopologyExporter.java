package org.idipaolo.cgraph.csv;

import com.csvreader.CsvWriter;
import org.idipaolo.cgraph.graphs.TopologyGraph;
import org.idipaolo.cgraph.model.Area;
import org.idipaolo.cgraph.model.Link;
import org.idipaolo.cgraph.model.Node;
import org.idipaolo.cgraph.model.Obstacle;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by Igor on 24/06/2015.
 */
public class TopologyExporter {


    public void writeLinks(List<Link> linkList,String linkFilename)
    {
        CsvWriter csvOutput = null;

        try {
            // use FileWriter constructor that specifies open for appending
            csvOutput = new CsvWriter(new FileWriter(linkFilename, true), ',');
            // else assume that the file already has the correct header line
        }
        catch(Exception e)
        {
            System.out.println("Hello my dear, something wrong happened! Do you have write " +
                    "permission? The CsvWriter is asking for it!");
            System.exit(1);
        }


        for(Link link: linkList)
        {
            Node sender = link.getNode(0);
            Node receiver = link.getNode(1);
            try {
                csvOutput.write(String.valueOf(sender.getPosition().getX()));
                csvOutput.write(String.valueOf(sender.getPosition().getY()));
                csvOutput.endRecord();
                csvOutput.write(String.valueOf(receiver.getPosition().getX()));
                csvOutput.write(String.valueOf(receiver.getPosition().getY()));
                csvOutput.endRecord();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        csvOutput.close();
    }

    public void writeObstacles(List<Obstacle> obstacleList,String obstacleFilename)
    {
        CsvWriter csvOutput = null;

        try {
            // use FileWriter constructor that specifies open for appending
            csvOutput = new CsvWriter(new FileWriter(obstacleFilename, true), ',');
            // else assume that the file already has the correct header line
        }
        catch(Exception e)
        {
            System.out.println("Hello my dear, something wrong happened! Do you have write " +
                    "permission? The CsvWriter is asking for it!");
            System.exit(1);
        }


        for(Obstacle obstacle: obstacleList)
        {

            try {
                csvOutput.write(String.valueOf(obstacle.getStartPoint().getX()));
                csvOutput.write(String.valueOf(obstacle.getStartPoint().getY()));
                csvOutput.write(String.valueOf(obstacle.getEndPoint().getX()));
                csvOutput.write(String.valueOf(obstacle.getEndPoint().getY()));
                csvOutput.endRecord();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        csvOutput.close();
    }

}
