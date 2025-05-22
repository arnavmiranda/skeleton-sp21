package deque;

public class LinkedListDeque<T> implements Deque<T> {
    private class Node {
        public T item;
        public Node next;
        public Node prev;

        Node(T item, Node next, Node prev) {
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
    public LinkedListDeque(T item) {
        Node element = new Node(item, sentinel, sentinel);
        sentinel.next = element;
        sentinel.prev = element;
        size = 1;
    }
    private void makeEmpty() {
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }
    @Override
    public void addFirst(T item) {
        Node element = new Node(item, sentinel.next, sentinel);
        sentinel.next.prev = element;
        sentinel.next = element;
        size += 1;
    }
    @Override
    public void addLast(T item) {
        Node element = new Node(item, sentinel, sentinel.prev);
        sentinel.prev.next = element;
        sentinel.prev = element;
        size += 1;
    }
    @Override
    public int size() {
        return this.size;
    }
    @Override
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
    @Override
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
    @Override
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
    @Override
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
