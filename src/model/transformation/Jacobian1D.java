package model.transformation;

import model.structure.Edge;

public class Jacobian1D extends Jacobian {

    public Jacobian1D(Edge edge) {
        if(edge.getNode(0).getX()-edge.getNode(1).getX() != 0){
            det = Math.abs(edge.getNode(0).getX()-edge.getNode(1).getX())/2;
        }
        else {
            det = Math.abs(edge.getNode(0).getY()-edge.getNode(1).getY())/2;
        }
    }

}
