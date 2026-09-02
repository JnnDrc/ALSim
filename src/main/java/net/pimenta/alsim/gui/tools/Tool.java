package net.pimenta.alsim.gui.tools;

import javafx.scene.input.MouseButton;
import net.pimenta.alsim.gui.CircuitEditor;

public interface Tool {
    void mouseClicked(CircuitEditor editor,double x, double y);
    void mouseMoved(CircuitEditor editor, double x, double y);
    void mouseDragged(CircuitEditor editor, double x, double y);

    void cancel(CircuitEditor editor);

    void mouseReleased(CircuitEditor circuitEditor, MouseButton button, double x, double y);

    void mousePressed(CircuitEditor circuitEditor, MouseButton button, double x, double y);
}
