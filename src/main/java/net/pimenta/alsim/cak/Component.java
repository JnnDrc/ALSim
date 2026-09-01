package net.pimenta.alsim.cak;

import java.util.List;

public abstract class Component {
    public String           id;
    protected List<Node>    nodes;
    private int             index   = -1;

    protected Component(String id, Node... nodes){
        this.id = id;
        this.nodes = List.of(nodes);
    }

    public void setIndex(int i){
        index = i;
    }

    public int getIndex(){
        return index;
    }

    public int extraVars(){
        return 0;
    }

    public abstract void    stamp(MNA mna);
}
