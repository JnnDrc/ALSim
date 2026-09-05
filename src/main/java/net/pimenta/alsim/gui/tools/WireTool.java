package net.pimenta.alsim.gui.tools;

import javafx.scene.input.MouseButton;
import net.pimenta.alsim.gui.CircuitEditor;
import net.pimenta.alsim.gui.elements.GraphicNode;
import net.pimenta.alsim.gui.elements.GraphicVSource;
import net.pimenta.alsim.gui.elements.GraphicWire;

public class WireTool implements Tool {
    private GraphicNode first;
    @Override
    public void mouseClicked(CircuitEditor editor,MouseButton button, int clickCount, double x, double y) {
        GraphicNode node = editor.findOrCreateNode(x,y);
        if (first == null) first = node;
        else{
            editor.addNode(first);
            editor.addNode(node);
            editor.addWire(new GraphicWire(first,node));
            first = null;
            editor.clearPreview();
        }
    }

    @Override
    public void mouseMoved(CircuitEditor editor, double x, double y) {
        if(first == null){
            return;
        }

        GraphicNode cursor = new GraphicNode(-1, x, y);

        editor.setPreview(new GraphicWire(first, cursor));
    }

    @Override
    public void mouseDragged(CircuitEditor editor, double x, double y) {

    }

    @Override
    public void cancel(CircuitEditor editor) {
        first = null;
        editor.clearPreview();
    }

    @Override
    public void mouseReleased(CircuitEditor circuitEditor, MouseButton button, double x, double y) {

    }

    @Override
    public void mousePressed(CircuitEditor circuitEditor, MouseButton button, double x, double y) {

    }
}
