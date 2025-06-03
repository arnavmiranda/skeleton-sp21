package ngordnet.main;
import java.util.*;

public class Graph {
    private class intList extends LinkedList<Integer> {}
    private ArrayList<intList> adjList;

    public Graph() {
        adjList = new ArrayList<>();
    }

    public void addNode(int v) {
        adjList.add(v, new intList());
    }
    public void addEdge(int from, int to) {
        intList neighbors = adjList.get(from);
        neighbors.addLast(to);
    }
    public LinkedList<Integer> adj(int i) {
        return adjList.get(i);
    }
    public void children(TreeSet<Integer> L, int id) {
        for(int i : adj(id)) {
            L.add(i);
            children(L, i);
        }
    }
}