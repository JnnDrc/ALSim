package net.pimenta.alsim.gui.tools;

import javafx.scene.input.MouseButton;
import net.pimenta.alsim.gui.CircuitEditor;
import net.pimenta.alsim.gui.elements.GraphicComponent;
import net.pimenta.alsim.gui.elements.GraphicElement;
import net.pimenta.alsim.gui.elements.GraphicNode;
import net.pimenta.alsim.gui.elements.GraphicWire;
import net.pimenta.alsim.util.Pair;

import javafx.scene.shape.Rectangle;

import java.util.HashSet;
import java.util.Set;


public class SelectTool implements Tool{
    private boolean dragging = false;
    private double dragStartX;
    private double dragStartY;

    private boolean selecting = false;
    double selectionLeft;
    double selectionTop;
    double selectionWidth;
    double selectionHeight;


    @Override
    public void mouseClicked(CircuitEditor editor,MouseButton button, int clickCount, double x, double y) {
        if(clickCount >= 2){
            GraphicElement selected = editor.findElementAt(x,y);

            if(selected != null){
                editor.clearSelection();
                editor.addSelected(selected);
                editor.editProperties(selected);
            }
        }
    }

    @Override
    public void mouseMoved(CircuitEditor editor, double x, double y) {

    }

    @Override
    public void mouseDragged(CircuitEditor editor, double x, double y) {
        if(dragging) dragSelected(editor,x,y);
        if(selecting) dragSelectionBox(editor,x,y);
    }

    private void dragSelected(CircuitEditor editor,double x, double y){
        if(editor.getSelected().isEmpty()) return;
        double dx = editor.snap(x) - editor.snap(dragStartX);
        double dy = editor.snap(y) - editor.snap(dragStartY);

        Set<GraphicNode> nodes = new HashSet<>();

        for(GraphicElement ge : editor.getSelected()){
            if(ge instanceof GraphicComponent component){
               nodes.addAll(component.getNodes());
            }
            else if(ge instanceof GraphicWire wire){
                Pair<GraphicNode,GraphicNode> wireNodes = wire.getNodes();
                nodes.add(wireNodes.getFirst());
                nodes.add(wireNodes.getSecond());
            }
        }

        for(GraphicNode node : nodes){
            node.setX(node.getX() + dx);
            node.setY(node.getY() + dy);
        }

        dragStartX = x;
        dragStartY = y;
    }
    private void dragSelectionBox(CircuitEditor editor, double x, double y) {
        selectionWidth  = x - selectionLeft;
        selectionHeight = y - selectionTop;
    }

    @Override
    public void cancel(CircuitEditor editor) {
        editor.getSelected().clear();
    }

    @Override
    public void mouseReleased(CircuitEditor editor, MouseButton button, double x, double y) {
        if(selecting){
            // collect selected items on the rectangle
            Rectangle rect = getSelectionRect();
            for(GraphicComponent component : editor.getComponents()){
                if(component.inside(rect.getX(),rect.getY(),rect.getWidth(),rect.getHeight()))
                    editor.addSelected(component);
            }
            for(GraphicWire wire : editor.getWires()){
                if(wire.inside(rect.getX(),rect.getY(),rect.getWidth(),rect.getHeight()))
                    editor.addSelected(wire);
            }
        }
        dragging = false;
        selecting = false;
    }

    @Override
    public void mousePressed(CircuitEditor editor, MouseButton button, double x, double y) {
        GraphicElement ge = editor.findElementAt(x,y);

        if(ge != null){
            if(editor.getSelected().isEmpty()){
                editor.addSelected(ge);
            }
            dragStartX = x;
            dragStartY = y;
            dragging = true;
        }
        else{
            editor.clearSelection();
            selecting = true;
            selectionLeft = x;
            selectionTop = y;
            selectionWidth = 0;
            selectionHeight = 0;
        }
    }

    public boolean isSelecting() {
        return selecting;
    }

    public Rectangle getSelectionRect() {
        double left = Math.min(selectionLeft, selectionLeft + selectionWidth);
        double top = Math.min(selectionTop, selectionTop + selectionHeight);

        double width = Math.abs(selectionWidth);
        double height = Math.abs(selectionHeight);

        return new Rectangle(left, top, width, height);
    }
}
