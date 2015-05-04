package org.idipaolo.cgraph;

import org.idipaolo.cgraph.model.Node;

/**
 * Created by Igor on 18/04/2015.
 */
public class NodeFactory {

    private int nodeNumber = 0;


    public Node createNode()
    {
        nodeNumber++;
        Node node = new Node(nodeNumber);


        return node;
    }

}
