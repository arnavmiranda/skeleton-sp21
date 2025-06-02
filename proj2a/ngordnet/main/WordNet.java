package ngordnet.main;

import edu.princeton.cs.introcs.In;

import java.util.*;

public class WordNet {
    private Graph graph;
    private TreeMap<String, Node> wordKeyDictionary;
    private TreeMap<Integer, Node> idKeyDictionary;

    private class Node {
        int id;
        String name;

        Node(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public WordNet(String synFile, String hypFile) {
        graph = new Graph();
        wordKeyDictionary = new TreeMap<>();
        idKeyDictionary = new TreeMap<>();

        In syn = new In(synFile);

        String line;
        String[] splitLine;
        Node node;
        int id;
        String synset;
        String[] terms;

        while (!syn.isEmpty()) {
            line = syn.readLine();
            splitLine = line.split(",");
            id = Integer.parseInt(splitLine[0]);
            graph.addNode(id);
            synset = splitLine[1];
            node = new Node(id, synset);
            idKeyDictionary.put(id, node);

            if (synset.contains(" ")) {
                terms = synset.split(" ");
                for (String word : terms) {
                    wordKeyDictionary.put(word, node);
                }
            } else {
                wordKeyDictionary.put(synset, node);
            }
        }
        syn.close();

        In hyp = new In(hypFile);
        String num;
        int n;
        while(!hyp.isEmpty()) {
            line = hyp.readLine();
            splitLine = line.split(",");
            id = Integer.parseInt(splitLine[0]);
            for(int i = 1; i < splitLine.length; i++) {
                num = splitLine[i];
                n = Integer.parseInt(num);
                graph.addEdge(id, n);
            }
        }
    }

    /* NOW DEVISE METHODS THAT THE WORD NET INSTANCE CALLED BY HYPONYM HANDLER SHOULD USE */

}

