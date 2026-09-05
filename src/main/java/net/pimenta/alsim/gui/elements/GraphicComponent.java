package net.pimenta.alsim.gui.elements;

import java.util.List;

public abstract class GraphicComponent extends GraphicElement{
    protected final List<GraphicNode> nodes;
    private   String id;

    protected GraphicComponent(String id, GraphicNode... nodes){
        this.nodes = List.of(nodes);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void swapNodes(int a, int b){
        GraphicNode nodeA = nodes.get(a);
        GraphicNode nodeB = nodes.get(b);

        double tempX = nodeA.getX();
        double tempY = nodeA.getY();

        nodeA.setX(nodeB.getX());
        nodeA.setY(nodeB.getY());

        nodeB.setX(tempX);
        nodeB.setY(tempY);
    }

    public void rotateCW(){
        GraphicNode a = nodes.get(0);
        GraphicNode b = nodes.get(1);

        double cx = (a.getX() + b.getX()) / 2;
        double cy = (a.getY() + b.getY()) / 2;

        double dxA = a.getX() - cx;
        double dyA = a.getY() - cy;
        double nxA = cx - dyA;
        double nyA = cy + dxA;

        double dxB = b.getX() - cx;
        double dyB = b.getY() - cy;
        double nxB = cx - dyB;
        double nyB = cy + dxB;


        a.setX(nxA);
        a.setY(nyA);

        b.setX(nxB);
        b.setY(nyB);
    }

    public void rotateCCW(){
        GraphicNode a = nodes.get(0);
        GraphicNode b = nodes.get(1);

        double cx = (a.getX() + b.getX()) / 2;
        double cy = (a.getY() + b.getY()) / 2;

        double dxA = a.getX() - cx;
        double dyA = a.getY() - cy;
        double nxA = cx + dyA;
        double nyA = cy - dxA;

        double dxB = b.getX() - cx;
        double dyB = b.getY() - cy;
        double nxB = cx + dyB;
        double nyB = cy - dxB;


        a.setX(nxA);
        a.setY(nyA);

        b.setX(nxB);
        b.setY(nyB);
    }
}
