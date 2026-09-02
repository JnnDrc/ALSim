package net.pimenta.alsim.cak;

import net.pimenta.alsim.util.Vector;

public class VDiv {

    public static void main(String[] args) {
        Circuit ckt = new Circuit(new Node(0));
        ckt.add(new VSource("V1", new Node(0),new Node(1),12));
        ckt.add(new Resistor("R1",new Node(1),new Node(2),100));
        ckt.add(new Resistor("R2",new Node(2),new Node(0),200));
        ckt.add(new Resistor("R3",new Node(1),new Node(0),300));

        MNA mna = new MNA(ckt);
        mna.print();
        System.out.println("--------");
        Vector x = mna.solve();

        System.out.println(x);
    }
}
