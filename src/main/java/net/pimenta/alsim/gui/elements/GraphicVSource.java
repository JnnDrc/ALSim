package net.pimenta.alsim.gui.elements;

import javafx.scene.canvas.GraphicsContext;
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

        drawVSource(gc, a.getX(), a.getY(), b.getX(), b.getY());
    }

    private void drawVSource(GraphicsContext gc, double ax, double ay, double bx, double by) {
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

        // Leads
        gc.strokeLine(ax, ay, x1, y1);
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
