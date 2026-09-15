package net.pimenta.alsim.gui.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import net.pimenta.alsim.gui.CircuitEditor;

public class CircuitPropertiesDialog {

    private CircuitPropertiesDialog() {}

    public static void show(CircuitEditor editor, Window owner) {
        Stage stage = new Stage();

        stage.setTitle("Circuit Properties");
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);

        TextField nameField = new TextField(editor.getCircuitName());

        TextArea descriptionArea = new TextArea(editor.getCircuitDesc());
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefRowCount(5);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(12));
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionArea, 1, 1);

        GridPane.setHgrow(nameField, Priority.ALWAYS);
        GridPane.setHgrow(descriptionArea, Priority.ALWAYS);

        Button cancel = new Button("Cancel");
        Button ok = new Button("OK");

        cancel.setOnAction(e -> stage.close());

        ok.setOnAction(e -> {
            editor.setCircuitName(nameField.getText());
            editor.setCircuitDesc(descriptionArea.getText());

            stage.close();
        });

        grid.add(cancel, 0, 2);
        grid.add(ok, 1, 2);

        stage.setScene(new Scene(grid));
        stage.showAndWait();
    }
}