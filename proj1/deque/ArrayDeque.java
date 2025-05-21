package deque;

public class ArrayDeque<T> {
    private T[] items;
    private int size;
    public static final int INITIAL_ARRAY_SIZE = 8;
    public static final int R_FACTOR = 2;

    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        items = (T[]) new Object[INITIAL_ARRAY_SIZE];
        size = 0;
        nextFirst = 3;
        nextLast = 4;
    }
    public int first(){
        if(!isEmpty()){
            if(nextFirst != (items.length - 1)) {
                return nextFirst + 1;
            } else {
                return 0;
            }
        }
        return -1;
    }
    public int last(){
        if(!isEmpty()){
            if(nextLast != 0) {
                return nextLast - 1;
            } else {
                return size - 1;
            }
        }
        return -1;
    }
    public boolean needsReducing(){
        double USAGE_RATIO = (double) size / items.length;
        return (USAGE_RATIO < 0.25);
    }
    public boolean needsResizing() {
        return (size == items.length);
    }
    public void reduceArray() {
        if(items.length>16) {
            while(needsReducing()) {
                resize(items.length / 2);
            }
        }
    }
    public void nextFirst() {
        if (!needsResizing()) {
            if(nextFirst != 0) {
                nextFirst--;
            } else {
                nextFirst = items.length - 1;
            }
        } else {
            resize(size * R_FACTOR);
        }
    }
    public void nextLast() {
        if (!needsResizing()) {
            if(nextLast != (items.length - 1)) {
                nextLast++;
            } else {
                nextLast = 0;
            }
        } else {
            resize(size * R_FACTOR);
        }
    }
     public void resize(int capacity) {
     T[] temp = (T[]) new Object[capacity];
     if(capacity > items.length) {
         if(nextFirst == (size - 1) && nextLast == 0) {
             System.arraycopy(items, 0, temp, 0, size);
         } else {
             int first = nextFirst + 1;
             int last = nextLast - 1;
             System.arraycopy(items, first, temp, 0, size - first);
             System.arraycopy(items, 0, temp, size - first, first);
             nextFirst = capacity - 1;
             nextLast = size;
         }
     }
     if(capacity < items.length) {
         int first = first();
         int last = last();
         if(first < last) {
             System.arraycopy(items, first, temp, 0, size);
             nextFirst = capacity - 1;
             nextLast = size;
         }
         if (first > last) {
             int index = 0;
             for(int i = first; i < size; i++) {
                 temp[index] = items[i];
                 index++;
             }
             for(int i = 0; i < last + 1; i++) {
                 temp[index] = items[i];
                 index++;
             }
         }
     }
     items = temp;
     }
    public void addFirst(T item) {
        items[nextFirst] = item;
        size++;
        nextFirst();
    }
    public void addLast(T item) {
        items[nextLast] = item;
        size++;
        nextLast();
    }
    public T getLast() {
        if(nextLast != 0) {
            return items[nextLast - 1];
        }
        return items[size - 1];
    }
    public T getFirst() {
        if(nextFirst != (size - 1)) {
            return items[nextFirst + 1];
        }
        return items[0];
    }
    public T get(int index) {
        int first = first();
        int last = last();
        if (first < last) {
            return items[first + index];
        }
        if(index < size - first) {
            return items[first + index];
        }
        return items[index + first - size];
    }
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        int last = last();
        T item = items[last];
        items[last] = null;
        if(last == (size - 1)) {
            nextLast = size - 1;
        } else {
            nextLast = nextLast - 1;
        }
        size--;
        reduceArray();
        return item;
    }
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        int first = first();
        T item = items[first];
        items[first] = null;
        if(first == 0) {
            nextFirst = 0;
        } else {
            nextFirst = nextFirst + 1;
        }
        size--;
        reduceArray();
        return item;
    }
    public int size() {
        return size;
    }
    public boolean isEmpty() {
        return (size == 0);
    }
    public void printDeque() {
        int first = first();
        int last = last();
        while(first != last) {
            System.out.print(items[first] + " ");
            first++;
            if(first == items.length){
                first = 0;
            }
        }
    }
}