package net.pimenta.alsim.gui;


import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import net.pimenta.alsim.gui.elements.GraphicComponent;
import net.pimenta.alsim.gui.elements.GraphicElement;


import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

public class CircuitCanvas extends Canvas {
    private CircuitContext context;

    private double panStartX;
    private double panStartY;
    private boolean panning = false;

    private boolean grid = true;

    private Consumer<GraphicElement> hoverListener;

    private ContextMenu contextMenu = new ContextMenu();

    public CircuitCanvas(double width, double height){
        super(width,height);
        setFocusTraversable(true);
        setOnMouseClicked(this::mouseClicked);
        setOnMouseMoved(this::mouseMoved);
        setOnMouseDragged(this::mouseDragged);
        setOnKeyPressed(this::KeyPressed);
        setOnMousePressed(this::mousePressed);
        setOnMouseReleased(this::mouseReleased);
        setOnScroll(this::onScroll);
    }

    private void onScroll(ScrollEvent event) {
        if(event.getDeltaY() == 0) return;

        double factor = Math.pow(getViewport().getBaseFactor(),event.getDeltaY()/40.0);

        getViewport().zoomAt(event.getX(),event.getY(),factor);

        draw();
    }

    private void mouseReleased(MouseEvent event) {
        double x = getViewport().toWorldX(event.getX());
        double y = getViewport().toWorldY(event.getY());
        getEditor().mouseReleased(event.getButton(),x,y);
        if(event.getButton() == MouseButton.MIDDLE){
            panning = false;
        }
        draw();
    }

    private void mousePressed(MouseEvent event) {
        double x = getViewport().toWorldX(event.getX());
        double y = getViewport().toWorldY(event.getY());

        if(event.getButton() == MouseButton.PRIMARY){
            getEditor().mousePressed(event.getButton(),x,y);
        } else if (event.getButton() == MouseButton.SECONDARY) {
            showContextMenu(event,x,y);
        } else if(event.getButton() == MouseButton.MIDDLE){
            panning = true;
            panStartX = event.getX();
            panStartY = event.getY();
        }
        draw();
    }

    private void mouseClicked(MouseEvent event){
        requestFocus();
        double x = getViewport().toWorldX(event.getX());
        double y = getViewport().toWorldY(event.getY());
        if(event.getButton() == MouseButton.PRIMARY){
            getEditor().mouseClicked(event.getButton(),event.getClickCount(),x,y);
        }
        draw();
    }
    private void mouseMoved(MouseEvent event){
        double x = getViewport().toWorldX(event.getX());
        double y = getViewport().toWorldY(event.getY());

        getEditor().mouseMoved(x,y);

        GraphicElement ge = getEditor().findElementAt(x,y);
        if(hoverListener != null) hoverListener.accept(ge);

        draw();
    }

    private void mouseDragged(MouseEvent event){
        double x = getViewport().toWorldX(event.getX());
        double y = getViewport().toWorldY(event.getY());
        getEditor().mouseDragged(x,y);
        if(event.getButton() == MouseButton.MIDDLE && panning){
            double dx = event.getX() - panStartX;
            double dy = event.getY() - panStartY;
            getViewport().pan(dx,dy);
            panStartX = event.getX();
            panStartY = event.getY();
        }
        draw();
    }

    private void KeyPressed(KeyEvent event) {
        if(event.isControlDown()) controlKeys(event);
        else normalKeys(event);

        draw();
    }
    private void controlKeys(KeyEvent event){
        switch (event.getCode()){
            //case S -> getEditor().saveCircuit();
            //case O -> getEditor().openCircuit();
            case Q -> Platform.exit();
            case G -> grid = !grid;
        }
    }
    private void normalKeys(KeyEvent event){
        switch (event.getCode()){
            case R -> getEditor().setResistorTool();
            case V -> getEditor().setVSourceTool();
            case W -> getEditor().setWireTool();
            case P -> getEditor().setProbeTool();
            case ESCAPE -> getEditor().setSelectTool();
            case DELETE -> getEditor().deleteSelected();
            case F -> getEditor().swapNodesOfSelected();
            case S -> getEditor().rotateSelectedCW();
            case D -> getEditor().rotateSelectedCCW();
            case F1 -> {
                getEditor().simulate();
                draw();
            }
            case F2 -> {
                try{
                    NetlistGenerator.generate(getEditor(), Path.of("netlist.ckt"));
                } catch (IOException e) {
                    System.out.println("Failed to generate netlist\n"+e.getMessage());
                }
            }
        }
    }

