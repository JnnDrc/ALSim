package net.pimenta.alsim.gui.simulate;

public class ComponentResult {
    private final double voltage;
    private final double current;
    private final double power;
    private final double resistance;

    public ComponentResult(double voltage, double current, double power, double resistance) {
        this.voltage = voltage;
        this.current = current;
        this.power = power;
        this.resistance = resistance;
    }

    public double getVoltage() {
        return voltage;
    }

    public double getCurrent() {
        return current;
    }

    public double getPower() {
        return power;
    }

    public double getResistance() {
        return resistance;
    }
}
