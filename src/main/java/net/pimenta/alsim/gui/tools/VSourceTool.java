package net.pimenta.alsim.gui.tools;

import javafx.scene.input.MouseButton;
import net.pimenta.alsim.gui.CircuitEditor;
import net.pimenta.alsim.gui.elements.GraphicNode;
import net.pimenta.alsim.gui.elements.GraphicResistor;
import net.pimenta.alsim.gui.elements.GraphicVSource;

public class VSourceTool implements Tool {
    private GraphicNode first;
    private int vSourceCount = 1;
    @Override
    public void mouseClicked(CircuitEditor editor,MouseButton button, int clickCount, double x, double y) {
        GraphicNode node = editor.findOrCreateNode(x,y);
        if (first == null) first = node;
        else{
            editor.addNode(first);
            editor.addNode(node);
            editor.addComponent(new GraphicVSource("V" + vSourceCount++,first,node));
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

        editor.setPreview(new GraphicVSource("__VPREVIEW__",first, cursor));
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
