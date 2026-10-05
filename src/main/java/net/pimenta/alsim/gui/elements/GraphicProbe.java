package net.pimenta.alsim.gui.elements;

import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import net.pimenta.alsim.gui.simulate.NodeResult;
import net.pimenta.alsim.util.Engineering;


public class GraphicProbe extends GraphicElement{
    private final GraphicNode node;
    private Point2D     anchor;
    private String label;
    private ProbeMode mode;

    private final double HEIGHT = 25;

    public GraphicProbe(String label, GraphicNode node, Point2D anchor){
        this.label = label;
        this.node = node;
        this.anchor = anchor;
        this.mode = ProbeMode.VOLTAGE;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public GraphicNode getNode() {
        return node;
    }

    public Point2D getAnchor() {
        return anchor;
    }

    public void setAnchor(double x, double y){
        anchor = new Point2D(x,y);
    }

    public ProbeMode getMode() {
        return mode;
    }

    public void setMode(ProbeMode mode) {
        this.mode = mode;
    }

    @Override
    public void draw(GraphicsContext gc, GraphicMode mode) {
        double nx = node.getX();
        double ny = node.getY();
        double ax = anchor.getX();
        double ay = anchor.getY();

        double dx = ax - nx;
        double dy = ay - ny;

        double length = Math.hypot(dx,dy);
        if(length < 10e-9) return;

        double theta = Math.toDegrees(Math.atan2(dy,dx));

        double arrowSize = 10.0;
        double halfHeight = HEIGHT/2;

        String data = "0";
        NodeResult result = node.getResult();
        if(result != null){
            if(this.mode == ProbeMode.VOLTAGE){
                data = Engineering.format(result.getVoltage(), "V");
            }
            else if(this.mode == ProbeMode.CURRENT){
                data = Engineering.format(result.getCurrent(),"A");
            }
        }

        Paint color = Color.BLACK;

        switch (mode){
            case NORMAL -> {}
            case PREVIEW -> color = Color.GRAY;
            case SELECTED -> color = Color.RED;
        }

        gc.save();

        gc.translate(nx,ny);
        gc.rotate(theta);

        gc.setStroke(color);
        gc.setFill(color);

        double bodyStart = Math.min(arrowSize, length);

        gc.strokeLine(0,0,bodyStart, -halfHeight);
        gc.strokeLine(0,0,bodyStart, halfHeight);

        gc.strokeLine(bodyStart,-halfHeight,length,-halfHeight);
        gc.strokeLine(bodyStart,halfHeight,length,halfHeight);
        gc.strokeLine(length,-halfHeight,length,halfHeight);

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        double textX = (bodyStart + length) / 2.0;
        gc.fillText(data,textX,0);

        gc.restore();
    }

    @Override
    public boolean hit(double x, double y) {
        double nx = node.getX();
        double ny = node.getY();

        double ax = anchor.getX();
        double ay = anchor.getY();

        double dx = ax - nx;
        double dy = ay - ny;

        double length = Math.hypot(dx, dy);

        if (length < 1e-9)
            return false;

        double px = x - nx;
        double py = y - ny;

        double cos = dx / length;
        double sin = dy / length;

        double localX =  px * cos + py * sin;
        double localY = -px * sin + py * cos;

        double arrowSize = 10.0;
        double halfHeight = HEIGHT / 2.0;

        if (localX >= arrowSize &&
                localX <= length &&
                localY >= -halfHeight &&
                localY <= halfHeight) {
            return true;
        }

        if (localX >= 0 && localX <= arrowSize) {
            double halfWidth = halfHeight * (localX / arrowSize);

            return Math.abs(localY) <= halfWidth;
        }

        return false;
    }

    @Override
    public boolean inside(double x, double y, double w, double h) {
        return node.inside(x,y,w,h);
    }
}
