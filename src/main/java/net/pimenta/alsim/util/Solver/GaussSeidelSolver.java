package net.pimenta.alsim.util.Solver;

import net.pimenta.alsim.util.Matrix;
import net.pimenta.alsim.util.Vector;

public class GaussSeidelSolver implements LinearSolver {
    private final double tolerance;
    private final int maxIterations;
    public GaussSeidelSolver (double tolerance, int maxIterations){
        this.tolerance = tolerance;
        this.maxIterations = maxIterations;
    }
    @Override
    public Vector solve(Matrix A, Vector b) {
        System.out.println(A);
        System.out.println(b);
        Vector x = new Vector(b.size());
        System.out.println(x);
        for(int k = 0; k < this.maxIterations; k++){
            double maxError = 0.0;

            for(int i = 0; i < b.size(); i++){
                double sum = 0.0;

                for(int j = 0; j < b.size(); j++){
                    if(j != i) sum += A.get(i,j)*x.get(j);
                }

                double newXi = (b.get(i) - sum)/A.get(i,i);
                maxError = Math.max(maxError,Math.abs(newXi - x.get(i)));

                x.set(i,newXi);
            }

            System.out.printf("x_(k=%d)",k);
            System.out.println(x);

            if(maxError < this.tolerance) break;
        }

        return x;
    }
}
