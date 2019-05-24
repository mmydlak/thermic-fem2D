package model.simulator;

import model.structure.Element;
import model.structure.Grid;
import model.utils.GlobalDerivsGenerator;
import model.transformation.Jacobian1D;
import model.transformation.Jacobian2D;
import model.utils.file.FileHandler;
import model.utils.algebra.GaussianElimination;
import model.utils.LocalComponentsGenerator;
import model.utils.algebra.MatrixUtils;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Arrays;

public class HeatFlowFEM {

    private double height, length;          //grid height and length
    private int nh, nl;                     //number of nodes on height and length
    private double tBegin;                  //begin temperature
    private double tau;                     //process time
    private double dtau;                    //time step
    private double tAmbient;                //surrounding temperature
    private double alfa;                    //convection (heat transfer coefficient)
    private double c;                       //specific heat
    private double k;                       //conductivity
    private double ro;                      //density

    private long startTime;

    private MatrixUtils m;

    public static void main(String[] args) {
        HeatFlowFEM executor = new HeatFlowFEM();
        try {
            executor.initializeData();
            executor.compute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public HeatFlowFEM() {
        this.m = new MatrixUtils();
        this.startTime = 0;
    }

    private void initializeData() throws IOException {
        String JSON = FileHandler.readFromFile("src/model/data/data.json");
        JSONObject data = new JSONObject(JSON);
        height = data.getDouble("height");
        length = data.getDouble("length");
        nh = data.getInt("nh");
        nl = data.getInt("nl");
        tBegin = data.getDouble("tBegin");
        tau = data.getDouble("tau");
        dtau = data.getDouble("dtau");
        tAmbient = data.getDouble("tAmbient");
        alfa = data.getDouble("alfa");
        c = data.getDouble("c");
        k = data.getDouble("k");
        ro = data.getDouble("ro");
    }

    public void compute() throws Exception {
        Grid grid = new Grid(height, length, nh, nl, tBegin);
        //System.out.println(grid.toStringCoordsAndTemp());

        double asr = k/(c*ro);
        double ntau = Math.pow((length/nl),2)/(0.5*asr);
        System.out.println(ntau);

        //result
        double[] tCalculated;

        //global
        double[][] globalH = new double[nh*nl][nh*nl];
        double[] globalP = new double[nh*nl];

        //for data calculated for each element
        double[][] H = new double[4][4];
        double[][] C = new double[4][4];
        double[] P = new double[4];
        double[] dNdx;
        double[] dNdy;
        double[] t0;
        Jacobian2D jacobian2D;
        Jacobian1D jacobian1D;

        //temporary use for clarity
        double[][] Ctau;
        double detJ;
        double[][][] partOfSum = new double[2][][];
        double[][] sumOfMatrices;

        //calculated once
        double[][] Ns = LocalComponentsGenerator.getInstance().getNs();
        double[][][] edgeNs = LocalComponentsGenerator.getInstance().getEdgeNs();


        //for each time step
        for (int itau = 0; itau < tau; itau += dtau) {
            System.out.println("Time: " + (itau+dtau));

            startTime = System.currentTimeMillis();

            //fill global matrices with 0
            for (int i = 0; i < nh*nl; i++) {
                for (int j = 0; j < nh*nl; j++) {
                    globalH[i][j] = 0;
                }
                globalP[i] = 0;
            }

            for (Element element : grid.getElements()) {
                //fill local matrices with 0
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        H[i][j] = 0;
                        C[i][j] = 0;
                    }
                    P[i] = 0;
                }

                //GAUSS INTEGRATION (volume integrals)
                //for each integration point (node)
                for (int i = 0; i < 4; i++) {
                    //calculate jacobi matrice for node number i
                    jacobian2D = new Jacobian2D(element, i);

                    //calculate global derivatives for node number i
                    dNdx = GlobalDerivsGenerator.getdNdx(jacobian2D, i);
                    dNdy = GlobalDerivsGenerator.getdNdy(jacobian2D, i);

                    //{dNdx}*{dNdx}^T + {dNdy}*{dNdy}^T
                    partOfSum[0] = m.multiply(m.asMatrix(dNdx), m.transpose(m.asMatrix(dNdx)));
                    partOfSum[1] = m.multiply(m.asMatrix(dNdy), m.transpose(m.asMatrix(dNdy)));
                    sumOfMatrices = m.add(partOfSum[0], partOfSum[1]);

                    //det[J]
                    detJ = jacobian2D.getDet();

                    //calculate local H (without boundary conditions) and local C matrix
                    H = m.add(H, m.multiply(sumOfMatrices, k * detJ));
                    C = m.add(C, m.multiply(m.multiply(m.asMatrix(Ns[i]), m.transpose(m.asMatrix(Ns[i]))), c * ro * detJ));
                }


                //boundary conditions
                //for each element edge
                for (int eNumber = 0; eNumber < 4; eNumber++) {
                    //but only for boundary ones
                    if (element.getEdge(eNumber).isBoundary()) {
                        //GAUSS INTEGRATION (surface integrals)
                        jacobian1D = new Jacobian1D(element.getEdge(eNumber));

                        //only two integration points for edge (loop not necessary)
                        //{edgeN}*{edgeN}^T two times
                        partOfSum[0] = m.multiply(m.asMatrix(edgeNs[eNumber][0]), m.transpose(m.asMatrix(edgeNs[eNumber][0])));
                        partOfSum[1] = m.multiply(m.asMatrix(edgeNs[eNumber][1]), m.transpose(m.asMatrix(edgeNs[eNumber][1])));
                        sumOfMatrices = m.add(partOfSum[0], partOfSum[1]);

                        detJ = jacobian1D.getDet();

                        //adding boundary conditions to local H matrix
                        H = m.add(H, m.multiply(sumOfMatrices, alfa * detJ));

                        //calculating local P vector
                        //{edgeN} two times
                        sumOfMatrices = m.add(m.asMatrix(edgeNs[eNumber][0]), m.asMatrix(edgeNs[eNumber][1]));
                        //have not implemented vector operations so have to treat as matrix, then transpose and take only first (0) row
                        P = m.transpose(m.add(m.asMatrix(P), (m.multiply(sumOfMatrices, alfa * tAmbient * detJ))))[0];
                    }
                }

                //from problem's formula
                Ctau = m.multiply(C, 1 / dtau);
                //t0p += temp_0[j] * el_lok.getN()[pc][j];
                t0 = element.getTemperatures();
                H = m.add(H, Ctau);
                P = m.transpose(m.add(m.asMatrix(P), m.multiply(Ctau, m.asMatrix(t0))))[0];

                //aggregate to global stiffness matrix and global load vector
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        globalH[element.getID()[i]][element.getID()[j]] += H[i][j];
                    }
                    globalP[element.getID()[i]] += P[i];
                }
            }


            //solving system of equations to get temperatures after next step time
            tCalculated = GaussianElimination.solve(globalH, globalP);

            //setting new temperature in nodes
            for (int i = 0; i < nh*nl; i++) {
                grid.getNodes()[i].setT(tCalculated[i]);
            }
            System.out.println(grid.toStringTemp());
            if(tCalculated[0]<42) break;

            long estimatedTime = System.currentTimeMillis() - startTime;
            //System.out.println(estimatedTime);
        }
    }

    public static void printArray2D(double[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.println(Arrays.toString(arr[i]));
        }
    }
}