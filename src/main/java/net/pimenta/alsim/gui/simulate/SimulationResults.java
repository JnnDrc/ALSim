package net.pimenta.alsim.gui.simulate;

import net.pimenta.alsim.gui.elements.GraphicComponent;
import net.pimenta.alsim.gui.elements.GraphicElement;
import net.pimenta.alsim.gui.elements.GraphicNode;

import java.util.HashMap;
import java.util.Map;

public class SimulationResults {
    private final Map<String, ComponentResult> components = new HashMap<>();
    private final Map<Integer, NodeResult>     nodes      = new HashMap<>();

    public void addComponentResult(String id, ComponentResult result){
        components.put(id,result);
    }

    public void addNodeResult(Integer id, NodeResult result){
        nodes.put(id,result);
    }

    public ComponentResult getComponentResult(String id){
        return components.get(id);
    }
    public NodeResult getNodeResult(int nodeId){
        return nodes.get(nodeId);
    }
}
