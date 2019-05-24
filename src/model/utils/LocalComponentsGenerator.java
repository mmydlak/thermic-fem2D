package model.utils;

import model.structure.Edge;
import model.structure.Element;
import model.structure.Node;

public class LocalComponentsGenerator {

    private static LocalComponentsGenerator localComponentsGenerator;
    private Element localElement;
    private Edge[] localEdges;
    private double[][] Ns;
    private double[][][] edgeNs;
    private double[][] dNdKsi;
    private double[][] dNdNi;

    public static LocalComponentsGenerator getInstance() {
        if(localComponentsGenerator == null) {
            localComponentsGenerator = new LocalComponentsGenerator();
        }
        return localComponentsGenerator;
    }

    private LocalComponentsGenerator() {
        createLocalElement();
        createLocalEdges();

        //1 index: number of integrstion points (nodes)
        //2 index: number of shape functions
        Ns = new double[4][4];
        dNdKsi = new double[4][4];
        dNdNi = new double[4][4];
        //1 index: nuber of edges
        //2 index: number of integration points (nodes)
        //3 index: number of shape functions
        edgeNs = new double[4][2][4];

        //for each integration point / each edge
        for(int i=0; i<4; i++) {
            calculateNs(i);
            calculateEdgeNs(i);
            calculatedNdKsi(i);
            calculatedNdNi(i);
        }
    }

    private void createLocalElement() {
        Node localNodes[] = new Node[]{ new Node(-1.0 / Math.sqrt(3.0), -1.0 / Math.sqrt(3.0)),
                                        new Node(1.0 / Math.sqrt(3.0), -1.0 / Math.sqrt(3.0)),
                                        new Node(1.0 / Math.sqrt(3.0), 1.0 / Math.sqrt(3.0)),
                                        new Node(-1.0 / Math.sqrt(3.0),1.0 / Math.sqrt(3.0)) };
        localElement = new Element(new Node[]{localNodes[0], localNodes[1], localNodes[2], localNodes[3]});
    }

    private void createLocalEdges(){
        localEdges = new Edge[] {   new Edge(new Node(-1.0 / Math.sqrt(3.0), -1.0), new Node(1.0 / Math.sqrt(3.0), -1.0)),
                                    new Edge(new Node(1.0, -1.0 / Math.sqrt(3.0)), new Node(1.0, 1.0 / Math.sqrt(3.0))),
                                    new Edge(new Node(1.0 / Math.sqrt(3.0), 1.0), new Node(-1.0 / Math.sqrt(3.0), 1.0)),
                                    new Edge(new Node(-1.0, 1.0 / Math.sqrt(3.0)), new Node(-1.0, -1.0 / Math.sqrt(3.0))) };

    }

    private void calculateNs(int i) {
        Ns[i][0] = N1(localElement.getNode(i).getX(), localElement.getNode(i).getY());
        Ns[i][1] = N2(localElement.getNode(i).getX(), localElement.getNode(i).getY());
        Ns[i][2] = N3(localElement.getNode(i).getX(), localElement.getNode(i).getY());
        Ns[i][3] = N4(localElement.getNode(i).getX(), localElement.getNode(i).getY());
    }

    private void calculatedNdKsi(int i) {
        dNdKsi[i][0] = dN1dKsi(localElement.getNode(i).getX(), localElement.getNode(i).getY());
        dNdKsi[i][1] = dN2dKsi(localElement.getNode(i).getX(), localElement.getNode(i).getY());
        dNdKsi[i][2] = dN3dKsi(localElement.getNode(i).getX(), localElement.getNode(i).getY());
        dNdKsi[i][3] = dN4dKsi(localElement.getNode(i).getX(), localElement.getNode(i).getY());
    }

    private void calculatedNdNi(int i) {
        dNdNi[i][0] = dN1dNi(localElement.getNode(i).getX(), localElement.getNode(i).getY());
        dNdNi[i][1] = dN2dNi(localElement.getNode(i).getX(), localElement.getNode(i).getY());
        dNdNi[i][2] = dN3dNi(localElement.getNode(i).getX(), localElement.getNode(i).getY());
        dNdNi[i][3] = dN4dNi(localElement.getNode(i).getX(), localElement.getNode(i).getY());
    }

    private void calculateEdgeNs(int i) {
        //matrices of shape functions for edges: 4 shape functions for 2 integration points * 4 edges
        //can be 2 shape functions for 2 integration points, but there is not XD (would be more difficult to put in H matrix later and more coding here XD)
        for (int j = 0; j < 2; j++) {
            edgeNs[i][j][0] = N1(localEdges[i].getNode(j).getX(), localEdges[i].getNode(j).getY());
            edgeNs[i][j][1] = N2(localEdges[i].getNode(j).getX(), localEdges[i].getNode(j).getY());
            edgeNs[i][j][2] = N3(localEdges[i].getNode(j).getX(), localEdges[i].getNode(j).getY());
            edgeNs[i][j][3] = N4(localEdges[i].getNode(j).getX(), localEdges[i].getNode(j).getY());
        }
    }

    private double N1(double ksi, double ni) {
        return 0.25 * (1 - ksi) * (1 - ni);
    }
    private double N2(double ksi, double ni) {
        return 0.25 * (1 + ksi) * (1 - ni);
    }
    private double N3(double ksi, double ni) {
        return 0.25 * (1 + ksi) * (1 + ni);
    }
    private double N4(double ksi, double ni) {
        return 0.25 * (1 - ksi) * (1 + ni);
    }
    private double dN1dKsi(double ksi, double ni) {
        return -0.25 * (1 - ni);
    }
    private double dN2dKsi(double ksi, double ni) {
        return 0.25 * (1 - ni);
    }
    private double dN3dKsi(double ksi, double ni) {
        return 0.25 * (1 + ni);
    }
    private double dN4dKsi(double ksi, double ni) {
        return -0.25 * (1 + ni);
    }
    private double dN1dNi(double ksi, double ni) {
        return -0.25 * (1 - ksi);
    }
    private double dN2dNi(double ksi, double ni) {
        return -0.25 * (1 + ksi);
    }
    private double dN3dNi(double ksi, double ni) {
        return 0.25 * (1 + ksi);
    }
    private double dN4dNi(double ksi, double ni) {
        return 0.25 * (1 - ksi);
    }

    public Element getLocalElement() {
        return localElement;
    }

    public double[][] getNs() {
        return Ns;
    }

    public double[][] getdNdKsi() {
        return dNdKsi;
    }

    public double[][] getdNdNi() {
        return dNdNi;
    }

    public double[][][] getEdgeNs() {
        return edgeNs;
    }

}
