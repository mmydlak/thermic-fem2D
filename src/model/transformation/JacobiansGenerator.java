package model.transformation;

import model.structure.Element;
import model.utils.LocalComponentsGenerator;

public class JacobiansGenerator {

    public static Jacobian2D[] getJacobians2D(Element element) throws Exception {
        Jacobian2D jacobians[] = new Jacobian2D[4];
        for (int i = 0; i < 4; i++) {
            jacobians[i] = new Jacobian2D(element, LocalComponentsGenerator.getInstance().getdNdKsi()[i], LocalComponentsGenerator.getInstance().getdNdNi()[i]);
        }
        return jacobians;
    }

    public static Jacobian1D[] getJacobians1D(Element element) throws Exception {
        Jacobian1D jacobians[] = new Jacobian1D[4];
        for (int i = 0; i < 4; i++) {
            jacobians[i] = new Jacobian1D(element.getEdge(i));
        }
        return jacobians;
    }
}
