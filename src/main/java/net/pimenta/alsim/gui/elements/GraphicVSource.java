package net.pimenta.alsim.gui.elements;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
import net.pimenta.alsim.gui.simulate.NodeResult;
import net.pimenta.alsim.util.Engineering;
import net.pimenta.alsim.util.Misc;

public class GraphicVSource extends GraphicComponent{
    private static final double MAX_SIZE = 10;
    private static final double MIN_SIZE = 5;

    double V = 12;
    public GraphicVSource(String id, GraphicNode neg, GraphicNode pos, double V) {
        super(id, neg,pos);
        this.V = V;
    }

    public GraphicVSource(String id, GraphicNode neg, GraphicNode pos) {
        super(id, neg,pos);
    }

    @Override
    public boolean hit(double x, double y) {
        GraphicNode a = nodes.get(0);
        GraphicNode b = nodes.get(1);
        return Misc.distToSegment(x,y,a.getX(),a.getY(),b.getX(),b.getY()) < 10.0;
    }

    @Override
    public void draw(GraphicsContext gc, GraphicMode mode) {
        GraphicNode a = nodes.get(0);
        GraphicNode b = nodes.get(1);

        drawVSource(gc, mode, a, b);
    }

    private void drawVSource(GraphicsContext gc, GraphicMode mode, GraphicNode a, GraphicNode b) {
        double ax = a.getX();
        double ay = a.getY();
        double bx = b.getX();
        double by = b.getY();

        NodeResult ar = a.getResult();
        NodeResult br = b.getResult();
        double va = ar == null ? 0 : ar.getVoltage();
        double vb = br == null ? 0 : br.getVoltage();

        double dx = bx - ax;
        double dy = by - ay;

        double length = Math.hypot(dx, dy);

        if (length < 1e-6)
            return;

        double ux = dx / length;
        double uy = dy / length;

        double px = -uy;
        double py = ux;

        double bodyLength = Math.clamp(length * 0.4, MIN_SIZE, MAX_SIZE);
        double lead = (length - bodyLength) / 2;

        double x1 = ax + ux * lead;
        double y1 = ay + uy * lead;

        double x2 = bx - ux * lead;
        double y2 = by - uy * lead;

        Color colorA;
        Color colorB;

        if(Math.abs(va - vb) < 10e-9){
            colorA = Color.BLACK;
            colorB = Color.BLACK;
        }
        else if(va > vb){
            colorA = Color.LIGHTGREEN;
            colorB = Color.RED;
        }
        else{
            colorA = Color.RED;
            colorB = Color.LIGHTGREEN;
        }

        Paint paintA = Color.BLACK;
        Paint paintB = Color.BLACK;
        Paint paintC = Color.BLACK;

        switch (mode){
            case NORMAL -> {
                paintA = new LinearGradient(ax,ay,x1,y1,false, CycleMethod.NO_CYCLE,new Stop(0,colorA),new Stop(2, Color.BLACK));
                paintB = new LinearGradient(bx,by,x2,y2,false, CycleMethod.NO_CYCLE,new Stop(0,colorB),new Stop(2,Color.BLACK));
            }
            case PREVIEW -> {
                paintA = Color.GRAY;
                paintB = Color.GRAY;
                paintC = Color.GRAY;
            }
            case SELECTED -> {
                paintA = Color.RED;
                paintB = Color.RED;
                paintC = Color.RED;
            }
        }

        // Leads
        gc.setStroke(paintA);
        gc.strokeLine(ax, ay, x1, y1);
        gc.setStroke(paintB);
        gc.strokeLine(x2, y2, bx, by);

        // plates

        double longPlate = 20;
        double minorPlate = 14;

        // Body endpoints
        double p1x = ax + ux * lead;
        double p1y = ay + uy * lead;

        double p2x = bx - ux * lead;
        double p2y = by - uy * lead;

        // Leads
        gc.strokeLine(ax, ay, p1x, p1y);
        gc.strokeLine(p2x, p2y, bx, by);

        gc.setStroke(paintC);
        // Negative/short plate (A side)
        gc.strokeLine(
                p1x - px * minorPlate / 2,
                p1y - py * minorPlate / 2,
                p1x + px * minorPlate / 2,
                p1y + py * minorPlate / 2
        );

        // Positive/long plate (B side)
        gc.strokeLine(
                p2x - px * longPlate / 2,
                p2y - py * longPlate / 2,
                p2x + px * longPlate / 2,
                p2y + py * longPlate / 2
        );

        // text
        double cx = (x1 + x2) / 2;
        double cy = (y1 + y2) / 2;
        drawVSourceText(gc,mode,getId(), Engineering.format(getV(),"V"),cx,cy,ux,uy);
    }

    private void drawVSourceText(GraphicsContext gc, GraphicMode mode, String label, String value,
                                  double cx, double cy, double ux, double uy) {
        if (mode == GraphicMode.PREVIEW)
            return;

        double theta = Math.toDegrees(Math.atan2(uy, ux));

        // Keep text upright.
        if (theta> 90 || theta < -90) {
            theta += 180;
        }

        gc.save();

        gc.translate(cx, cy);
        gc.rotate(theta);

        gc.setFill(Color.BLACK);
        gc.setTextAlign(javafx.scene.text.TextAlignment.CENTER);
        gc.setTextBaseline(javafx.geometry.VPos.CENTER);

        gc.fillText(label, 0, -25);
        gc.fillText(value, 0, 25);

        gc.restore();
    }

    public double getV() {
        return V;
    }

    public void setV(double v) {
        V = v;
    }
}
