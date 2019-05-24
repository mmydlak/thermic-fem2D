package model.structure;

import java.util.Arrays;

public class Element {
    private int[] ID;
    private Node[] nodes;
    private Edge[] edges;

    public Element(int first, int second, int third, int fourth, Node[] nodes) {
        this.ID = new int[]{first, second, third, fourth};
        this.nodes = nodes;
        this.edges = new Edge[]{new Edge(nodes[0], nodes[1]), new Edge(nodes[1], nodes[2]), new Edge(nodes[2], nodes[3]), new Edge(nodes[3],nodes[0])};
    }

    public Element(Node[] nodes) {
        this.ID = new int[]{0, 2, 3, 1};
        this.nodes = nodes;
        this.edges = new Edge[]{new Edge(nodes[0], nodes[1]), new Edge(nodes[1], nodes[2]), new Edge(nodes[2], nodes[3]), new Edge(nodes[3],nodes[0])};
    }

    public int[] getID() {
        return ID;
    }

    //should throw Exception and catch it in LocalComponentsGenerator (there will be always ok) but it was temporarily easier to catch it here for me XD
    public Node getNode(int i) {
        if(i<0 || i>3) {
            try {
                throw new Exception("You can get only node from 0 to 3.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return nodes[i];
    }

    //should throw Exception and catch it in LocalComponentsGenerator (there will be always ok) but it was temporarily easier to catch it here for me XD
    public Edge getEdge(int i) {
        if(i<0 || i>3) {
            try {
                throw new Exception("You can get only edge from 0 to 3.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return edges[i];
    }

    public double[] getTemperatures() {
        return new double[]{nodes[0].getT(), nodes[1].getT(), nodes[2].getT(), nodes[3].getT()};
    }

    @Override
    public String toString() {
        return "Element{" +
                "ID=" + Arrays.toString(ID) +
                ",\nnodes=" + Arrays.toString(nodes) +
                ",\nedges=" + Arrays.toString(edges) +
                "\n}";
    }
}
