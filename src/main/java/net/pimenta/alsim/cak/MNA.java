package net.pimenta.alsim.cak;

import net.pimenta.alsim.util.*;
import net.pimenta.alsim.util.Solver.GaussianSolver;
import net.pimenta.alsim.util.Solver.LinearSolver;

public class MNA {
    private final Matrix A;
    private final Vector b;
    private Vector x = null;
    private final LinearSolver solver;

    private final int nodeCount;
    private final int extraVars;

    public MNA(Circuit circuit){
       this(circuit,new GaussianSolver());
    }

    public MNA(Circuit circuit, LinearSolver solver){
        // constructor to make, with custom solver
        nodeCount = circuit.nodeCount() - 1;
        extraVars = circuit.extraVars();
        int size = nodeCount + extraVars;
        A = new Matrix(size,size);
        b = new Vector(size);

        this.solver = solver;

        for(Component component : circuit.getComponents()){
            component.stamp(this);
        }
    }

    public void add(int i, int j, double x){
        A.add(i,j,x);
    }
    public void add(int i, double x){
        b.add(i,x);
    }
    public void set(int i, int j, double x){
        A.set(i,j,x);
    }
    public void set(int i, double x){
        b.set(i,x);
    }

    public double get(int i, int j){
        return A.get(i,j);
    }

    public double get(int i){
        return b.get(i);
    }

    public Vector solve(){
        if(x == null) x = solver.solve(A,b);
        return x;
    }

    public void print(){
        System.out.println(A);
        System.out.println(" = \n");
        System.out.println(b);
    }

    public int nodeIndex(Node node){
        if(node.getId() == 0) return -1;
        return node.getId() - 1;
    }
    public int extraIndex(int offset){
        return nodeCount + offset;
    }
}
