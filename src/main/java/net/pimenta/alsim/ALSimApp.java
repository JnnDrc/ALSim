package net.pimenta.alsim;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.pimenta.alsim.gui.MainWindow;

public class ALSimApp extends Application {
    @Override
    public void start(Stage stage){
        MainWindow window = new MainWindow();
        Scene scene = window.getScene();
        stage.setScene(scene);
        stage.setTitle("AL.Sim");
        stage.show();
    }
}
