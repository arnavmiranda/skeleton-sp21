package ngordnet.main;

import edu.princeton.cs.introcs.In;

import java.util.*;

public class WordNet {
    private Graph graph;
    private TreeMap<String, ListOfNodes> wordKeyDictionary;
    private TreeMap<Integer, Node> idKeyDictionary;

    private class Node {
        int id;
        String name;

        Node(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }
    private class ListOfNodes extends LinkedList<Node>{ }

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
        ListOfNodes nodeList;

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
                    if(wordKeyDictionary.containsKey(word)) {
                        nodeList = wordKeyDictionary.get(word);
                        nodeList.add(node);
                    } else {
                        nodeList = new ListOfNodes();
                        nodeList.add(node);
                        wordKeyDictionary.put(word, nodeList);
                    }
                }
            } else {
                if(wordKeyDictionary.containsKey(synset)) {
                    nodeList = wordKeyDictionary.get(synset);
                    nodeList.add(node);
                } else {
                    nodeList = new ListOfNodes();
                    nodeList.add(node);
                    wordKeyDictionary.put(synset, nodeList);
                }
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

    private void spliceAdd(String string, TreeSet<String> treeset) {
        if(string.contains(" ")) {
            for(String name : string.split(" ")) {
                treeset.add(name);
            }
        } else {
            treeset.add(string);
        }
    }

    private void addToSet(List<String> L, Set<String> S) {
        for(String s : L) {
            S.add(s);
        }
    }
    public LinkedList<String> stringHyponyms(List<String> words) {
        if(words.size() == 1) {
            return stringHyponyms(words.getFirst());
        }
        TreeSet<String> set = new TreeSet<>();
        TreeSet<String> newset;

        LinkedList<String> hyponyms;

        for(int i = 0; i < words.size(); i++) {
            String string = words.get(i);
            if(i == 0) {
                addToSet(stringHyponyms(string), set);
                continue;
            }
            newset = new TreeSet<>();
            for(String word : stringHyponyms(string)) {
                if(set.contains(word)) {
                    newset.add(word);
                }
            }
            set = newset;
        }

        LinkedList<String> llist = new LinkedList<>();
        for(String s : set) {
            llist.addLast(s);
        }
        return llist;
    }


    public LinkedList<String> stringHyponyms(String word) {
        ListOfNodes nodeList = wordKeyDictionary.get(word);
        TreeSet<Integer> treeset = new TreeSet<>();
        TreeSet<String> stringTreeset = new TreeSet<>();
        String string, n;
        int id;
        for(Node node : nodeList) {
            n = node.name;
            spliceAdd(n, stringTreeset);
            id = node.id;
            graph.children(treeset, id);
            for (int i : treeset) {
                string = idKeyDictionary.get(i).name;
                spliceAdd(string, stringTreeset);
            }
        }
        LinkedList<String> llist = new LinkedList<>();
        for(String s : stringTreeset) {
            llist.addLast(s);
        }
        return llist;
    }
}

