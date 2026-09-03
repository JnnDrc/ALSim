package net.pimenta.alsim.gui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import net.pimenta.alsim.gui.elements.GraphicComponent;
import net.pimenta.alsim.gui.elements.GraphicElement;
import net.pimenta.alsim.gui.elements.GraphicNode;
import net.pimenta.alsim.gui.elements.GraphicWire;
import net.pimenta.alsim.gui.tools.*;
import net.pimenta.alsim.gui.ui.PropertiesDialog;
import net.pimenta.alsim.util.Pair;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CircuitEditor {
    public  final double GRID_SIZE   = 50;
    private final double SNAP_RADIUS = 25;

    private static final Tool resistorTool = new ResistorTool();
    private static final Tool vsourceTool  = new VSourceTool();
    private static final Tool wireTool     = new WireTool();
    private static final Tool selectTool   = new SelectTool();

    private final List<GraphicNode> nodes = new ArrayList<>();
    private final List<GraphicWire> wires = new ArrayList<>();
    private final List<GraphicComponent> components = new ArrayList<>();
    private GraphicElement preview;
    private final Set<GraphicElement> selected = new HashSet<>();
    private Tool tool = resistorTool;

    private int nextNodeId = 0;

    public double snap(double value){
        return Math.round(value/GRID_SIZE)*GRID_SIZE;
    }

    public void setResistorTool(){
        tool.cancel(this);
        tool = resistorTool;
    }
    public void setVSourceTool(){
        tool.cancel(this);
        tool = vsourceTool;
    }
    public void setWireTool(){
        tool.cancel(this);
        tool = wireTool;
    }
    public void setSelectTool() {
        tool.cancel(this);
        tool = selectTool;
        selected.clear();
    }

    public void mouseClicked(MouseButton button, int clickCount, double x, double y){
        tool.mouseClicked(this,button, clickCount, snap(x),snap(y));
    }

    public void mouseMoved(double x, double y){
        tool.mouseMoved(this,snap(x),snap(y));
    }
    public void mouseDragged(double x, double y) {
        tool.mouseDragged(this,snap(x), snap(y));
    }
    public void mouseReleased(MouseButton button, double x, double y) {
        tool.mouseReleased(this,button,snap(x),snap(y));
    }

    public void mousePressed(MouseButton button, double x, double y) {
        tool.mousePressed(this,button,x,y);
    }

    public void setPreview(GraphicElement ge){
        preview = ge;
    }

    public void clearPreview(){
        preview = null;
    }

    public GraphicNode findOrCreateNode(double x, double y){
        for (GraphicNode node : nodes){
            if(node.distTo(x,y) < SNAP_RADIUS){
                System.out.println("Node selected");
                return node;
            }
        }
        GraphicNode node = new GraphicNode(nextNodeId++,x,y);
        nodes.add(node);
        System.out.println("Node created");
        return node;
    }

    public void addComponent(GraphicComponent graphicComponent) {
        components.add(graphicComponent);
        for (GraphicNode node : graphicComponent.getNodes()) {
            if (!nodes.contains(node)) nodes.add(node);
        }
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.setLineWidth(2.0);
        for (GraphicWire wire : wires) {
            wire.draw(gc);
        }

        for (GraphicComponent component : components) {
            component.draw(gc);
        }

        for (GraphicNode node : nodes) {
            node.draw(gc);
        }

        gc.setStroke(Color.GRAY);
        if(preview != null) preview.draw(gc);

        gc.setStroke(Color.RED);
        if(!selected.isEmpty()) selected.forEach((element) -> element.draw(gc));

        // draw selection rectangle
        if(tool instanceof SelectTool hereSelectTool && hereSelectTool.isSelecting()){
            Rectangle rec = hereSelectTool.getSelectionRect();
            gc.setLineWidth(4.0);
            gc.setStroke(Color.LIGHTBLUE.deriveColor(0,1,1,0.75));
            gc.strokeRect(rec.getX(),rec.getY(),rec.getWidth(),rec.getHeight());
            gc.setFill(Color.LIGHTBLUE.deriveColor(0,1,1,0.25));
            gc.fillRect(rec.getX(),rec.getY(),rec.getWidth(),rec.getHeight());
        }
    }

    public void cancelTool() {
        tool.cancel(this);
    }

    public void addWire(GraphicWire wire) {
        wires.add(wire);
    }

    public List<GraphicComponent> getComponents(){
        return components;
    }
    public List<GraphicNode> getNodes(){
        return nodes;
    }

    public List<GraphicWire> getWires() {
        return wires;
    }

    public void clearSelection() {
        selected.clear();
    }

    public void addSelected(GraphicElement ge){
        selected.add(ge);
    }

    public Set<GraphicElement> getSelected(){
        return selected;
    }

    public boolean isSelected(GraphicElement ge){
        return selected.contains(ge);
    }

    public void deleteSelected(){
        for (GraphicElement element : selected){
            if (element instanceof GraphicComponent component){
                components.remove(component);

                for(GraphicNode node : component.getNodes())
                    if(!nodeIsUsed(node)) nodes.remove(node);
            }
            else if(element instanceof GraphicWire wire){
                wires.remove(wire);

                Pair<GraphicNode,GraphicNode> wireNodes = wire.getNodes();
                if(!nodeIsUsed(wireNodes.getFirst())) nodes.remove(wireNodes.getFirst());
                if(!nodeIsUsed(wireNodes.getSecond())) nodes.remove(wireNodes.getSecond());
            }
        }
        selected.clear();
    }

    private boolean nodeIsUsed(GraphicNode node){
        for(GraphicComponent component : components)
            if(component.hasNode(node)) return true;

        for(GraphicWire wire : wires)
            if(wire.hasNode(node)) return true;

        return false;
    }

    public GraphicElement findElementAt(double x, double y) {
        for(int i = components.size() - 1; i >= 0; i--){
            if(components.get(i).hit(x,y)) return components.get(i);
        }
        for(int i = wires.size() - 1; i >= 0; i--){
            if(wires.get(i).hit(x,y)) return wires.get(i);
        }
        return null;
    }

    public void editProperties(GraphicElement selected) {
        PropertiesDialog.show(selected);
    }
}
