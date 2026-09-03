package net.pimenta.alsim.gui.elements;

import javafx.scene.canvas.GraphicsContext;

public class GraphicNode extends GraphicElement{
    private final int nodeId;
    private double x, y;

    private static final double RADIUS = 8;

    public GraphicNode(int nodeId,double x, double y){
        this.nodeId = nodeId;
        this.x = x;
        this.y = y;
    }
    @Override
    public void draw(GraphicsContext gc){
        gc.fillOval(x - RADIUS/2,y - RADIUS/2,RADIUS,RADIUS);
    }
    @Override
    public boolean hit(double x, double y) {
        return Math.hypot(x - this.x,y - this.y) < 8;
    }

    @Override
    public boolean inside(double x, double y, double w, double h) {
        return this.x > x && this.x < x + w && this.y > y && this.y < y + h;
    }

    private double sqr(double x){
        return x * x;
    }


    public double distTo(double x, double y){
        return Math.sqrt(sqr(this.x - x) + sqr(this.y - y));
    }

    public void moveTo(double x, double y){
        this.x = x;
        this.y = y;
    }

    public int getNode(){
        return nodeId;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
