package net.pimenta.alsim.gui;

import net.pimenta.alsim.gui.elements.GraphicNode;
import net.pimenta.alsim.gui.elements.GraphicWire;
import net.pimenta.alsim.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetResolver {
    private final Map<Integer, Integer> parent = new HashMap<>();
    private final Map<Integer,Integer>  netNumbers = new HashMap<>();

    public NetResolver(List<GraphicNode> nodes, List<GraphicWire> wires){
        for(GraphicNode node : nodes){
            parent.put(node.getNode(),node.getNode());
        }

        for(GraphicWire wire : wires){
            Pair<GraphicNode,GraphicNode> wireNodes = wire.getNodes();
            union(wireNodes.getFirst().getNode(), wireNodes.getSecond().getNode());
        }

        int nextNet = 0;
        if(parent.containsKey(0)){
            netNumbers.put(find(0),0);
            nextNet = 1;
        }
        for(GraphicNode node : nodes){
            int root = find(node.getNode());
            if(!netNumbers.containsKey(root)){
                netNumbers.put(root,nextNet++);
            }
        }
    }

    public int netOf(GraphicNode node){
        return netNumbers.get(find(node.getNode()));
    }

    private int find(int node){
        if(parent.get(node) != node){
            parent.put(node,find(parent.get(node)));
        }
        return parent.get(node);
    }

    private void union(int a, int b){
        int rootA = find(a);
        int rootB = find(b);
        if(rootA != rootB){
            parent.put(rootB,rootA);
        }
    }
}
