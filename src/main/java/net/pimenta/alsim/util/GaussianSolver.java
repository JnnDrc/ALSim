package net.pimenta.alsim.util;

public class GaussianSolver implements LinearSolver{
    public static final double EPS = 1e-12;

    @Override
    public Vector solve(Matrix A, Vector b) {
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

            if (max < EPS) throw new IllegalArgumentException("Singular matrix");

            // swap pivots ----
            if (pivot != k){
                A.swapRows(pivot,k);
                b.swap(pivot,k);
            }

            // eliminate ----
            for(int i = k + 1; i < n; i++){
                double factor = A.get(i,k) / A.get(k,k);
                A.set(i,k,0);
                A.addRows(i,k,-factor);
                b.add(i,k,-factor);
            }
        }

        Vector x = new Vector(b.size());
        for(int i = n - 1; i >= 0; i--){
            double sum = b.get(i);
            for(int j = i + 1; j < n; j++){
                sum -= A.get(i,j)*x.get(j);
            }
            if(Math.abs(A.get(i,i)) < EPS) throw new IllegalArgumentException("Singular matrix");
            x.set(i,sum/A.get(i,i));
        }

        return x;
    }
}
