package net.pimenta.alsim.cak;

import net.pimenta.alsim.util.Misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class NetlistParser {

    public static Circuit parse(Reader reader) throws RuntimeException, IOException {
        BufferedReader buffered = new BufferedReader(reader);

        Circuit circuit = new Circuit(new Node(0));

        String line;

        while((line = buffered.readLine()) != null) {
            line = line.trim();
            if(line.isEmpty()) continue;
            if(line.charAt(0) == '*') continue;

            if(line.equals(".end")) break;

            String[] tokens = line.split("\\s+");
            String id = tokens[0];
            char type = Character.toUpperCase(id.charAt(0));

            switch (type){
                case 'R':
                    int a = Integer.parseInt(tokens[1]);
                    int b = Integer.parseInt(tokens[2]);
                    double R = Misc.parseValue(tokens[3]);
                    circuit.add(new Resistor(id,new Node(a),new Node(b),R));
                    break;
                case 'V':
                    int pos = Integer.parseInt(tokens[1]);
                    int neg = Integer.parseInt(tokens[2]);
                    double V = Misc.parseValue(tokens[3]);
                    circuit.add(new VSource(id,new Node(pos),new Node(neg),V));
                    break;
                default:
                    System.err.println("[NL]: Unknown component " + type);
                    break;
            }
        }
        return circuit;
    }

    public static Circuit parse(Path path) throws IOException{
        return parse(Files.newBufferedReader(path));
    }

    public static Circuit parse(String string) throws IOException{
        return parse(new StringReader(string));
    }


}
