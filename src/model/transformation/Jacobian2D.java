package model.transformation;

import model.structure.Element;
import model.utils.LocalComponentsGenerator;
import model.utils.algebra.MatrixUtils;

//jacobian in det of Jacobi matrix btw
public class Jacobian2D extends Jacobian{
        private double[][] jacobiMatrice;

        public Jacobian2D(Element element, double[] dNdKsi, double[] dNdNi) throws Exception {
            calculateMatrixElements(element, dNdKsi, dNdNi);
            det = new MatrixUtils().determinant(jacobiMatrice);
        }

        public Jacobian2D(Element element, int nodeNumber) throws Exception {
            double[] dNdKsi = LocalComponentsGenerator.getInstance().getdNdKsi()[nodeNumber];
            double[] dNdNi = LocalComponentsGenerator.getInstance().getdNdNi()[nodeNumber];
            calculateMatrixElements(element, dNdKsi, dNdNi);
            det = new MatrixUtils().determinant(jacobiMatrice);
        }

    private void calculateMatrixElements(Element element, double[] dNdKsi, double[] dNdNi) {
        double dxdKSI;
        double dydKSI;
        double dxdNI;
        double dydNI;
        jacobiMatrice = new double[2][2];
        dxdKSI = dNdKsi[0] * element.getNode(0).getX() + dNdKsi[1] * element.getNode(1).getX() + dNdKsi[2] * element.getNode(2).getX() + dNdKsi[3] * element.getNode(3).getX();
        dydKSI = dNdKsi[0] * element.getNode(0).getY() + dNdKsi[1] * element.getNode(1).getY() + dNdKsi[2] * element.getNode(2).getY() + dNdKsi[3] * element.getNode(3).getY();
        dxdNI = dNdNi[0] * element.getNode(0).getX() + dNdNi[1] * element.getNode(1).getX() + dNdNi[2] * element.getNode(2).getX() + dNdNi[3] * element.getNode(3).getX();
        dydNI = dNdNi[0] * element.getNode(0).getY() + dNdNi[1] * element.getNode(1).getY() + dNdNi[2] * element.getNode(2).getY() + dNdNi[3] * element.getNode(3).getY();

        jacobiMatrice[0][0] = dxdKSI;
        jacobiMatrice[0][1] = dydKSI;
        jacobiMatrice[1][0] = dxdNI;
        jacobiMatrice[1][1] = dydNI;
    }

        public double[][] getJacobiMatrice() {
            double[][] result = new double[jacobiMatrice.length][];
            for (int i = 0; i < jacobiMatrice.length; i++) {
                result[i] = jacobiMatrice[i].clone();
            }
            return result;
        }

}
