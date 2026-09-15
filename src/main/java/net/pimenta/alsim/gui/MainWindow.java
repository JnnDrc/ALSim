package net.pimenta.alsim.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import net.pimenta.alsim.gui.elements.GraphicComponent;
import net.pimenta.alsim.gui.ui.CircuitPropertiesDialog;
import net.pimenta.alsim.gui.ui.PropertiesDialog;
import net.pimenta.alsim.gui.ui.SimulationInfoPanel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class MainWindow {
    private final Scene scene;
    TabPane tabs = new TabPane();
    private CheckMenuItem simulationLive; // gambiarra

    public MainWindow(){
        CircuitCanvas canvas = new CircuitCanvas(800,600);

        SimulationInfoPanel infoPanel = new SimulationInfoPanel();

        StackPane canvasArea = new StackPane(canvas,infoPanel);

        int blankTab = canvas.newCircuit();
        Tab tab = new Tab("Untitled");
        tab.setUserData(tab);
        tabs.getTabs().add(tab);
        tabs.getSelectionModel().select(tab);
        canvas.setActive(blankTab);
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        StackPane.setAlignment(infoPanel, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(infoPanel, new Insets(10));

        canvas.widthProperty().bind(canvasArea.widthProperty());
        canvas.heightProperty().bind(canvasArea.heightProperty());

        canvas.widthProperty()
                .addListener((obs,oldValue,newValue) -> canvas.draw());
        canvas.heightProperty()
                .addListener((obs,oldValue,newValue) -> canvas.draw());

        canvas.getEditor()
                .setPropertyEditor(element -> PropertiesDialog.show(element, getScene().getWindow()));

        MenuBar menuBar = createMenuBar(canvas);


        VBox center = new VBox(tabs,canvasArea);
        VBox.setVgrow(canvasArea, Priority.ALWAYS);
        tabs.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, oldTab, newTab) -> {
                            if(newTab == null) return;
                            int index = (int)newTab.getUserData();
                            canvas.setActive(index);
                            simulationLive.setSelected(canvas.getEditor().getSimulation().isLive());
                        });

        canvas.setHoverListener(element -> {
            if(element instanceof GraphicComponent component && component.getResult() != null){
                infoPanel.showComponent(component);
            }
            else{
                infoPanel.hideInfo();
            }
        });


        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(center);
        scene = new Scene(root);
    }

    public Scene getScene(){
        return scene;
    }

    private void newCircuit(CircuitCanvas canvas) {
        int index = canvas.newCircuit();

        Tab tab = new Tab("Untitled");
        tab.setUserData(index);

        tabs.getTabs().add(tab);
        tabs.getSelectionModel().select(tab);
        canvas.setActive(index);
    }

    private void closeCircuit(CircuitCanvas canvas) {
        Tab tab = tabs.getSelectionModel().getSelectedItem();

        if (tab == null) return;

        int index = (int)tab.getUserData();

        canvas.closeCircuit(index);
        tabs.getTabs().remove(tab);

        for (int i = 0; i < tabs.getTabs().size(); i++) {
            tabs.getTabs().get(i).setUserData(i);
        }

        if (tabs.getTabs().isEmpty()) {
            newCircuit(canvas);
        } else {
            int newIndex = Math.min(
                    index,
                    tabs.getTabs().size() - 1
            );

            tabs.getSelectionModel().select(newIndex);
        }
    }

    private void saveCircuit(CircuitCanvas canvas){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save circuit");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Alsim circuit (*.alsim)", "*.alsim")
        );
        chooser.setInitialFileName("circuit.alsim");

        Path path = canvas.getEditor().getCircuitPath();

        if(path == null){
            File saveFile = chooser.showSaveDialog(scene.getWindow());
            if(saveFile == null) return;
            path = saveFile.toPath();
            canvas.getEditor().setCircuitPath(path);
        }

        try {
            if(canvas.getEditor().getCircuitName() == null) canvas.getEditor().setCircuitName(path.getFileName().toString());
            CircuitLoader.saveCircuit(canvas.getEditor(),canvas.getViewport(), path);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    private void openCircuit(CircuitCanvas canvas){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open circuit");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Alsim circuit (*.alsim)", "*.alsim")
        );
        chooser.setInitialFileName("circuit.alsim");

        File openFile = chooser.showOpenDialog(scene.getWindow());
        if(openFile == null) return;

        int index = canvas.newCircuit();
        canvas.setActive(index);

        try {
            CircuitLoader.openCircuit(canvas.getEditor(),canvas.getViewport(),openFile.toPath());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        canvas.getEditor().setCircuitPath(openFile.toPath());

        String circuitName = canvas.getEditor().getCircuitName();
        if(circuitName.equals("noname")) circuitName = "Untitled";
        Tab tab = new Tab(circuitName);
        tab.setUserData(index);
        tabs.getTabs().add(tab);
        tabs.getSelectionModel().select(tab);
    }

    private MenuBar createMenuBar(CircuitCanvas canvas){
        MenuBar menuBar = new MenuBar();

        Menu file = new Menu("File");
        MenuItem fileNew = new MenuItem("New(Ctrl-N)");
        fileNew.setOnAction(e -> newCircuit(canvas));
        MenuItem fileSave = new MenuItem("Save(Ctrl-S)");
        fileSave.setOnAction(e -> saveCircuit(canvas));
        MenuItem fileOpen = new MenuItem("Open(Ctrl-O)");
        fileOpen.setOnAction(e -> openCircuit(canvas));
        MenuItem fileClose = new MenuItem("Close(Ctrl-C)");
        fileClose.setOnAction(e -> closeCircuit(canvas));
        MenuItem fileExit = new MenuItem("Exit(Ctrl-Q)");
        fileExit.setOnAction(e -> Platform.exit());
        MenuItem fileProperties = new MenuItem("Properties");
        fileProperties.setOnAction(e -> {
            CircuitPropertiesDialog.show(canvas.getEditor(),scene.getWindow());
            String name = canvas.getEditor().getCircuitName();

            Tab tab = tabs.getSelectionModel().getSelectedItem();
            tab.setText(name.isBlank() ? "Untitled" : name);
        });
        file.getItems().addAll(fileNew,fileSave,fileOpen,fileClose,new SeparatorMenuItem(),fileProperties,fileExit);

        Menu edit = new Menu("Edit");
        MenuItem editUndo = new MenuItem("Undo(Ctrl-Z)");
        MenuItem editRedo = new MenuItem("Redo(Ctrl-Y)");
        MenuItem editClear = new MenuItem("Clear");
        editClear.setOnAction(e -> canvas.getEditor().clear());
        edit.getItems().addAll(editUndo,editRedo,new SeparatorMenuItem(),editClear);

        Menu draw = new Menu("Draw");
        MenuItem drawWire = new MenuItem("Wire(W)");
        drawWire.setOnAction(e -> canvas.getEditor().setWireTool());
        MenuItem drawProbe = new MenuItem("Probe(P)");
        drawProbe.setOnAction(e -> canvas.getEditor().setProbeTool());
        MenuItem drawResistor = new MenuItem("Resistor(R)");
        drawResistor.setOnAction(e -> canvas.getEditor().setResistorTool());
        MenuItem drawVSource = new MenuItem("VSource(V)");
        drawVSource.setOnAction(e -> canvas.getEditor().setVSourceTool());
        draw.getItems().addAll(drawWire,drawProbe,drawResistor,drawVSource);

        Menu options = new Menu("Options");
        MenuItem optionsGrid = new MenuItem("Grid(Ctrl-G)");
        optionsGrid.setOnAction(e -> canvas.toggleGrid());
        options.getItems().addAll(optionsGrid);

        Menu simulation = new Menu("Simulation");
        MenuItem simulationSimulate = new MenuItem("Simulate (F1)");
        simulationSimulate.setOnAction(e -> canvas.getEditor().simulate());
        simulationLive = new CheckMenuItem("Live refresh");
        simulationLive.setOnAction(e -> {
            boolean enabled = simulationLive.isSelected();
            canvas.getEditor().setLive(simulationLive.isSelected());
            if(enabled) canvas.getEditor().simulate();
        });
        simulation.getItems().addAll(simulationSimulate,simulationLive);

        menuBar.getMenus().addAll(file,edit,draw,simulation,options);
        return menuBar  ;
    }

}
