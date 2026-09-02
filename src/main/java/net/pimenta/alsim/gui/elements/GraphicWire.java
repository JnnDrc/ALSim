package net.pimenta.alsim.gui.elements;

import javafx.scene.canvas.GraphicsContext;
import net.pimenta.alsim.util.Misc;
import net.pimenta.alsim.util.Pair;

public class GraphicWire extends GraphicElement{
    private final GraphicNode a;
    private final GraphicNode b;

    public GraphicWire(GraphicNode a, GraphicNode b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean hit(double x, double y) {
        return Misc.distToSegment(x,y,a.getX(),a.getY(),b.getX(),b.getY()) < 10.0;
    }

    @Override
    public boolean inside(double x, double y, double w, double h) {
        return a.inside(x,y,w,h) || b.inside(x,y,w,h);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.strokeLine(a.getX(), a.getY(), b.getX(), b.getY());
    }

    public Pair<GraphicNode,GraphicNode> getNodes(){
        return new Pair<>(a,b);
    }

    public boolean hasNode(GraphicNode node) {
        return a.getNode() == node.getNode() || b.getNode() == node.getNode();
    }
}
