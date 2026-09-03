package net.pimenta.alsim.cak;

public class VSource extends Component{
    private final double V;

    public VSource(String id, Node neg, Node pos, double V){
        super(id,neg,pos);
        this.V = V;
    }

    public double getV() {
        return V;
    }

    @Override
    public int extraVars(){
        return 1;
    }

    @Override
    public void stamp(MNA mna) {
        int neg = nodes.get(0).getId();
        int pos = nodes.get(1).getId();
        int k = mna.extraIndex(getIndex());

        if(pos != 0){
            mna.add(pos - 1, k,  1);
            mna.add(k, pos - 1,  1);
        }
        if(neg != 0){
            mna.add(neg - 1, k, -1);
            mna.add(k, neg - 1, -1);
        }

        mna.set(k, V);
    }
}
