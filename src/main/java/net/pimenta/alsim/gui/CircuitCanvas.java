package net.pimenta.alsim.gui;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;


import java.io.IOException;
import java.nio.file.Path;

public class CircuitCanvas extends Canvas {
    CircuitEditor editor;

    public CircuitCanvas(double width, double height){
        super(width,height);
        editor = new CircuitEditor();
        setFocusTraversable(true);
        setOnMouseClicked(this::mouseClicked);
        setOnMouseMoved(this::mouseMoved);
        setOnMouseDragged(this::mouseDragged);
        setOnKeyPressed(this::KeyPressed);
        setOnMousePressed(this::mousePressed);
        setOnMouseReleased(this::mouseReleased);
    }

    private void mouseReleased(MouseEvent event) {
        editor.mouseReleased(event.getButton(),event.getX(),event.getY());
        draw();
    }

    private void mousePressed(MouseEvent event) {
        editor.mousePressed(event.getButton(),event.getX(),event.getY());
        draw();
    }

    private void mouseClicked(MouseEvent event){
        requestFocus();
        editor.mouseClicked(event.getButton(),event.getClickCount(),event.getX(),event.getY());
        draw();
    }
    private void mouseMoved(MouseEvent event){
        editor.mouseMoved(event.getX(),event.getY());
        draw();
    }

    private void mouseDragged(MouseEvent event){
        editor.mouseDragged(event.getX(),event.getY());
        draw();
    }

    private void KeyPressed(KeyEvent event) {
        switch (event.getCode()){
            case R -> editor.setResistorTool();
            case V -> editor.setVSourceTool();
            case W -> editor.setWireTool();
            case ESCAPE -> editor.setSelectTool();
            case DELETE -> editor.deleteSelected();
            case F1 -> {
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

        gc.setFill(Color.WHITE);
        gc.setLineWidth(.5);

        gc.clearRect(0,0,getWidth(),getHeight());

        // draw grid

        gc.setStroke(Color.BLACK);
        double w = getWidth();
        double h = getHeight();
        for(double x = 0; x < w; x+= editor.GRID_SIZE){
          gc.strokeLine(x,0,x,h);
        }
        for(double y = 0; y < h; y+= editor.GRID_SIZE){
            gc.strokeLine(0,y,w,y);

        }

        // draw editor elements
        editor.draw(gc);
    }
}
