package net.pimenta.alsim.cli;

import net.pimenta.alsim.cak.Circuit;
import net.pimenta.alsim.cak.MNA;
import net.pimenta.alsim.cak.NetlistParser;
import net.pimenta.alsim.math.Vector;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Path path = Path.of(args[0]);

        Circuit circuit = new NetlistParser().parse(path);
        MNA mna = new MNA(circuit);
        Vector x = mna.solve();

        System.out.println("Resultant system:");
        System.out.println(x);
    }
}
