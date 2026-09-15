package net.pimenta.alsim.gui.elements;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import net.pimenta.alsim.util.Misc;
import net.pimenta.alsim.util.Pair;

public class GraphicWire extends GraphicElement{
    private final GraphicNode a;
    private final GraphicNode b;

    private int id;

    public GraphicWire(int id, GraphicNode a, GraphicNode b) {
        this.id = id;
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
    public void draw(GraphicsContext gc, GraphicMode mode) {
        switch (mode){
            case NORMAL -> gc.setStroke(Color.BLACK);
            case PREVIEW -> gc.setStroke(Color.GRAY);
            case SELECTED -> gc.setStroke(Color.RED);
        }
        gc.strokeLine(a.getX(), a.getY(), b.getX(), b.getY());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pair<GraphicNode,GraphicNode> getNodes(){
        return new Pair<>(a,b);
    }

    public boolean hasNode(GraphicNode node) {
        return a.getNode() == node.getNode() || b.getNode() == node.getNode();
    }
}
