package model.structure;

import java.util.Arrays;

public class Grid {
    private Node[] nodes;
    private Element[] elements;
    private double height;
    private double length;
    private int nh;
    private int nl;

    public Grid(double height, double length, int nh, int nl, double t) {
        this.height = height;
        this.length = length;
        this.nh = nh;
        this.nl = nl;
        buildGridNodes(t);
        buildGridElements();
    }

    private void buildGridNodes(double t) {
        this.nodes = new Node[nh*nl];
        double deltax = length/(nl-1);
        double deltay = height/(nh-1);

        int tmp;
        double x;
        double y;
        for(int i=0; i<nh*nl; i++){
            tmp = i/nh;
            x = tmp*deltax;
            y = (i-tmp*nh)*deltay;
            nodes[i] = new Node(x, y, t);
            if(x==0 || x==length || y==0 || y==height) {
                nodes[i].setBoundary(true);
            }
        }
    }

    private void buildGridElements(){
        this.elements = new Element[(nh-1)*(nl-1)];
        int tmp;
        for(int i=0; i<(nh-1)*(nl-1); i++){
            tmp = i/(nh-1);
            elements[i] = new Element(i+tmp, i+tmp+nh, i+tmp+nh+1, i+tmp+1, new Node[]{nodes[i+tmp], nodes[i+tmp+nh], nodes[i+tmp+nh+1], nodes[i+tmp+1]});
        }
    }

    public Grid(Node[] nodes, Element[] elements) {
        this.nodes = nodes;
        this.elements = elements;
    }

    public Grid(Node[] nodes, Element[] elements, int nh, int nl) {
        this.nodes = nodes;
        this.elements = elements;
        this.nh = nh;
        this.nl = nl;
    }

    public Node[] getNodes() {
        return nodes;
    }

    public Element[] getElements() {
        return elements;
    }

    public String toStringCoordsAndTemp() {
        StringBuilder txt = new StringBuilder();

        for(int j=0; j<(nh); j++) {
            for (int i = (nh - 1 - j); i < (nh) * (nl); i += (nh)) {
                txt.append(nodes[i].toStringCoordsAndTemp());
                txt.append("\t\t\t");
            }
            txt.append("\n");
        }
        return txt.toString();
    }

    public String toStringCoords() {
        StringBuilder txt = new StringBuilder();
        for(int j=0; j<(nh); j++) {
            for (int i = (nh - 1 - j); i < (nh) * (nl); i += (nh)) {
                txt.append(nodes[i].toStringCoords());
                txt.append("\t\t\t");
            }
            txt.append("\n");
        }
        return txt.toString();
    }

    public String toStringTemp() {
        StringBuilder txt = new StringBuilder();
        for(int j=0; j<(nh); j++) {
            for (int i = (nh - 1 - j); i < (nh) * (nl); i += (nh)) {
                txt.append(nodes[i].toStringTemp());
                txt.append("\t");
            }
            txt.append("\n");
        }
        return txt.toString();
    }

    @Override
    public String toString() {
        return "Grid{" +
                "\nnodes[" + nodes.length + "]=" + Arrays.toString(nodes) +
                ",\nelements[" + elements.length + "]=" + Arrays.toString(elements) +
                ",\nheight=" + height +
                ",\nlength=" + length +
                "\n}";
    }
}