    public void draw(){
        GraphicsContext gc = getGraphicsContext2D();

        // clear screen
        gc.clearRect(0,0,getWidth(),getHeight());

        // save coordinate system and transform to world one
        gc.save();

        gc.translate(getViewport().getOffsetX(),getViewport().getOffsetY());
        gc.scale(getViewport().getZoom(),getViewport().getZoom());

        if(grid) drawGrid(gc);
        getEditor().draw(gc);

        // restore original coordinates
        gc.restore();
    }

    private void drawGrid(GraphicsContext gc) {
        double worldLeft  = getViewport().toWorldX(0);
        double worldTop   = getViewport().toWorldY(0);
        double worldRight = getViewport().toWorldX(getWidth());
        double worldBottom = getViewport().toWorldY(getHeight());

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(0.5 / getViewport().getZoom());

        for (double x = Math.floor(worldLeft / getEditor().GRID_SIZE) * getEditor().GRID_SIZE;
             x <= worldRight;
             x += getEditor().GRID_SIZE) {

            gc.strokeLine(x, worldTop, x, worldBottom);
        }

        for (double y = Math.floor(worldTop / getEditor().GRID_SIZE) * getEditor().GRID_SIZE;
             y <= worldBottom;
             y += getEditor().GRID_SIZE) {

            gc.strokeLine(worldLeft, y, worldRight, y);
        }
    }


    private void showContextMenu(MouseEvent event, double x, double y) {
        contextMenu.hide();
        contextMenu.getItems().clear();

        GraphicElement ge = getEditor().findElementAt(x,y);

        if(ge == null) contextMenu = showCanvasContextMenu();
        else{
            getEditor().clearSelection();
            getEditor().addSelected(ge);
            contextMenu = showElementContextMenu(ge);
        }

        contextMenu.show(this,event.getScreenX(),event.getScreenY());
    }

    private ContextMenu showElementContextMenu(GraphicElement ge) {
        ContextMenu menu = new ContextMenu();

        if(ge instanceof GraphicComponent){
            MenuItem properties = new MenuItem("Properties");
            MenuItem rotateCW   = new MenuItem("Rotate CW  (S)");
            MenuItem rotateCCW  = new MenuItem("Rotate CCW (D)");
            MenuItem permute    = new MenuItem("Permute (F)");

            properties.setOnAction(e -> getEditor().editProperties(ge));
            rotateCW.setOnAction(e -> getEditor().rotateSelectedCW());
            rotateCCW.setOnAction(e -> getEditor().rotateSelectedCCW());
            permute.setOnAction(e -> getEditor().swapNodesOfSelected());

            menu.getItems().addAll(
                    properties,
                    new SeparatorMenuItem(),
                    rotateCW,rotateCCW,permute
            );
        }

        MenuItem delete     = new MenuItem("Delete");
        delete.setOnAction(e -> getEditor().deleteSelected());

        menu.getItems().addAll(new SeparatorMenuItem(), delete);

        return menu;
    }

    private ContextMenu showCanvasContextMenu() {
        ContextMenu menu = new ContextMenu();

        MenuItem resistor = new MenuItem("Place resistor (R)");
        MenuItem vSource  = new MenuItem("Place VSource  (V)");
        MenuItem wire     = new MenuItem("Place wire (W)");
        MenuItem probe    = new MenuItem("Place Probe (P)");

        resistor.setOnAction(e -> getEditor().setResistorTool());
        vSource.setOnAction(e -> getEditor().setVSourceTool());
        wire.setOnAction(e -> getEditor().setWireTool());
        probe.setOnAction(e -> getEditor().setProbeTool());

        menu.getItems().addAll(resistor,vSource,new SeparatorMenuItem(),wire,new SeparatorMenuItem(),probe);

        return menu;
    }



    public void setHoverListener(Consumer<GraphicElement> listener){
        hoverListener = listener;
    }

    public Viewport getViewport() {
        return context.getViewport();
    }
    public CircuitEditor getEditor() {
        return context.getEditor();
    }
    public CircuitContext getContext(){
        return context;
    }
    public void setContext(CircuitContext context) {
        this.context = context;
        draw();
    }

    public void toggleGrid(){
        grid = !grid;
        draw();
    }
}
