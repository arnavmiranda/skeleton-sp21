package deque;

class LinkedListDeque<T> {
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

    private Node sentinel = new Node(null, null, null);
    private int size;

    public LinkedListDeque() {
        size = 0;
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }
    public LinkedListDeque(T x) {
        Node element = new Node(x, sentinel, sentinel);
        sentinel.next = element;
        sentinel.prev = element;
        size = 1;
    }

    public void addFirst(T x) {
        Node element = new Node(x, sentinel.next, sentinel);
        sentinel.next.prev = element;
        sentinel.next = element;
        size += 1;
    }
    public T getFirst() {
        return sentinel.next.item;
    }
    public void addLast(T x) {
        Node element = new Node(x, sentinel, sentinel.prev);
        sentinel.prev.next = element;
        sentinel.prev = element;
        size += 1;
    }
    public T getLast() {
        return sentinel.prev.item;
    }
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
        Node iter = sentinel.next;
        for(int i = 0; i < size; i++) {
            if(i == index) {
                return iter.item;
            }
            iter = iter.next;
        }
        return null;
    }
    public T getRecursive(int index) {
        Node recur = sentinel.next;
        while(recur.next != sentinel) {
            if(index == 0) {
                return recur.item;
            }
            recur = recur.next;
            index++;
        }
        return null;
    }
}
