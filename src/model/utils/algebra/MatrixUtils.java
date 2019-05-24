package model.utils.algebra;

public class MatrixUtils {

    public double[][] asMatrix(double[] vector) {
        double[][] result = new double[vector.length][1];
        for(int i=0; i<vector.length; i++) {
            result[i][0] = vector[i];
        }
        return result;
    }

    public double[][] transpose(double[][] matrix) throws Exception {
        if(matrix == null || matrix[0] == null) {
            throw new Exception("Matrix indicates to nullptr.");
        }
        double[][] transposedMatrix = new double[matrix[0].length][matrix.length];
        for (int i=0;i<matrix.length;i++) {
            if(matrix[i] == null) {
                throw new Exception("Matrix[" + i + "] indicates to nullptr.");
            }
            for (int j=0;j<matrix[0].length;j++) {
                transposedMatrix[j][i] = matrix[i][j];
            }
        }
        return transposedMatrix;
    }

    public boolean isSquare(double[][] matrix) throws Exception {
        if (matrix == null || matrix[0] == null) {
            throw new Exception("Matrix indicates to nullptr.");
        }
        return matrix.length == matrix[0].length;
    }

    public double determinant(double[][] m) throws Exception {
        if (!isSquare(m)) {
            throw new Exception("Matrix is not square.");
        }
        if (m.length == 1) {
            return m[0][0];
        }
        if (m.length == 2) {
            return ((m[0][0] * m[1][1]) - (m[0][1] * m[1][0]));
        }
        if (m.length == 3) {
            return ((m[0][0]*m[1][1]*m[2][2]) + (m[0][1]*m[1][2]*m[2][0]) + (m[0][2]*m[1][0]*m[2][1])
                    - (m[0][2]*m[1][1]*m[2][0]) - (m[0][1]*m[1][0]*m[2][2]) - (m[0][0]*m[1][2]*m[2][1]));
        }
        double sum = 0.0;
        for (int i=0; i<m[0].length; i++) {
            sum += changeSign(i) * m[0][i] * determinant(createSubMatrix(m, 0, i));
        }
        return sum;
    }

    private int changeSign(int i) {
        if(i%2==0) {
            return 1;
        }
        else {
            return -1;
        }
    }

    public double[][] createSubMatrix(double[][] matrix, int excluding_row, int excluding_col) {
        double[][] result = new double[matrix.length-1][matrix[0].length-1];
        int r = -1;
        for (int i=0;i<matrix.length;i++) {
            if (i==excluding_row)
                continue;
            r++;
            int c = -1;
            for (int j=0;j<matrix[0].length;j++) {
                if (j==excluding_col)
                    continue;
                c++;
                result[r][c] = matrix[i][j];
            }
        }
        return result;
    }

    public double[][] cofactor(double[][] matrix) throws Exception {
        if (!isSquare(matrix)) {
            throw new Exception("Matrix is not square.");
        }
        double[][] result = new double[matrix.length][matrix[0].length];
        for (int i=0;i<matrix.length;i++) {
            for (int j=0; j<matrix[0].length;j++) {
                result[i][j] = changeSign(i) * changeSign(j) * determinant(createSubMatrix(matrix, i, j));
            }
        }
        return result;
    }

    public double[][] inverse(double[][] matrix) throws Exception {
        return multiply(transpose(cofactor(matrix)), 1.0/determinant(matrix));
    }


    public double[][] multiply(double[][] matrix, double constant){
        double result[][] = new double[matrix.length][matrix[0].length];
        for (int i=0;i<matrix.length;i++) {
            for (int j=0;j<matrix[0].length;j++) {
                result[i][j] = matrix[i][j]*constant;
            }
        }
        return result;
    }

    public double[][] multiply(double[][] m1, double[][] m2) throws Exception {
        if(m1[0].length != m2.length) {
            throw new Exception("Cannot multiply matrices. Improper dimensions.");
        }
        double result[][] = new double[m1.length][m2[0].length];
        for (int i=0;i<m1.length;i++) {
            for (int j=0;j<m2[0].length;j++) {
                result[i][j] = 0;
                for (int k=0;k<m1[i].length;k++){
                    result[i][j] += m1[i][k] * m2[k][j];
                }
            }

        }
        return result;
    }


    public double[][] add(double[][] m1, double[][] m2) throws Exception{
        if(m1.length != m2.length || m1[0].length != m2[0].length) {
            throw new Exception("Cannot add matrices. Improper dimensions.");
        }
        double result[][] = new double[m1.length][m1[0].length];
        for (int i=0;i<m1.length;i++) {
            for (int j=0;j<m1[0].length;j++) {
                result[i][j] = m1[i][j]+m2[i][j];
            }
        }
        return result;
    }

    public double[][] subtract(double[][] m1, double[][] m2) throws Exception{
        if(m1.length != m2.length || m1[0].length != m2[0].length) {
            throw new Exception("Cannot subtract matrices. Improper dimensions.");
        }
        double result[][] = new double[m1.length][m1[0].length];
        for (int i=0;i<m1.length;i++) {
            for (int j=0;j<m1[0].length;j++) {
                result[i][j] = m1[i][j]-m2[i][j];
            }
        }
        return result;
    }

}

