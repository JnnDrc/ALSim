package net.pimenta.alsim.gui.elements;

import javafx.scene.canvas.GraphicsContext;

public abstract class GraphicElement {
    public abstract void draw(GraphicsContext gc);
    public abstract boolean hit(double x, double y);
    public abstract boolean inside(double x, double y, double w, double h);
}
