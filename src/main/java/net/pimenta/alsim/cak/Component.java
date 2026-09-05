package net.pimenta.alsim.cak;

import java.util.List;

public abstract class Component {
    private String           id;
    protected List<Node>    nodes;
    private int             index   = -1;

    protected Component(String id, Node... nodes){
        this.id = id;
        this.nodes = List.of(nodes);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIndex(int i){
        index = i;
    }

    public List<Node> getNodes(){
        return nodes;
    }

    public int getIndex(){
        return index;
    }

    public int extraVars(){
        return 0;
    }

    public abstract void    stamp(MNA mna);
}
