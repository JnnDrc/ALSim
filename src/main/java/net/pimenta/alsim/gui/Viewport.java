package net.pimenta.alsim.gui;

public class Viewport {
    private double zoom = 1.0;
    private double offsetX = 0;
    private double offsetY = 0;

    private final double ZOOM_FACTOR = 1.1;

    private final double MIN_ZOOM   = 0.1;
    private final double MAX_ZOOM   = 10.0;

    public double toScreenX(double x){
        return x * zoom + offsetX;
    }

    public double toScreenY(double y){
        return y * zoom + offsetY;
    }

    public double toWorldX(double x){
        return (x - offsetX) / zoom;
    }

    public double toWorldY(double y){
        return (y - offsetY) / zoom;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }
    public void zoomIn() {
        zoom = Math.min(MAX_ZOOM,zoom * ZOOM_FACTOR);
    }

    public void zoomOut() {
        zoom = Math.max(MIN_ZOOM, zoom / ZOOM_FACTOR);
    }

    public void pan(double dx, double dy) {
        offsetX += dx;
        offsetY += dy;
    }

    public void centerOnWorldPoint(double worldX, double worldY, double screenX, double screenY) {
        offsetX = screenX - worldX*zoom;
        offsetY = screenY - worldY*zoom;
    }
}
