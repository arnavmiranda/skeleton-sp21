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
    private IntNode sentinel = new IntNode(null , null);
    private int size;

    public SLList() {
        size = 0;
    }
    public SLList(T x) {
        sentinel.next = new IntNode(x, null);
        size = 1;
    }

    /** Adds element to beginning of SLL, after sentinel node. */
    public void addFirst(T x) {
        sentinel.next = new IntNode(x, sentinel);
        size += 1;
    }
    /** Returns first element of SLL, after sentinel node. */
    public T getFirst() {
        return sentinel.next.item;
    }
    /** Adds a node to the final node of SLL. */
    public void addLast(T x) {
        IntNode iter = sentinel.next;
        while(iter.next!=null){
            iter = iter.next;
        }
        iter.next = new IntNode(x, null);
        size += 1;
    }
    /** Returns size of the SLL, calculated after the sentinel node. */
    public int size() {
        return this.size;
    }
}










public class LinkedListDeque {
}
