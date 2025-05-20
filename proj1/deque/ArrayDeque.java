package deque;

public class ArrayDeque<T> {
    private T[] items;
    private int size;
    public static final int INITIAL_ARRAY_SIZE = 8;
    public static final int R_FACTOR = 2;
    public int USAGE_RATIO = size / items.length;

    private int first;
    private int last;

    public ArrayDeque() {
        items = (T[]) new Object[INITIAL_ARRAY_SIZE];
        size = 0;
        first = 0;
        last = 0;
    }

    public boolean needsResizing() {
        if (size == items.length) {
            return true;
        }
        if (Math.abs(first - last) == 1) {
            return true;
        }
        if (Math.abs(first - last) == (items.length - 1)) {
            return true;
        }
        return false;
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

    #WRONG
    public void reduceArray() {
        if(items.length>16) {
            while(USAGE_RATIO > 0.25) {
                resize(items.length / 2)
            }
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
                 if( i >= newfirst) {
                     temp[i] = items[  newfirst - i + first]
                 }
                 else temp[i] = null;
             }
             
         }

     }
     items = temp;
     }

    public void addFirst(T item) {
        changeFirst();
        items[first] = item;
        size++;
    }

    public void addLast(T item) {
        changeLast();
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
        return item;
    }

    //TODO: CHECK IF BOTH REMOVES HANDLE SINGLE ELEMENT ALISTS

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
        return item;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
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