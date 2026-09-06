package net.pimenta.alsim.gui.elements;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import net.pimenta.alsim.gui.simulate.NodeResult;
import net.pimenta.alsim.util.Misc;

public class GraphicVSource extends GraphicComponent{
    private static final double MAX_SIZE = 10;
    private static final double MIN_SIZE = 5;

    double V = 12;

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
    public void draw(GraphicsContext gc) {
        GraphicNode a = nodes.get(0);
        GraphicNode b = nodes.get(1);

        drawVSource(gc, a, b);
    }

    private void drawVSource(GraphicsContext gc, GraphicNode a, GraphicNode b) {
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

        // Leads
        gc.setStroke(new LinearGradient(ax,ay,x1,y1,false, CycleMethod.NO_CYCLE,new Stop(0,colorA),new Stop(2, Color.BLACK)));
        gc.strokeLine(ax, ay, x1, y1);
        gc.setStroke(new LinearGradient(bx,by,x2,y2,false, CycleMethod.NO_CYCLE,new Stop(0,colorB),new Stop(2,Color.BLACK)));
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
    }

    public double getV() {
        return V;
    }

    public void setV(double v) {
        V = v;
    }
}
