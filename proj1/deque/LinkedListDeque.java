package deque;



class LList<T> {

    /** Internal private class for the foundation of a Singly Linked List, ie SLL.
     * This IntNode class is not accessed by any other class outside SSL, and is
     * hence kept private. If need be, can be changed to public without much issue.
     *
     * Ultimately this LList is implemented through a Circular Sentinel Node Doubly
     * Linked List data structure.
     */

    private class IntNode {
        public T item;
        public IntNode next;
        public IntNode prev;

        public IntNode(T item, IntNode next, IntNode prev) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }

    /* Sentinel Node implementation of Singly Linked Lists. */
    private IntNode sentinel = new IntNode(null , null, null);
    private int size;

    public LList() {
        size = 0;
    }
    public LList(T x) {
        IntNode element = new IntNode(x, sentinel, sentinel);
        sentinel.next = element;
        sentinel.prev = element;
        size = 1;
    }

    /** Adds element to beginning of LL. */
    public void addFirst(T x) {
        IntNode element = new IntNode(x, sentinel.next, sentinel);
        sentinel.next.prev = element;
        sentinel.next = element;
        size += 1;
    }
    /** Returns first element of LL. */
    public T getFirst() {
        return sentinel.next.item;
    }
    /** Adds a node to the final node of SLL. */
    public void addLast(T x) {
        IntNode element = new IntNode(x, sentinel, sentinel.prev);
        sentinel.prev.next = element;
        sentinel.prev = element;
        size += 1;
    }
    public T getLast() {
        return sentinel.prev.item;
    }
    /** Returns size of the SLL, calculated after the sentinel node. */
    public int size() {
        return this.size;
    }
}



public class LinkedListDeque {
}
