package net.pimenta.alsim.cak;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Circuit {
    private final Node gnd;

    private final Set<Integer> nodeIds = new HashSet<>();
    private final List<Component> components = new ArrayList<>();

    private int extraVarOffset = 0;

    Circuit(Node gnd){
        this.gnd = gnd;
    }

    public int extraVars(){
        return extraVarOffset;
    }

    public List<Component> components(){
        return  components;
    }

    public void add(Component component){
        for(Node node : component.nodes){
            nodeIds.add(node.getId());
        }

        if(component.extraVars() > 0){
            component.setIndex(extraVarOffset++);
        }
        components.add(component);
    }
    public Component get(String id){
        for(Component component : components){
            if(component.id.equals(id)) return component;
        }
        return null;
    }

    public int nodeCount(){
        return nodeIds.size();
    }
}
