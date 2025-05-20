package deque;

public class ArrayDeque<T> {
    private T[] items;
    private int size;
    public static final int INITIAL_ARRAY_SIZE = 8;
    public static final int R_FACTOR = 2;
    public int USAGE_RATIO = size / items.length;

    public ArrayDeque() {
        items = (T[]) new Object[INITIAL_ARRAY_SIZE];
        size = 0;
    }

    /*
    public void resizeArray() {
        if(items.length>16) {
            while(USAGE_RATIO > 0.25) {
                resize(items.length / 2)
            }
        }
    }
     */
     public void resize(int capacity) {
     T[] temp = (T[]) new Object[capacity];
     System.arraycopy(items, 0, temp, 0, size);
     items = temp;
     }

    public void addFirst(T item) {
        if(size == items.length) {
            resize(size * R_FACTOR);
        }
        T[] temp = (T[]) new Object[items.length];
        temp[0] = item;
        System.arraycopy(items, 0, temp, 1, size);
        items = temp;
        size++;
    }

    public void addLast(T x) {
        if(size == items.length) {
            resize(size * R_FACTOR);
        }
        items[size] = x;
        size++;
    }
    public T getLast() {
        return items[size - 1];
    }
    public T get(int index) {
        return items[index];
    }
    public T removeLast() {
        if(isEmpty()) {
            return null;
        }
        T item = items[size - 1];
        items[size - 1] = null;
        size--;
        return item;
    }
    public T removeFirst() {
        if(isEmpty()) {
            return null;
        }
        T[] temp = (T[]) new Object[items.length];
        T item = items[0];
        System.arraycopy(items, 1, temp, 0, size-1);
        items = temp;
        size--;
        return item;
    }
    public int size() {
        return size;
    }
    public boolean isEmpty() {
        if(size == 0) {
            return true;
        }
        return false;
    }
    public void printDeque() {
        for(T i : items) {
            System.out.println(i + " ");
        }
        System.out.println();
    }
}

