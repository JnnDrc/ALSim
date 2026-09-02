package net.pimenta.alsim;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import net.pimenta.alsim.gui.CircuitCanvas;
import net.pimenta.alsim.gui.MainWindow;

public class Main extends Application{
    @Override
    public void start(Stage stage){
        MainWindow window = new MainWindow();
        Scene scene = window.getScene();
        stage.setScene(scene);
        stage.setTitle("AL.Sim");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}