package model.structure;

public class Node {
    private final double x;
    private final double y;
    private double t;
    private boolean boundary;

    public Node(double x, double y, double t, boolean boundary) {
        this.x = x;
        this.y = y;
        this.t = t;
        this.boundary = boundary;
    }

    public Node(double x, double y, double t) {
        this.x = x;
        this.y = y;
        this.t = t;
        this.boundary = false;
    }

    public Node(double x, double y) {
        this.x = x;
        this.y = y;
        this.t = 0;
        this.boundary = false;
    }

    public void setBoundary(boolean boundary) {
        this.boundary = boundary;
    }

    public boolean isBoundary() {
        return boundary;
    }

    public void setT(double t0) {
        this.t = t0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getT() {
        return t;
    }

    public String toStringCoordsAndTemp() {
        return "("+String.format("%.3f", x)+" ; "+String.format("%.3f", y)+"),t="+String.format("%.3f", t);
    }

    public String toStringCoords() {
        return "("+String.format("%.3f", x)+" ; "+String.format("%.3f", y)+")";
    }

    public String toStringTemp() {
        return ""+String.format("%.3f", t);
    }

    @Override
    public String toString() {
        return "Node{" +
                "x=" + x +
                ", y=" + y +
                ", t=" + t +
                '}';
    }

}
