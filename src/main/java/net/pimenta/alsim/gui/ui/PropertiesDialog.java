package net.pimenta.alsim.gui.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import net.pimenta.alsim.gui.elements.*;
import net.pimenta.alsim.util.Engineering;

public class PropertiesDialog {

    public static void show(GraphicElement ge, Window owner){
        switch (ge){
            case GraphicResistor resistor -> showResistor(resistor, owner);
            case GraphicVSource  vsource  -> showVSource(vsource, owner);
            case GraphicNode     node     -> showNode(node, owner);
            case GraphicProbe    probe    -> showProbe(probe,owner);
            default -> throw new IllegalStateException("Unexpected value: " + ge);
        }
    }

    private static void showNode(GraphicNode node, Window owner) {
        Stage stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);

        stage.setTitle("Properties");
        stage.initModality(Modality.APPLICATION_MODAL);

        Label idLabel = new Label("Name:");
        TextField idField = new TextField(node.getLabel());
        HBox idRow = new HBox(10,idLabel,idField);

        Button cancelButton = new Button("Cancel");
        Button applyButton  = new Button("Apply");

        cancelButton.setOnAction(event -> stage.close());
        applyButton.setOnAction(event -> {
            String id    = idField.getText();
            node.setLabel(id);
            stage.close();
        });

        HBox buttons = new HBox(10,cancelButton,applyButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        VBox root = new VBox(15, idRow, buttons);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private static void showResistor(GraphicResistor resistor, Window owner){
        Stage stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);

        stage.setTitle("Properties");
        stage.initModality(Modality.APPLICATION_MODAL);

        Label resistanceLabel = new Label("Resistance:");
        TextField resistanceField = new TextField(Double.toString(resistor.getR()));
        Label ohmLabel = new Label("Ω");
        HBox resistanceRow = new HBox(10, resistanceLabel,resistanceField,ohmLabel);

        resistanceRow.setAlignment(Pos.CENTER_LEFT);

        Label idLabel = new Label("Name:");
        TextField idField = new TextField(resistor.getId());
        HBox idRow = new HBox(10,idLabel,idField);

        idRow.setAlignment(Pos.CENTER_LEFT);

        Button cancelButton = new Button("Cancel");
        Button applyButton  = new Button("Apply");

        cancelButton.setOnAction(event -> stage.close());
        applyButton.setOnAction(event -> {
            try{
                double value = Engineering.parse(resistanceField.getText());
                String id    = idField.getText();
                resistor.setR(value);
                resistor.setId(id);
                stage.close();
            }catch (NumberFormatException e){
                resistanceField.setStyle("-fx-border-color: red;");
            }
        });

        HBox buttons = new HBox(10,cancelButton,applyButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        VBox root = new VBox(15, resistanceRow,idRow, buttons);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private static void showVSource(GraphicVSource vsource, Window owner){
        Stage stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);

        stage.setTitle("Properties");
        stage.initModality(Modality.APPLICATION_MODAL);

        Label voltageLabel = new Label("Voltage:");
        TextField voltageField = new TextField(Double.toString(vsource.getV()));
        Label vLabel = new Label("V");
        HBox voltageRow = new HBox(10, voltageLabel,voltageField,vLabel);

        voltageRow.setAlignment(Pos.CENTER_LEFT);

        Label idLabel = new Label("Name:");
        TextField idField = new TextField(vsource.getId());
        HBox idRow = new HBox(10,idLabel,idField);

        idRow.setAlignment(Pos.CENTER_LEFT);

        Button cancelButton = new Button("Cancel");
        Button applyButton  = new Button("Apply");

        cancelButton.setOnAction(event -> stage.close());
        applyButton.setOnAction(event -> {
            try{
                double value = Engineering.parse(voltageField.getText());
                vsource.setV(value);
                stage.close();
            }catch (NumberFormatException e){
                voltageField.setStyle("-fx-border-color: red;");
            }
        });

        HBox buttons = new HBox(10,cancelButton,applyButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        VBox root = new VBox(15,voltageRow,idRow,buttons);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private static void showProbe(GraphicProbe probe, Window owner) {
        Stage stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);

        stage.setTitle("Properties");
        stage.initModality(Modality.APPLICATION_MODAL);

        Label idLabel = new Label("Name:");
        TextField idField = new TextField(probe.getLabel());
        HBox idRow = new HBox(10,idLabel,idField);

//        Label modeLabel = new Label("Mode ");
//        RadioButton voltageButton = new RadioButton("Voltage");
//        RadioButton currentButton = new RadioButton("Current");
//        ToggleGroup modeGroup = new ToggleGroup();
//
//        voltageButton.setToggleGroup(modeGroup);
//        currentButton.setToggleGroup(modeGroup);

//        if(probe.getMode() == ProbeMode.VOLTAGE)
//            voltageButton.setSelected(true);
//        else
//            currentButton.setSelected(true);

//        HBox modeRow = new HBox(10,modeLabel,voltageButton,currentButton);

        Button cancelButton = new Button("Cancel");
        Button applyButton  = new Button("Apply");

        cancelButton.setOnAction(event -> stage.close());
        applyButton.setOnAction(event -> {
            String id    = idField.getText();
            probe.setLabel(id);
            //probe.setMode(voltageButton.isSelected() ? ProbeMode.VOLTAGE : ProbeMode.CURRENT);
            stage.close();
        });

        HBox buttons = new HBox(10,cancelButton,applyButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        VBox root = new VBox(15, idRow, buttons);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }

}
