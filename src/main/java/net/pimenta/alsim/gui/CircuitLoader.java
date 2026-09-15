package net.pimenta.alsim.gui;

import net.pimenta.alsim.gui.elements.*;
import net.pimenta.alsim.util.Misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class CircuitLoader {

    public static void saveCircuit(CircuitEditor editor, Viewport viewport,Path path) throws IOException{
        Files.writeString(path,saveCircuit(editor,viewport));
    }

    public static String saveCircuit(CircuitEditor editor, Viewport viewport) throws IOException {
        StringBuilder out = new StringBuilder();

        String name = editor.getCircuitName() == null ? "noname" : editor.getCircuitName();
        String desc = editor.getCircuitDesc() == null ? "nothing" : editor.getCircuitDesc();

        out.append("* Al.Sim circuit simulator file !\n");
        out.append(".version 1\n");

        out.append('\n');


        out.append(".name \"")
                .append(name)
                .append("\"\n");

        out.append('\n');

        out.append(".desc\n")
                .append(desc);

        out.append('\n');

        out.append(".cam ")
                .append(viewport.getOffsetX())
                .append(' ')
                .append(viewport.getOffsetY())
                .append(' ')
                .append(viewport.getZoom())
                .append('\n');

        out.append('\n');

        out.append(".nodes\n");

        for (GraphicNode node : editor.getNodes()) {
            out.append(node.getNode())
                    .append(" ")
                    .append(node.getX())
                    .append(" ")
                    .append(node.getY())
                    .append("\n");
        }

        out.append("\n");

        out.append(".comps\n");

        for (GraphicComponent component : editor.getComponents()) {
            if (component instanceof GraphicResistor resistor) {
                var nodes = resistor.getNodes();

                out.append(resistor.getId())
                        .append(" ")
                        .append(nodes.get(0).getNode())
                        .append(" ")
                        .append(nodes.get(1).getNode())
                        .append(" ")
                        .append(resistor.getR())
                        .append("\n");

            } else if (component instanceof GraphicVSource source) {
                var nodes = source.getNodes();

                out.append(source.getId())
                        .append(" ")
                        .append(nodes.get(0).getNode())
                        .append(" ")
                        .append(nodes.get(1).getNode())
                        .append(" ")
                        .append(source.getV())
                        .append("\n");
            }
        }

        out.append("\n");

        out.append(".wires\n");

        for (GraphicWire wire : editor.getWires()) {
            var nodes = wire.getNodes();

            out.append(wire.getId())
                    .append(" ")
                    .append(nodes.getFirst().getNode())
                    .append(" ")
                    .append(nodes.getSecond().getNode())
                    .append("\n");
        }

        out.append('\n');

        out.append(".end\n");

        return out.toString();
    }

    public static void openCircuit(CircuitEditor editor, Viewport viewport, Path path) throws IOException {
        try (Reader reader = Files.newBufferedReader(path)) {
            openCircuit(editor, viewport, reader);
        }
    }

    public static void openCircuit(CircuitEditor editor, Viewport viewport, Reader reader) throws IOException {
        BufferedReader buffered = new BufferedReader(reader);

        String line;
        String pendingLine = null;

        while(true) {

            if(pendingLine != null){
                line = pendingLine;
                pendingLine = null;
            }else{
                line = buffered.readLine();
            }

            if(line == null) break;

            line = line.trim();
            if(line.isEmpty() || line.charAt(0) == '*') continue;

            if(line.equals(".end")) break;


            String[] tokens = line.split("\\s+");
            String directive = tokens[0];

            switch (directive) {
                case ".name" -> editor.setCircuitName(parseQuotedValue(line));
                case ".desc" -> {
                    StringBuilder desc = new StringBuilder();
                    while((line = buffered.readLine()) != null){
                        line = line.trim();
                        if(line.startsWith(".")){
                            pendingLine = line;
                            break;
                        }
                        if(!line.isBlank()) desc.append(line).append('\n');
                    }
                    editor.setCircuitDesc(desc.toString());
                }
                case ".version" -> {
                    break;
                }
                case ".cam" -> {
                    double x = Double.parseDouble(tokens[1]);
                    double y = Double.parseDouble(tokens[2]);
                    double zoom = Double.parseDouble(tokens[3]);

                    viewport.setOffsetX(x);
                    viewport.setOffsetY(y);
                    viewport.setZoom(zoom);
                }
                case ".nodes" -> {
                    while ((line = buffered.readLine()) != null) {
                        line = line.trim();
                        if (line.isEmpty() || line.charAt(0) == '*') continue;

                        if(line.startsWith(".")){
                            pendingLine = line;
                            break;
                        }

                        String[] parts = line.split("\\s+");

                        int id = Integer.parseInt(parts[0]);
                        double x = Double.parseDouble(parts[1]);
                        double y = Double.parseDouble(parts[2]);

                        editor.addNode(new GraphicNode(id,x,y));
                    }
                }
                case ".wires" -> {
                    while ((line = buffered.readLine()) != null) {
                        line = line.trim();
                        if (line.isEmpty() || line.charAt(0) == '*') continue;

                        if(line.startsWith(".")){
                            pendingLine = line;
                            break;
                        }

                        String[] parts = line.split("\\s+");

                        int id = Integer.parseInt(parts[0]);
                        int aId = Integer.parseInt(parts[1]);
                        int bId = Integer.parseInt(parts[2]);

                        GraphicNode a = editor.getNode(aId);
                        GraphicNode b = editor.getNode(bId);

                        if (a == null || b == null)
                            throw new RuntimeException("Node doesn't exist");

                        editor.addWire(new GraphicWire(id,a,b));
                    }
                }
                case ".comps" -> {
                    while((line = buffered.readLine()) != null)  {
                        line = line.trim();
                        if (line.isEmpty() || line.charAt(0) == '*') continue;

                        if(line.startsWith(".")){
                            pendingLine = line;
                            break;
                        }

                        String[] parts = line.split("\\s+");

                        String id = parts[0];
                        char type = id.charAt(0);

                        switch (type) {
                            case 'R':
                                int aId = Integer.parseInt(parts[1]);
                                int bId = Integer.parseInt(parts[2]);
                                double R = Misc.parseValue(parts[3]);

                                GraphicNode a = editor.getNode(aId);
                                GraphicNode b = editor.getNode(bId);

                                if (a == null || b == null)
                                    throw new RuntimeException("Node doesn't exist");

                                editor.addComponent(new GraphicResistor(id, a, b, R));
                                break;

                            case 'V':
                                int posId = Integer.parseInt(parts[1]);
                                int negId = Integer.parseInt(parts[2]);
                                double V = Misc.parseValue(parts[3]);

                                GraphicNode pos = editor.getNode(posId);
                                GraphicNode neg = editor.getNode(negId);

                                if (pos == null || neg == null)
                                    throw new RuntimeException("Node doesn't exist");

                                editor.addComponent(new GraphicVSource(id, pos, neg, V));
                                break;

                            default:
                                throw new RuntimeException("Unknown component: " + type);
                        }
                    }
                }
            }
        }
    }

    private static String parseQuotedValue(String line) {
        int first = line.indexOf('"');
        int last  = line.lastIndexOf('"');
        if(first < 0 || last <= first){
            throw new RuntimeException("Expected quoted string: " + line);
        }
        return line.substring(first+1,last);
    }

}
