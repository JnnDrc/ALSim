package net.pimenta.alsim.gui.simulate;

import net.pimenta.alsim.cak.Circuit;
import net.pimenta.alsim.cak.NetlistParser;
import net.pimenta.alsim.gui.CircuitEditor;
import net.pimenta.alsim.gui.NetlistGenerator;

import java.io.IOException;

public class CircuitConverter {
    public static Circuit convert(CircuitEditor editor) throws IOException {
        String netlist = NetlistGenerator.generate(editor);
        return NetlistParser.parse(netlist);
    }
}
