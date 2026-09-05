package net.pimenta.alsim.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import net.pimenta.alsim.gui.elements.GraphicComponent;
import net.pimenta.alsim.gui.elements.GraphicNode;
import net.pimenta.alsim.gui.simulate.ComponentResult;
import net.pimenta.alsim.gui.simulate.NodeResult;
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

        canvas.setHoverListener(element -> {
            if(element instanceof GraphicComponent component){
                ComponentResult result = canvas.getEditor().getSimulation().getComponentResult(component.getId());
                if(result != null){
                    infoPanel.showComponent(component,result);
                }else{
                    infoPanel.hideInfo();
                }
            }
            else{
                infoPanel.hideInfo();
            }
        });


        root = new BorderPane();
        root.setCenter(canvasArea);
        scene = new Scene(root);
    }

    public Scene getScene(){
        return scene;
    }
}
