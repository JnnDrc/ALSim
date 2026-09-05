package net.pimenta.alsim.gui.simulate;

public class NodeResult {
    private final double voltage;
    private final double current;

    public NodeResult(double voltage, double current) {
        this.voltage = voltage;
        this.current = current;
    }

    public double getVoltage() {
        return voltage;
    }

    public double getCurrent() {
        return current;
    }
}
