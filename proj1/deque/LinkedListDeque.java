package deque;



class LList<T> {

    /**
     * Internal private class for the foundation of a Singly Linked List, ie SLL.
     * This Node class is not accessed by any other class outside SSL, and is
     * hence kept private. If need be, can be changed to public without much issue.
     * <p>
     * Ultimately this LList is implemented through a Circular Sentinel Node Doubly
     * Linked List data structure.
     */

    private class Node {
        public T item;
        public Node next;
        public Node prev;

        public Node(T item, Node next, Node prev) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }

    /* Sentinel Node implementation of Singly Linked Lists. */
    private Node sentinel = new Node(null, null, null);
    private int size;

    public LList() {
        size = 0;
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }

    public LList(T x) {
        Node element = new Node(x, sentinel, sentinel);
        sentinel.next = element;
        sentinel.prev = element;
        size = 1;
    }

    /**
     * Adds element to beginning of LL.
     */
    public void addFirst(T x) {
        Node element = new Node(x, sentinel.next, sentinel);
        sentinel.next.prev = element;
        sentinel.next = element;
        size += 1;
    }

    /**
     * Returns first element of LL.
     */
    public T getFirst() {
        return sentinel.next.item;
    }

    /**
     * Adds a node to the final node of SLL.
     */
    public void addLast(T x) {
        Node element = new Node(x, sentinel, sentinel.prev);
        sentinel.prev.next = element;
        sentinel.prev = element;
        size += 1;
    }

    public T getLast() {
        return sentinel.prev.item;
    }

    /**
     * Returns size of the SLL, calculated after the sentinel node.
     */
    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        if (sentinel.next == null && sentinel.prev == null)
            return true;
        return false;
    }

    public void makeEmpty() {
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }

    public void printDeque() {
        if (!isEmpty()) {
            Node iter = sentinel.next;
            while (iter != sentinel) {
                System.out.print(iter.item + " ");
                iter = iter.next;
            }
            System.out.println();
        }
    }

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        Node element = sentinel.next;
        sentinel.next = element.next;
        element.next.prev = sentinel;
        size--;
        return element.item;
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        Node element = sentinel.prev;
        sentinel.prev = element.prev;
        element.prev.next = sentinel;
        size--;
        return element.item;
    }
    public T get(int index) {
        int c = index;
        Node recur = sentinel.next;
        while(recur.next != sentinel) {
            if(c == 0) {
                return recur.item;
            }
            recur = recur.next;
            c++;
        }
        return null;
    }
}


/*TODO: dont forget to specify that if  sentinel is empty- both next and priv must be NULL */
public class LinkedListDeque {
}
