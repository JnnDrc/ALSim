package net.pimenta.alsim.util;

public class GaussJordanSolver implements LinearSolver{
    @Override
    public Vector solve(Matrix A, Vector b) throws IllegalArgumentException {
        if(A.rows() != A.cols() || b.size() != A.rows()) throw new IllegalArgumentException("Invalid system dimensions");
        int n = A.rows();

        for(int k = 0; k < n; k++){

            // find pivot ----
            int pivot = k;
            double max = Math.abs(A.get(k,k));

            for(int i = k + 1; i < n; i++){
                double val = Math.abs(A.get(i,k));
                if(val > max){
                    max = val;
                    pivot = i;
                }
            }

            if (max < 1e-12) throw new IllegalArgumentException("Singular matrix");

            // swap pivots ----
            if (pivot != k){
                A.swapRows(pivot,k);
                b.swap(pivot,k);
            }


            // normalize ----
            double pivotValue = A.get(k,k);
            A.scaleRow(k,1.0/pivotValue);
            b.scale(k,1.0/pivotValue);

            // eliminate ---
            for(int i = 0; i < n; i++){
                if (i == k) continue;
                double factor = A.get(i,k);
                A.addRows(i,k,-factor);
                b.add(i,k,-factor);
            }
        }

        return b;
    }
}
