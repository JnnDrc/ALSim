package net.pimenta.alsim.cak;


import net.pimenta.alsim.util.GaussSeidelSolver;
import net.pimenta.alsim.util.Vector;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CircuitSolver {

    public static void main(String[] args) throws IOException {
        NetlistParser nlParser = new NetlistParser();

        Circuit circuit = nlParser.parse(Path.of("netlist.ckt"));

        MNA mna = new MNA(circuit);

        Vector x = mna.solve();

        int nodeCount      = circuit.nodeCount();

        List<VSource> vSources = collectVsources(circuit);
        List<Resistor> resistors = collectResistors(circuit);

        List<Double> vsourceCurrents = new ArrayList<>();
        List<Double> nodeVoltages    = new ArrayList<>();

        for (int i = 0; i < x.size(); i++){

            if(i < nodeCount - 1) nodeVoltages.add(x.get(i));
            else vsourceCurrents.add(x.get(i));
        }

        System.out.println("Resistors");
        for(int i = 0; i < resistors.size(); i++){
            int a = resistors.get(i).nodes.get(0).getId();
            int b = resistors.get(i).nodes.get(1).getId();

            double Ua = a == 0 ? 0 : nodeVoltages.get(a - 1);
            double Ub = b == 0 ? 0 : nodeVoltages.get(b - 1);

            double R = resistors.get(i).getR();
            double I = (Ua - Ub)/R;
            double V = R*I;
            System.out.printf("  R%d (%fR):\n    V: %f\n    I: %f\n",i+1,R,V,I);
        }
        System.out.println("Voltage Sources");
        for (int i = 0; i < vSources.size(); i++){
            double V = vSources.get(i).getV();
            double I = vsourceCurrents.get(i);
            System.out.printf("  V%d (%fV):\n    I: %f",i,V,I);
        }

    }

    private static List<Resistor> collectResistors(Circuit circuit){
        List<Resistor> resistors = new ArrayList<>();
        for(Component component : circuit.components())
            if(component instanceof Resistor resistor) resistors.add(resistor);
        return resistors;
    }

    private static List<VSource> collectVsources(Circuit circuit){
        List<VSource> vsources = new ArrayList<>();
        for (Component component : circuit.components())
            if(component instanceof VSource vSource) vsources.add(vSource);
        return vsources;
    }
}
