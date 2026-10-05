package net.pimenta.alsim.gui.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import net.pimenta.alsim.gui.elements.GraphicComponent;
import net.pimenta.alsim.gui.elements.GraphicNode;
import net.pimenta.alsim.gui.simulate.ComponentResult;
import net.pimenta.alsim.gui.simulate.NodeResult;
import net.pimenta.alsim.util.Engineering;


public class SimulationInfoPanel extends VBox {
    private final Label title = new Label();
    private final Label line1 = new Label();
    private final Label line2 = new Label();
    private final Label line3 = new Label();
    private final Label line4 = new Label();

    public SimulationInfoPanel() {
        super(5);

        setPadding(new Insets(10));
        setMaxSize(Region.USE_PREF_SIZE,Region.USE_PREF_SIZE);
        setMouseTransparent(true);
        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY,new CornerRadii(8),Insets.EMPTY)));
        getChildren().addAll(
                title,
                line1,
                line2,
                line3,
                line4
        );

        setVisible(false);
    }

    public void showComponent(GraphicComponent component) {
        ComponentResult result = component.getResult();
        title.setText(component.getId());

        // example
        line1.setText("R = " + Engineering.format(result.getResistance(),"R"));
        line2.setText("V = " + Engineering.format(result.getVoltage(),"V"));
        line3.setText("I = " + Engineering.format(result.getCurrent(),"A"));
        line4.setText("P = " + Engineering.format(result.getPower(),"W"));

        setVisible(true);
    }

    public void showNode(
            GraphicNode node,
            NodeResult result) {

        title.setText("Node " + node.getNode());

        line1.setText("V = " + result.getVoltage());
        line2.setText("I = " + result.getCurrent());

        line3.setText("");
        line4.setText("");

        setVisible(true);
    }

    public void hideInfo() {
        setVisible(false);
    }
}