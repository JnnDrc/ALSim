package net.pimenta.alsim.cak;

import net.pimenta.alsim.util.Vector;

import java.io.IOException;
import java.nio.file.Path;

public class Netlist {

    public static void main(String[] args) throws IOException {
        NetlistParser nlParser = new NetlistParser();

        Circuit circuit = nlParser.parse(Path.of("netlist.ckt"));

        MNA mna = new MNA(circuit);

        Vector x = mna.solve();

        System.out.println(x);
    }
}
