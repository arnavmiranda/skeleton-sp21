package deque;

public class ArrayDeque<T> {
    private T[] items;
    private int size;
    public static final int INITIAL_ARRAY_SIZE = 8;
    public static final int R_FACTOR = 2;

    private int first;
    private int last;

    public ArrayDeque() {
        items = (T[]) new Object[INITIAL_ARRAY_SIZE];
        size = 0;
        first = 0;
        last = 0;
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
    public void changeFirst() {
        if (!needsResizing()) {
            if (first != 0) {
                first--;
            } else {
                first = items.length - 1;
            }
        } else {
            resize(size * R_FACTOR);
        }
    }
    public void changeLast() {
        if (!needsResizing()) {
            if (last != items.length - 1) {
                last++;
            } else {
                last = 0;
            }
        } else {
            resize(size * R_FACTOR);
        }
    }
     public void resize(int capacity) {
     T[] temp = (T[]) new Object[capacity];
     if(capacity > items.length) {
         if(first < last) {
             System.arraycopy(items, first, temp, first, size);
         } else {
             int newfirst = capacity - (size - 1 - first);
             for(int i = 0; i < capacity; i++) {
                 if(i <= last) {
                     temp[i] = items[i];
                 }
                 if(i >= newfirst) {
                     temp[i] = items[i - newfirst + first];
                 }
                 else temp[i] = null;
             }
             first = newfirst;
         }
     }
     if(capacity < items.length) {
         if(first < last) {
             System.arraycopy(items, first, temp, 0, size);
             first = 0;
             last = size;
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
             first = 0;
             last = size - 1;
         }
     }
     items = temp;
     }
    public void addFirst(T item) {
        if(isEmpty()) {
            first = 0;
            last = 0;
        } else {
            changeFirst();
        }
        items[first] = item;
        size++;
    }
    public void addLast(T item) {
        if(isEmpty()) {
            first = 1;
            last = 1;
        } else {
            changeLast();
        }
        items[last] = item;
        size++;
    }
    public T getLast() {
        return items[last];
    }
    public T getFirst() {
        return items[first];
    }
    public int circularIndex(int index) {
        int num = first;
        while (num != size) {
            if (index == 0) {
                return num;
            }
            index--;
            num++;
            if (num == size) {
                num = 0;
            }
        }
        return -1;
    }
    public T get(int index) {
        return items[circularIndex(index)];
    }
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T item = items[last];
        items[last] = null;
        if (last != 0) {
            last--;
        } else {
            last = size - 1;
        }
        size--;
        reduceArray();
        return item;
    }
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T item = items[first];
        items[first] = null;
        if (first != (size - 1)) {
            first++;
        } else {
            first = 0;
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
        int num = first;
        while (num != size) {
            System.out.print(items[num] + " ");
            num++;
            if (num == size) {
                num = 0;
            }
        }
        System.out.println();
    }
}