package net.pimenta.alsim;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{
    @Override
    public void start(Stage stage){
        stage.setTitle("AL.Sim");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}