package net.pimenta.alsim.gui.simulate;

import net.pimenta.alsim.gui.CircuitEditor;


public class SimulationState {
    SimulationResults results = null;
    boolean dirty = true;
    double  time = 0;

    public void simulate(CircuitEditor editor){
        Simulator simulator = new Simulator();
        try {
            SimulationResults newResults = simulator.simulate(editor);
            results = newResults;
            dirty = false;
            time = 0;
        }
        catch (Exception e){
            System.err.println("Failed to simulate: " + e.getMessage());
        }
    }

    public NodeResult getNodeResult(int id){
        if(results == null) return null;
        return results.getNodeResult(id);
    }

    public ComponentResult getComponentResult(String id){
        if(results == null) return null;
        return results.getComponentResult(id);
    }

    public boolean isDirty(){
        return dirty;
    }

    public void invalidate(){
        dirty = true;
    }

}
