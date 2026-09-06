package net.pimenta.alsim.gui;


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
    private final CircuitEditor editor = new CircuitEditor();
    private final Viewport viewport = new Viewport();
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
        setOnScroll(this::onScrool);
    }

    private void onScrool(ScrollEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();

        double worldX = viewport.toWorldX(mouseX);
        double worldY = viewport.toWorldY(mouseY);


        if(event.getDeltaY() > 0) viewport.zoomIn();
        else                      viewport.zoomOut();

        viewport.centerOnWorldPoint(worldX,worldY,mouseX,mouseY);
        draw();
    }

    private void mouseReleased(MouseEvent event) {
        double x = viewport.toWorldX(event.getX());
        double y = viewport.toWorldY(event.getY());
        editor.mouseReleased(event.getButton(),x,y);
        if(event.getButton() == MouseButton.MIDDLE){
            panning = false;
        }
        draw();
    }

    private void mousePressed(MouseEvent event) {
        double x = viewport.toWorldX(event.getX());
        double y = viewport.toWorldY(event.getY());

        if(event.getButton() == MouseButton.PRIMARY){
            editor.mousePressed(event.getButton(),x,y);
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
        double x = viewport.toWorldX(event.getX());
        double y = viewport.toWorldY(event.getY());
        editor.mouseClicked(event.getButton(),event.getClickCount(),x,y);
        draw();
    }
    private void mouseMoved(MouseEvent event){
        double x = viewport.toWorldX(event.getX());
        double y = viewport.toWorldY(event.getY());

        editor.mouseMoved(x,y);

        GraphicElement ge = editor.findElementAt(x,y);
        if(hoverListener != null) hoverListener.accept(ge);

        draw();
    }

    private void mouseDragged(MouseEvent event){
        double x = viewport.toWorldX(event.getX());
        double y = viewport.toWorldY(event.getY());
        editor.mouseDragged(x,y);
        if(event.getButton() == MouseButton.MIDDLE && panning){
            double dx = event.getX() - panStartX;
            double dy = event.getY() - panStartY;
            viewport.pan(dx,dy);
            panStartX = event.getX();
            panStartY = event.getY();
        }
        draw();
    }

    private void KeyPressed(KeyEvent event) {
        switch (event.getCode()){
            case R -> editor.setResistorTool();
            case V -> editor.setVSourceTool();
            case W -> editor.setWireTool();
            case ESCAPE -> editor.setSelectTool();
            case DELETE -> editor.deleteSelected();
            case P -> editor.swapNodesOfSelected();
            case S -> editor.rotateSelectedCW();
            case D -> editor.rotateSelectedCCW();
            case F1 -> editor.simulate();
            case F2 -> {
                try{
                    NetlistGenerator.generate(editor, Path.of("netlist.ckt"));
                } catch (IOException e) {
                    System.out.println("Failed to generate netlist\n"+e.getMessage());
                }
            }
        }
        draw();
    }

    public void draw(){
        GraphicsContext gc = getGraphicsContext2D();

        // clear screen
        gc.clearRect(0,0,getWidth(),getHeight());

        // save coordinate system and transform to world one
        gc.save();

        gc.translate(viewport.getOffsetX(),viewport.getOffsetY());
        gc.scale(viewport.getZoom(),viewport.getZoom());

        if(grid) drawGrid(gc);
        editor.draw(gc);

        // restore original coordinates
        gc.restore();
    }

    private void drawGrid(GraphicsContext gc) {
        double worldLeft  = viewport.toWorldX(0);
        double worldTop   = viewport.toWorldY(0);
        double worldRight = viewport.toWorldX(getWidth());
        double worldBottom = viewport.toWorldY(getHeight());

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(0.5 / viewport.getZoom());

        for (double x = Math.floor(worldLeft / editor.GRID_SIZE) * editor.GRID_SIZE;
             x <= worldRight;
             x += editor.GRID_SIZE) {

            gc.strokeLine(x, worldTop, x, worldBottom);
        }

        for (double y = Math.floor(worldTop / editor.GRID_SIZE) * editor.GRID_SIZE;
             y <= worldBottom;
             y += editor.GRID_SIZE) {

            gc.strokeLine(worldLeft, y, worldRight, y);
        }
    }


    private void showContextMenu(MouseEvent event, double x, double y) {
        contextMenu.hide();
        contextMenu.getItems().clear();

        GraphicElement ge = editor.findElementAt(x,y);

        if(ge == null) contextMenu = showCanvasContextMenu();
        else{
            editor.clearSelection();
            editor.addSelected(ge);
            contextMenu = showElementContextMenu(ge);
        }

        contextMenu.show(this,event.getScreenX(),event.getScreenY());
    }

    private ContextMenu showElementContextMenu(GraphicElement ge) {
        ContextMenu menu = new ContextMenu();

        if(ge instanceof GraphicComponent){
            MenuItem properties = new MenuItem("Properties (Double click)");
            MenuItem rotateCW   = new MenuItem("Rotate CW  (S)");
            MenuItem rotateCCW  = new MenuItem("Rotate CCW (D)");
            MenuItem permute    = new MenuItem("Permute (P)");

            properties.setOnAction(e -> editor.editProperties(ge));
            rotateCW.setOnAction(e -> editor.rotateSelectedCW());
            rotateCCW.setOnAction(e -> editor.rotateSelectedCCW());
            permute.setOnAction(e -> editor.swapNodesOfSelected());

            menu.getItems().addAll(
                    properties,
                    new SeparatorMenuItem(),
                    rotateCW,rotateCCW,permute
            );
        }

        MenuItem delete     = new MenuItem("Delete");
        delete.setOnAction(e -> editor.deleteSelected());

        menu.getItems().addAll(new SeparatorMenuItem(), delete);

        return menu;
    }

    private ContextMenu showCanvasContextMenu() {
        ContextMenu menu = new ContextMenu();

        MenuItem resistor = new MenuItem("Place resistor (R)");
        MenuItem vSource  = new MenuItem("Place VSource  (V)");
        MenuItem wire     = new MenuItem("Place wire (W)");

        resistor.setOnAction(e -> editor.setResistorTool());
        vSource.setOnAction(e -> editor.setVSourceTool());
        wire.setOnAction(e -> editor.setWireTool());

        menu.getItems().addAll(resistor,vSource,wire);

        return menu;
    }

    public void setHoverListener(Consumer<GraphicElement> listener){
        hoverListener = listener;
    }

    public CircuitEditor getEditor() {
        return editor;
    }

    public void setGrid(boolean grid) {
        this.grid = grid;
    }
    public boolean getGrid(){
        return grid;
    }
}
