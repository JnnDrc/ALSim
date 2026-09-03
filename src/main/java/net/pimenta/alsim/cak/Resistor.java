package net.pimenta.alsim.cak;

public class Resistor extends Component{
    private final double R;

    public Resistor(String id, Node nodeA, Node nodeB, double R){
        super(id,nodeA,nodeB);
        this.R = R;
    }

    public double getR() {
        return R;
    }

    @Override
    public void stamp(MNA mna) {
        Node a = nodes.get(0);
        Node b = nodes.get(1);
        final int aId = a.getId();
        final int bId = b.getId();
        final double G = 1/R;

        // stamp
        if(aId != 0) mna.add(aId - 1,aId - 1,G);
        if(bId != 0) mna.add(bId - 1,bId - 1,G);
        if(aId != 0 && bId != 0){
            mna.add(aId - 1,bId - 1,-G);
            mna.add(bId - 1,aId - 1,-G);
        }
    }
}
