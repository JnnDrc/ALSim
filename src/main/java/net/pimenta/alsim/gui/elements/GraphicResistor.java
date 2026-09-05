package net.pimenta.alsim.gui.elements;

import javafx.scene.canvas.GraphicsContext;
import net.pimenta.alsim.util.Misc;

public class GraphicResistor extends GraphicComponent{
    private static final double MAX_SIZE = 40;
    private static final double MIN_SIZE = 20;

    double R = 1000;

    public GraphicResistor(String id, GraphicNode a, GraphicNode b) {
        super(id, a,b);
    }

    @Override
    public void draw(GraphicsContext gc) {
        GraphicNode a = nodes.get(0);
        GraphicNode b = nodes.get(1);

        drawResistor(gc, a.getX(), a.getY(), b.getX(), b.getY());
    }

    @Override
    public boolean hit(double x, double y) {
        GraphicNode a = nodes.get(0);
        GraphicNode b = nodes.get(1);
        return Misc.distToSegment(x,y,a.getX(),a.getY(),b.getX(),b.getY()) < 10.0;
    }

    private void drawResistor(GraphicsContext gc, double ax, double ay, double bx, double by) {
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

        double amplitude = 8;

        double lead = (length - bodyLength) / 2;

        double x1 = ax + ux * lead;
        double y1 = ay + uy * lead;

        double x2 = bx - ux * lead;
        double y2 = by - uy * lead;

        // Leads
        gc.strokeLine(ax, ay, x1, y1);
        gc.strokeLine(x2, y2, bx, by);

        // Zigzag
        int segments = 6;

        double lastX = x1;
        double lastY = y1;

        for (int i = 1; i <= segments; i++) {
            double t = (double) i / segments;

            double cx = x1 + (x2 - x1) * t;
            double cy = y1 + (y2 - y1) * t;

            double offset;

            if (i == segments) {
                offset = 0;
            } else {
                offset = (i % 2 == 1)
                        ? amplitude
                        : -amplitude;
            }

            double x = cx + px * offset;
            double y = cy + py * offset;

            gc.strokeLine(lastX, lastY, x, y);

            lastX = x;
            lastY = y;
        }
    }

    public double getR() {
        return R;
    }

    public void setR(double r) {
        R = r;
    }
}
