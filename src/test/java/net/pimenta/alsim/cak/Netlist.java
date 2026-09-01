package net.pimenta.alsim.cak;

import net.pimenta.alsim.math.Vector;

import java.nio.file.Path;

public class Netlist {

    public static void main(String[] args) {
        NetlistParser nlParser = new NetlistParser();

        Circuit circuit = nlParser.parse(Path.of("vdiv.ckt"));

        MNA mna = new MNA(circuit);

        Vector x = mna.solve();

        System.out.println(x);
    }
}
