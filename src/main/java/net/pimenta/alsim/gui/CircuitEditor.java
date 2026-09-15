package net.pimenta.alsim.gui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import net.pimenta.alsim.gui.elements.*;
import net.pimenta.alsim.gui.simulate.SimulationState;
import net.pimenta.alsim.gui.tools.*;
import net.pimenta.alsim.util.Pair;

import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class CircuitEditor {
    public  final double GRID_SIZE   = 50;
    private final double SNAP_RADIUS = GRID_SIZE/2;

    private static final Tool resistorTool = new ResistorTool();
    private static final Tool vsourceTool  = new VSourceTool();
    private static final Tool wireTool     = new WireTool();
    private static final Tool selectTool   = new SelectTool();
    private static final Tool probeTool    = new ProbeTool();

    private final List<GraphicNode>      nodes      = new ArrayList<>();
    private final List<GraphicWire>      wires      = new ArrayList<>();
    private final List<GraphicComponent> components = new ArrayList<>();
    private final List<GraphicProbe>     probes     = new ArrayList<>();
    private GraphicElement preview;
    private final List<GraphicElement> selected = new ArrayList<>();
    private Tool tool = resistorTool;

    private final SimulationState simulation = new SimulationState();

    private String circuitName = null;
    private String circuitDesc = null;
    private Path circuitPath;


    private Consumer<GraphicElement> propertyEditor;

    private int nextNodeId = 0;
    private int nextWireId = 0;

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
    }
    public void setProbeTool(){
        tool.cancel(this);
        tool = probeTool;
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
                return node;
            }
        }
        return new GraphicNode(nextNodeId++,x,y);
    }

    public void addNode(GraphicNode node){
        if(!nodes.contains(node)) nodes.add(node);
        nextNodeId = Math.max(nextNodeId,node.getNode() + 1);
    }

    private void invalidate() {
        simulation.invalidate();
        if(simulation.isLive()) simulation.simulate(this);
    }

    public void addWire(GraphicWire wire) {
        wires.add(wire);
        Pair<GraphicNode,GraphicNode> wireNodes = wire.getNodes();
        addNode(wireNodes.getFirst());
        addNode(wireNodes.getSecond());
        nextWireId = Math.max(nextWireId,wire.getId()+1);

        invalidate();
    }

    public void addComponent(GraphicComponent component) {
        components.add(component);
        for (GraphicNode node : component.getNodes()) {
            if (!nodes.contains(node)) nodes.add(node);
        }

        invalidate();
    }

    public void addProbe(GraphicProbe probe){
        probes.add(probe);
        if(!nodes.contains(probe.getNode())) nodes.add(probe.getNode());

        invalidate();
    }


    public List<GraphicNode> getNodes(){
        return nodes;
    }
    public GraphicNode getNode(int id){
        for (GraphicNode node : nodes) if(node.getNode() == id) return node;
        return null;
    }
    public List<GraphicWire> getWires() {
        return wires;
    }

    public List<GraphicComponent> getComponents(){
        return components;
    }

    public List<GraphicProbe> getProbes() {
        return probes;
    }

    public List<GraphicElement> getElements(){
        List<GraphicElement> elements = new ArrayList<>(nodes);
        elements.addAll(wires);
        elements.addAll(components);
        elements.addAll(probes);
        return elements;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.setLineWidth(2.0);

        List<GraphicElement> elements = getElements().reversed();

        for(GraphicElement element : elements){
            element.draw(gc,GraphicMode.NORMAL);
        }

        gc.setStroke(Color.GRAY);
        if(preview != null) preview.draw(gc,GraphicMode.PREVIEW);

        gc.setStroke(Color.RED);
        if(!selected.isEmpty()) selected.forEach((element) -> element.draw(gc,GraphicMode.SELECTED));

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

    public void clearSelection() {
        selected.clear();
    }

    public void addSelected(GraphicElement ge){
        selected.add(ge);
    }

    public List<GraphicElement> getSelected(){
        return selected;
    }

    public boolean isSelected(GraphicElement ge){
        return selected.contains(ge);
    }

    public void deleteSelected(){
        List<GraphicNode> affectedNodes = new ArrayList<>();
        for (GraphicElement element : selected){
            if (element instanceof GraphicComponent component){
                components.remove(component);

                affectedNodes.addAll(component.getNodes());
            }
            else if(element instanceof GraphicWire wire){
                wires.remove(wire);

                Pair<GraphicNode,GraphicNode> wireNodes = wire.getNodes();
                affectedNodes.add(wireNodes.getFirst());
                affectedNodes.add(wireNodes.getSecond());
            }
            else if(element instanceof GraphicProbe probe){
                probes.remove(probe);

                affectedNodes.add(probe.getNode());
            }
        }
        for(GraphicNode node : affectedNodes){
            if(!nodeIsUsed(node)) nodes.remove(node);
        }
        selected.clear();


        invalidate();
    }

    private boolean nodeIsUsed(GraphicNode node){
        for(GraphicComponent component : components)
            if(component.hasNode(node)) return true;

        for(GraphicWire wire : wires)
            if(wire.hasNode(node)) return true;

        for(GraphicProbe probe : probes)
            if(probe.getNode() == node) return true;

        return false;
    }

    public GraphicElement findElementAt(double x, double y) {
        for(int i = components.size() - 1; i >= 0; i--){
            if(components.get(i).hit(x,y)) return components.get(i);
        }
        for(int i = wires.size() - 1; i >= 0; i--){
            if(wires.get(i).hit(x,y)) return wires.get(i);
        }
        for(int i = probes.size() - 1; i >= 0; i--){
            if(probes.get(i).hit(x,y)) return probes.get(i);
        }
        return null;
    }

    public void setPropertyEditor(Consumer<GraphicElement> cge){
        propertyEditor = cge;
    }
    public void editProperties(GraphicElement selected) {
        if(propertyEditor != null){
            propertyEditor.accept(selected);
            invalidate();
        }
    }

    public void simulate(){
        simulation.simulate(this);
    }
    public SimulationState getSimulation() {
        return simulation;
    }

    public void swapNodesOfSelected() {
        if(selected.size() != 1) return;
        if(selected.getFirst() instanceof GraphicComponent component) {
            component.swapNodes(0,1);
            invalidate();
        }
    }

    public void rotateSelectedCW() {
        if(selected.size() != 1) return;
        if(selected.getFirst() instanceof GraphicComponent component) {
            component.rotateCW();
            invalidate();
        }
    }
    public void rotateSelectedCCW() {
        if(selected.size() != 1) return;
        if(selected.getFirst() instanceof GraphicComponent component) {
            component.rotateCCW();
            invalidate();
        }
    }

    public String getCircuitName() {
        return circuitName;
    }

    public void setCircuitName(String circuitName) {
        this.circuitName = circuitName;
    }
    public String getCircuitDesc(){
        return this.circuitDesc;
    }
    public void setCircuitDesc(String circuitDesc) {
        this.circuitDesc = circuitDesc;
    }

    public Path getCircuitPath() {
        return circuitPath;
    }

    public void setCircuitPath(Path path) {
        this.circuitPath = path;
    }

    public int nextWire() {
        return nextWireId;
    }

    public void clear() {
        clearPreview();
        clearSelection();
        nodes.clear();
        wires.clear();
        components.clear();
        probes.clear();
        nextWireId = 0;
        nextNodeId = 0;
        circuitName = null;
        circuitDesc = null;
    }

    public void setLive(boolean live) {
        simulation.setLive(live);
    }
}
