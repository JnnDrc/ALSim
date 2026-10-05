package net.pimenta.alsim.util.Solver;

import net.pimenta.alsim.util.Matrix;
import net.pimenta.alsim.util.Vector;

public interface LinearSolver {
    Vector solve(Matrix A, Vector b);
}
