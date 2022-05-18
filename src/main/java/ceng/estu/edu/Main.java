package ceng.estu.edu;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import java.io.*;
import java.util.*;
class Options{
    @Option(name = "-i", usage = "Input file's name", required = true)
    String input_file;
}
public class Main {

    public static void main(String[] args) throws CmdLineException {
        ArrayList<Node> allNodes= new ArrayList<>();
        Set<String> chOfLines= new HashSet<>();
        Node right = null;

        Options options= new Options();
        CmdLineParser parser= new CmdLineParser(options);
        parser.parseArgument(args);


        List<String> lines = new ArrayList<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    options.input_file));
            String line = reader.readLine();
            while (line != null) {
                // read next line
                lines.add(line);
                line = reader.readLine();

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(String line:lines){
            if(!line.contains("->")){
                chOfLines.add(line);
            }
            else{
                String[] split_lines = line.split("->");
                String[] requests= split_lines[0].split(",");
                for(String req:requests){
                    chOfLines.add(req);
                }
                chOfLines.add(split_lines[1]);
            }
        }
        for(String el:chOfLines){
            allNodes.add(new Node(el));
        }
        for(String line:lines) {
            if (line.contains("->")) {
                String[] split_lines = line.split("->");
                String[] requests = split_lines[0].split(",");
                for (Node right_side : allNodes) {
                    if (Objects.equals(right_side.name, split_lines[1])) {
                        right = right_side;
                        break;
                    }
                }
                for (Node n : allNodes) {
                    if (split_lines[0].contains(n.name)) {
                        right.waitFor.add(n);
                        n.whoIsWaitingForMe.add(right);
                    }
                }
            }
        }

        for(Node node: allNodes){
            node.numOfWaitFor=node.waitFor.size();
            node.start();
        }


    }
}
