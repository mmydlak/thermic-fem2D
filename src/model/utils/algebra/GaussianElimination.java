package model.utils.algebra;

public class GaussianElimination {

    public static double[] solve(double[][] A, double[] b) throws Exception {
        int n = b.length;

        int max;
        double[] tmpArr;
        double tmp;
        double[] result = new double[n];

        for (int column = 0; column < n; column++) {
            max = column;
            for (int i = column + 1; i < n; i++) {
                if (Math.abs(A[i][column]) > Math.abs(A[max][column])) {
                    max = i;
                }
            }
            tmpArr = A[column];
            A[column] = A[max];
            A[max] = tmpArr;

            tmp = b[column];
            b[column] = b[max];
            b[max] = tmp;


            for (int row = column + 1; row < n; row++) {
                tmp = A[row][column] / A[column][column];
                b[row] -= tmp * b[column];
                for (int k2 = column; k2 < n; k2++) {
                    A[row][k2] -= tmp * A[column][k2];
                }
            }
        }


        for (int row = n - 1; row >= 0; row--) {
            result[row] = b[row];
            for (int column = n-1; column >=row+1; column--) {
                result[row] -= A[row][column] * result[column];
            }
            if(A[row][row]==0) {
                throw new Exception("Cannot solve. Indeterminate equation system\n.");
            }
            result[row] /= A[row][row];
        }

        return result;
    }

}