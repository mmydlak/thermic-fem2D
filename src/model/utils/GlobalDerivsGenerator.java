package model.utils;

import model.structure.Element;
import model.transformation.Jacobian2D;
import model.transformation.JacobiansGenerator;
import model.utils.algebra.MatrixUtils;

public class GlobalDerivsGenerator {

    public static double[][] getdNdx(Element element) throws Exception {
        return getdNdx(JacobiansGenerator.getJacobians2D(element));
    }

    public static double[][] getdNdx(Jacobian2D[] jacobians) throws Exception {
        MatrixUtils m = new MatrixUtils();
        LocalComponentsGenerator local = LocalComponentsGenerator.getInstance();
        double[][] dNdx = new double[4][4];
        double[][] jacobian;
        double tmp;
        for (int j = 0; j < 4; j++) {
            jacobian = jacobians[j].getJacobiMatrice();
            tmp = 1/m.determinant(jacobian);
            dNdx[j][0] = tmp*(jacobian[1][1]*local.getdNdKsi()[j][0] - jacobian[0][1]*local.getdNdNi()[j][0]);
            dNdx[j][1] = tmp*(jacobian[1][1]*local.getdNdKsi()[j][1] - jacobian[0][1]*local.getdNdNi()[j][1]);
            dNdx[j][2] = tmp*(jacobian[1][1]*local.getdNdKsi()[j][2] - jacobian[0][1]*local.getdNdNi()[j][2]);
            dNdx[j][3] = tmp*(jacobian[1][1]*local.getdNdKsi()[j][3] - jacobian[0][1]*local.getdNdNi()[j][3]);
        }
        return dNdx;
    }

    public static double[] getdNdx(Jacobian2D jacobian, int nodeNumber) throws Exception {
        MatrixUtils m = new MatrixUtils();
        LocalComponentsGenerator local = LocalComponentsGenerator.getInstance();
        double[] dNdx = new double[4];
        double[][] jacobiMatrix;
        double tmp;
        jacobiMatrix = jacobian.getJacobiMatrice();
        tmp = 1/m.determinant(jacobiMatrix);
        dNdx[0] = tmp*(jacobiMatrix[1][1]*local.getdNdKsi()[nodeNumber][0] - jacobiMatrix[0][1]*local.getdNdNi()[nodeNumber][0]);
        dNdx[1] = tmp*(jacobiMatrix[1][1]*local.getdNdKsi()[nodeNumber][1] - jacobiMatrix[0][1]*local.getdNdNi()[nodeNumber][1]);
        dNdx[2] = tmp*(jacobiMatrix[1][1]*local.getdNdKsi()[nodeNumber][2] - jacobiMatrix[0][1]*local.getdNdNi()[nodeNumber][2]);
        dNdx[3] = tmp*(jacobiMatrix[1][1]*local.getdNdKsi()[nodeNumber][3] - jacobiMatrix[0][1]*local.getdNdNi()[nodeNumber][3]);
        return dNdx;
    }

    public static double[][] getdNdy(Element element) throws Exception {
        return getdNdy(JacobiansGenerator.getJacobians2D(element));

    }

    public static double[][] getdNdy(Jacobian2D[] jacobians) throws Exception {
        MatrixUtils m = new MatrixUtils();
        LocalComponentsGenerator local = LocalComponentsGenerator.getInstance();
        double[][] dNdy = new double[4][4];
        double[][] jacobian;
        double tmp;
        for (int j = 0; j < 4; j++) {
            jacobian = jacobians[j].getJacobiMatrice();
            tmp = 1/m.determinant(jacobian);
            dNdy[j][0] = tmp*(-jacobian[1][0]*local.getdNdKsi()[j][0] + jacobian[0][0]*local.getdNdNi()[j][0]);
            dNdy[j][1] = tmp*(-jacobian[1][0]*local.getdNdKsi()[j][1] + jacobian[0][0]*local.getdNdNi()[j][1]);
            dNdy[j][2] = tmp*(-jacobian[1][0]*local.getdNdKsi()[j][2] + jacobian[0][0]*local.getdNdNi()[j][2]);
            dNdy[j][3] = tmp*(-jacobian[1][0]*local.getdNdKsi()[j][3] + jacobian[0][0]*local.getdNdNi()[j][3]);
        }
        return dNdy;
    }

    public static double[] getdNdy(Jacobian2D jacobian, int nodeNumber) throws Exception {
        MatrixUtils m = new MatrixUtils();
        LocalComponentsGenerator local = LocalComponentsGenerator.getInstance();
        double[] dNdy = new double[4];
        double[][] jacobianMatrix;
        double tmp;
        jacobianMatrix = jacobian.getJacobiMatrice();
        tmp = 1/m.determinant(jacobianMatrix);
        dNdy[0] = tmp*(-jacobianMatrix[1][0]*local.getdNdKsi()[nodeNumber][0] + jacobianMatrix[0][0]*local.getdNdNi()[nodeNumber][0]);
        dNdy[1] = tmp*(-jacobianMatrix[1][0]*local.getdNdKsi()[nodeNumber][1] + jacobianMatrix[0][0]*local.getdNdNi()[nodeNumber][1]);
        dNdy[2] = tmp*(-jacobianMatrix[1][0]*local.getdNdKsi()[nodeNumber][2] + jacobianMatrix[0][0]*local.getdNdNi()[nodeNumber][2]);
        dNdy[3] = tmp*(-jacobianMatrix[1][0]*local.getdNdKsi()[nodeNumber][3] + jacobianMatrix[0][0]*local.getdNdNi()[nodeNumber][3]);
        return dNdy;
    }
}
