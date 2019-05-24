package model.structure;

import java.util.Arrays;

public class Edge {
    private final Node[] nodes;
    private boolean boundary;

    public Edge(Node node1, Node node2) {
        this.nodes = new Node[]{node1, node2};
        setBoundary();
    }

    private void setBoundary() {
        if(nodes[0].isBoundary() && nodes[1].isBoundary()) {
            boundary = true;
        }
        else {
            boundary = false;
        }
    }

    public boolean isBoundary() {
        return boundary;
    }

    public Node getNode(int i) {
        if(i<0 || i>1) {
            try {
                throw new Exception("You can get only node from 0 to 1.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return nodes[i];
    }

    @Override
    public String toString() {
        return "Edge{" +
                "nodes=" + Arrays.toString(nodes) +
                ",\nboundary=" + boundary +
                "\n}";
    }
}
