package net.pimenta.alsim.gui.elements;

import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
import net.pimenta.alsim.gui.simulate.NodeResult;
import net.pimenta.alsim.util.Misc;

public class GraphicResistor extends GraphicComponent{
    private static final double MAX_SIZE = 40;
    private static final double MIN_SIZE = 20;

    double R = 1000;

    public GraphicResistor(String id, GraphicNode a, GraphicNode b, double R){
        super(id,a,b);
        this.R = R;
    }
    public GraphicResistor(String id, GraphicNode a, GraphicNode b) {
        super(id, a,b);
    }

    @Override
    public void draw(GraphicsContext gc, GraphicMode mode) {
        GraphicNode a = nodes.get(0);
        GraphicNode b = nodes.get(1);

        drawResistor(gc, mode, a, b);
    }

    @Override
    public boolean hit(double x, double y) {
        GraphicNode a = nodes.get(0);
        GraphicNode b = nodes.get(1);
        return Misc.distToSegment(x,y,a.getX(),a.getY(),b.getX(),b.getY()) < 10.0;
    }

    private void drawResistor(GraphicsContext gc,GraphicMode mode ,GraphicNode a, GraphicNode b) {
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

        // calculating parameters

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

        gc.setStroke(paintC);
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
