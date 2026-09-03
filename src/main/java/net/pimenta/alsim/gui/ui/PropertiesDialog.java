package net.pimenta.alsim.gui.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.pimenta.alsim.gui.elements.GraphicElement;
import net.pimenta.alsim.gui.elements.GraphicResistor;
import net.pimenta.alsim.gui.elements.GraphicVSource;

public class PropertiesDialog {

    public static void show(GraphicElement ge){
        switch (ge){
            case GraphicResistor resistor -> showResistor(resistor);
            case GraphicVSource  vsource  -> showVSource(vsource);
            default -> throw new IllegalStateException("Unexpected value: " + ge);
        }
    }

    private static void showResistor(GraphicResistor resistor){
        Stage stage = new Stage();

        stage.setTitle("Properties");
        stage.initModality(Modality.APPLICATION_MODAL);

        Label resistanceLabel = new Label("Resistance:");
        TextField resistanceField = new TextField(Double.toString(resistor.getR()));
        Label ohmLabel = new Label("Ω");
        HBox resitanceRow = new HBox(10, resistanceLabel,resistanceField,ohmLabel);

        resitanceRow.setAlignment(Pos.CENTER_LEFT);

        Button cancelButton = new Button("Cancel");
        Button applyButton  = new Button("Apply");

        cancelButton.setOnAction(event -> stage.close());
        applyButton.setOnAction(event -> {
            try{
                double value = Double.parseDouble(resistanceField.getText());
                resistor.setR(value);
                stage.close();
            }catch (NumberFormatException e){
                resistanceField.setStyle("-fx-border-color: red;");
            }
        });

        HBox buttons = new HBox(10,cancelButton,applyButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        VBox root = new VBox(15,resitanceRow,buttons);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private static void showVSource(GraphicVSource vsource){
        Stage stage = new Stage();

        stage.setTitle("Properties");
        stage.initModality(Modality.APPLICATION_MODAL);

        Label voltageLabel = new Label("Voltage:");
        TextField voltageField = new TextField(Double.toString(vsource.getV()));
        Label vLabel = new Label("V");
        HBox voltageRow = new HBox(10, voltageLabel,voltageField,vLabel);

        voltageRow.setAlignment(Pos.CENTER_LEFT);

        Button cancelButton = new Button("Cancel");
        Button applyButton  = new Button("Apply");

        cancelButton.setOnAction(event -> stage.close());
        applyButton.setOnAction(event -> {
            try{
                double value = Double.parseDouble(voltageField.getText());
                vsource.setV(value);
                stage.close();
            }catch (NumberFormatException e){
                voltageField.setStyle("-fx-border-color: red;");
            }
        });

        HBox buttons = new HBox(10,cancelButton,applyButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        VBox root = new VBox(15,voltageRow,buttons);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
