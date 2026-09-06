package net.pimenta.alsim.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import net.pimenta.alsim.gui.elements.GraphicComponent;
import net.pimenta.alsim.gui.simulate.ComponentResult;
import net.pimenta.alsim.gui.ui.SimulationInfoPanel;

public class MainWindow {
    private final BorderPane root;
    private final Scene scene;

    public MainWindow(){
        CircuitCanvas canvas = new CircuitCanvas(800,600);
        SimulationInfoPanel infoPanel = new SimulationInfoPanel();

        StackPane canvasArea = new StackPane(canvas,infoPanel);

        StackPane.setAlignment(infoPanel, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(infoPanel, new Insets(10));

        MenuBar menuBar = createMenuBar(canvas);

        canvas.setHoverListener(element -> {
            if(element instanceof GraphicComponent component){
                if(component.getResult() != null){
                    infoPanel.showComponent(component);
                }else{
                    infoPanel.hideInfo();
                }
            }
            else{
                infoPanel.hideInfo();
            }
        });


        root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(canvasArea);
        scene = new Scene(root);
    }

    public Scene getScene(){
        return scene;
    }

    private MenuBar createMenuBar(CircuitCanvas canvas){
        MenuBar menuBar = new MenuBar();

        Menu file = new Menu("File");
        MenuItem fileSave = new MenuItem("Save");
        MenuItem fileOpen = new MenuItem("Open");
        file.getItems().addAll(fileSave,fileOpen);

        Menu edit = new Menu("Edit");
        MenuItem editUndo = new MenuItem("Undo");
        MenuItem editRedo = new MenuItem("Redo");
        edit.getItems().addAll(editUndo,editRedo);

        Menu draw = new Menu("Draw");
        MenuItem drawWire = new MenuItem("Wire(W)");
        drawWire.setOnAction(e -> canvas.getEditor().setWireTool());
        MenuItem drawResistor = new MenuItem("Resistor(R)");
        drawResistor.setOnAction(e -> canvas.getEditor().setResistorTool());
        MenuItem drawVSource = new MenuItem("VSource(V)");
        drawVSource.setOnAction(e -> canvas.getEditor().setVSourceTool());
        draw.getItems().addAll(drawWire,drawResistor,drawVSource);

        Menu options = new Menu("Options");
        MenuItem optionsGrid = new MenuItem("Grid");
        optionsGrid.setOnAction(e -> canvas.setGrid(!canvas.getGrid()));
        options.getItems().addAll(optionsGrid);

        Menu simulation = new Menu("Simulation");
        MenuItem simulationSimulate = new MenuItem("Simulate (F1)");
        simulationSimulate.setOnAction(e -> canvas.getEditor().simulate());
        simulation.getItems().addAll(simulationSimulate);

        menuBar.getMenus().addAll(file,edit,draw,simulation,options);
        return menuBar  ;
    }

}
