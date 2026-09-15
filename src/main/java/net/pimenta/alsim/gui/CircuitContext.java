package net.pimenta.alsim.gui;

public class CircuitContext {
    private final CircuitEditor editor;
    private final Viewport viewport;

    public CircuitContext() {
        editor = new CircuitEditor();
        viewport = new Viewport();
    }

    public CircuitEditor getEditor() {
        return editor;
    }


    public Viewport getViewport() {
        return viewport;
    }

}
