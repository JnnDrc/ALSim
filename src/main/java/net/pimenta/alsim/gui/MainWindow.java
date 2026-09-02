package net.pimenta.alsim.gui;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class MainWindow {
    private final BorderPane root;
    private final Scene scene;

    public MainWindow(){
        CircuitCanvas canvas = new CircuitCanvas(800,600);
        root = new BorderPane(canvas);
        scene = new Scene(root);
    }

    public Scene getScene(){
        return scene;
    }
}
