package net.pimenta.alsim.gui.elements;

import java.util.List;

public abstract class GraphicComponent extends GraphicElement{
    protected final List<GraphicNode> nodes;

    protected GraphicComponent(GraphicNode... nodes){
        this.nodes = List.of(nodes);
    }

    public List<GraphicNode> getNodes() {
        return nodes;
    }

    public boolean hasNode(GraphicNode node){
        for (GraphicNode n : nodes){
            if (node.getNode() == n.getNode()) return true;
        }
        return false;
    }

    @Override
    public boolean inside(double x, double y, double w, double h) {
        for(GraphicNode node : nodes){
            if(node.inside(x,y,w,h)) return true;
        }
        return false;
    }
}
