package net.pimenta.alsim.util;

import net.pimenta.alsim.util.Solver.GaussJordanSolver;

public class Solving {

    public static void main(String[] args) {
        Matrix A = new Matrix(3,3);
        Vector b = new Vector(4,1,3);

        A.set(0,0,1); A.set(0,1,0); A.set(0,2,5);
        A.set(1,0,3); A.set(1,1,2); A.set(1,2,-1);
        A.set(2,0,4); A.set(2,1,3); A.set(2,2,2);

        GaussJordanSolver solver = new GaussJordanSolver();

        Vector x = solver.solve(A,b);

        System.out.println(x);
    }
}
