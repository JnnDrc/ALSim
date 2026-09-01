package net.pimenta.alsim.cak;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class NetlistParser {
    public Circuit parse(Path path) throws RuntimeException{
            List<String> lines;
        try{
            lines = Files.readAllLines(path);
        }
        catch (IOException e){
            throw new RuntimeException("Failed to read netlist '" + path.toString() +"';\n+" + e.getMessage());
        }

        Circuit circuit = new Circuit(new Node(0));

        for(String line : lines){
            line = line.trim();
            if(line.isEmpty()) continue;
            if(line.charAt(0) == '*') continue;

            if(line.equals(".end")) break;

            String[] tokens = line.split(" ");
            String id = tokens[0];
            char type = Character.toUpperCase(id.charAt(0));

            switch (type){
                case 'R':
                    int a = Integer.parseInt(tokens[1]);
                    int b = Integer.parseInt(tokens[2]);
                    double R = parseValue(tokens[3]);
                    circuit.add(new Resistor(id,new Node(a),new Node(b),R));
                    break;
                case 'V':
                    int pos = Integer.parseInt(tokens[1]);
                    int neg = Integer.parseInt(tokens[2]);
                    double V = parseValue(tokens[3]);
                    circuit.add(new VSource(id,new Node(pos),new Node(neg),V));
                    break;
                default:
                    System.err.println("[NL]: Unknown component " + type);
                    break;
            }
        }

        return circuit;
    }

    double parseValue(String text){
        char suffix = text.charAt(text.length()-1);
        String withoutLast = new StringBuilder(text).deleteCharAt(text.length() - 1).toString();

        return switch (suffix) {
            case 'k' -> Double.parseDouble(withoutLast) * 1e3;
            case 'm' -> Double.parseDouble(withoutLast) * 1e-3;
            case 'u' -> Double.parseDouble(withoutLast) * 1e-6;
            case 'n' -> Double.parseDouble(withoutLast) * 1e-9;
            case 'p' -> Double.parseDouble(withoutLast) * 1e-12;
            default -> Double.parseDouble(text);
        };
    }
}
