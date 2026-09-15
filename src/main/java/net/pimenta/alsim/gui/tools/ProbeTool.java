package net.pimenta.alsim.gui.tools;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import net.pimenta.alsim.gui.CircuitEditor;
import net.pimenta.alsim.gui.elements.GraphicNode;
import net.pimenta.alsim.gui.elements.GraphicProbe;
import net.pimenta.alsim.gui.elements.GraphicWire;

public class ProbeTool implements Tool {
    private GraphicNode node;
    private int probeCount = 1;

    @Override
    public void mouseClicked(CircuitEditor editor, MouseButton button, int clickCount, double x, double y) {
        GraphicNode newNode = editor.findOrCreateNode(x,y);
        if(this.node == null) this.node = newNode;
        else {
            editor.addNode(this.node);
            editor.addProbe(new GraphicProbe("M" + probeCount++,this.node,new Point2D(x,y)));
            this.node = null;
            editor.clearPreview();
        }
    }

    @Override
    public void mouseMoved(CircuitEditor editor, double x, double y) {
        if(node == null){
            return;
        }

        Point2D cursor = new Point2D(x, y);

        editor.setPreview(new GraphicProbe("__PROBE_PREVIEW__",node, cursor));
    }

    @Override
    public void mouseDragged(CircuitEditor editor, double x, double y) {

    }

    @Override
    public void cancel(CircuitEditor editor) {
        node = null;
        editor.clearPreview();
    }

    @Override
    public void mouseReleased(CircuitEditor circuitEditor, MouseButton button, double x, double y) {

    }

    @Override
    public void mousePressed(CircuitEditor circuitEditor, MouseButton button, double x, double y) {

    }
}
