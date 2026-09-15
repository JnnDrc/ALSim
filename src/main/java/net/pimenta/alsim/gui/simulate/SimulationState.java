package net.pimenta.alsim.gui.simulate;

import net.pimenta.alsim.gui.CircuitEditor;
import net.pimenta.alsim.gui.NetResolver;
import net.pimenta.alsim.gui.elements.GraphicComponent;
import net.pimenta.alsim.gui.elements.GraphicNode;


public class SimulationState {
    private SimulationResults results = null;
    private boolean dirty = true;
    private boolean live = false;
    private double  time = 0;

    public void simulate(CircuitEditor editor){
        Simulator simulator = new Simulator();
        try {
            SimulationResults newResults = simulator.simulate(editor);
            if(newResults != null) results = newResults;
            dirty = false;
            time = 0;
        }
        catch (Exception e){
            dirty = true;
            System.err.println("Failed to simulate: " + e.getMessage());
            return;
        }

        for(GraphicComponent component : editor.getComponents()) {
            component.setResult(results.getComponentResult(component.getId()));
        }

        NetResolver resolver = new NetResolver(editor.getNodes(),editor.getWires());
        for (GraphicNode node : editor.getNodes()){
            int net = resolver.netOf(node);
            NodeResult result = results.getNodeResult(net);

            System.out.println(
                    "Updating node " + node.getNode() +
                            " -> " + (result == null ? "null" : result.getVoltage()) +
                            " object=" + System.identityHashCode(node)
            );
            node.setResult(result);
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

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }
}
