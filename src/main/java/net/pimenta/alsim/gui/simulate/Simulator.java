package net.pimenta.alsim.gui.simulate;

import net.pimenta.alsim.cak.*;
import net.pimenta.alsim.gui.CircuitEditor;
import net.pimenta.alsim.gui.NetlistGenerator;
import net.pimenta.alsim.util.Vector;

import java.io.IOException;

public class Simulator {

    public SimulationResults simulate(CircuitEditor editor) throws IOException {
        String netlist = NetlistGenerator.generate(editor);
        System.out.println("Generated netlist: ");
        System.out.println(netlist);

        Circuit circuit = CircuitConverter.convert(editor);
        MNA mna = new MNA(circuit);
        System.out.println("Simulation: system is: ");
        mna.print();
        mna.solve();

        SimulationResults results = new SimulationResults();

        calculateNodeResults(circuit,mna,results);

        calculateComponentResults(circuit,mna,results);

        return results;
    }

    private void calculateNodeResults(Circuit circuit, MNA mna, SimulationResults results){
        for(Node node : circuit.getNodes()){
            int nodeId = node.getId();
            double voltage;
            if(nodeId == 0) voltage = 0;
            else{
                int index = mna.nodeIndex(node);
                voltage = mna.solve().get(index);
            }
            results.addNodeResult(nodeId,new NodeResult(voltage,0) );
        }
    }

    private void calculateComponentResults(Circuit circuit, MNA mna, SimulationResults results){
        for(Component component : circuit.getComponents()){
            switch (component){
                case Resistor resistor -> {
                    Node a = resistor.getNodes().get(0);
                    Node b = resistor.getNodes().get(1);

                    double Ua = results.getNodeResult(a.getId()).getVoltage();
                    double Ub = results.getNodeResult(b.getId()).getVoltage();

                    double R = resistor.getR();

                    double I = (Ua - Ub) / R;
                    double V = I * R;
                    results.addComponentResult(resistor.getId(),new ComponentResult(V,I,V*I,R));
                }
                case VSource vSource ->{
                    double V = vSource.getV();
                    double I = mna.solve().get(mna.extraIndex(vSource.getIndex()));

                    results.addComponentResult(vSource.getId(),new ComponentResult(V,I,V*I,0));
                }
                default -> {
                    System.out.println("[ERROR]::UNK");
                }
            }
        }
    }
}
