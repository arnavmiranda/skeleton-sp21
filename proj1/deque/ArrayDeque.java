package deque;
import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {

    private class newIterator implements Iterator<T> {
        private int pos;
        public newIterator() {
            pos = 0;
        }
        public boolean hasNext() {
            return pos < size;
        }
        public T next() {
            return get(pos++);
        }
    }
    public Iterator<T> iterator() {
        return new newIterator();
    }
    public static int ARRAY_SIZE = 8;
    public static final int RESIZE_FACTOR = 2;

    private int nextStart;
    private int nextEnd;
    private int size;

    private T[] items;

    public ArrayDeque() {
        items = (T[]) new Object[ARRAY_SIZE];
        nextStart = items.length - 1;
        nextEnd = 0;
        size = 0;
    }
    public ArrayDeque(int capacity) {
        items = (T[]) new Object[capacity];
        nextStart = items.length - 1;
        nextEnd = 0;
        size = 0;
    }

    private int length() {
        return items.length;
    }

    private int start() {
        if (nextStart == items.length - 1) {
            return 0;
        }
        return nextStart + 1;
    }

    private int end() {
        if (nextEnd == 0) {
            return items.length - 1;
        }
        return nextEnd - 1;
    }

    private boolean full() {
        return (size == items.length);
    }

    private boolean startFromFirst() {
        return (start() == 0);
    }

    private boolean endsAtLast() {
        return (end() == items.length - 1);
    }

    private void compress() {
        if(length() <= 16) {
            return;
        }
        double USAGE_RATIO = (double) size / length();
        while (USAGE_RATIO < 0.25) {
            T[] temp = (T[]) new Object[length() / 2];
            int start = start();
            if (start < end()) {
                System.arraycopy(items, start, temp, 0, size);
            } else {
                System.arraycopy(items, start, temp, 0, length() - start);
                System.arraycopy(items, 0, temp, length() - start, size + start - length());
            }
            items = temp;
            nextStart = length() - 1;
            nextEnd = size;
            USAGE_RATIO = (double) size / length();
            if(USAGE_RATIO >= 0.25) {
                break;
            }
        }
    }

    private void resize(int capacity) {
        if (size > capacity) return;
        if (!full()) return;

        T[] temp = (T[]) new Object[capacity];
        int start = start();
        if (startFromFirst()) {
            System.arraycopy(items, 0, temp, 0, size);
        } else {
            System.arraycopy(items, start, temp, 0, length() - start);
            System.arraycopy(items, 0, temp, length() - start, start);
        }
        items = temp;
        nextStart = length() - 1;
        nextEnd = size;
    }

    private void changeNextStart() {
        if (!full()) {
            if (nextStart == 0) {
                nextStart = length() - 1;
                return;
            }
            nextStart--;
        }
    }
    private void changeNextEnd() {
        if (!full()) {
            if (nextEnd == length() - 1) {
                nextEnd = 0;
                return;
            }
            nextEnd++;
        }
    }

    @Override
    public void addFirst(T item) {
        resize(size * RESIZE_FACTOR);
        items[nextStart] = item;
        size++;
        changeNextStart();
    }
    @Override
    public void addLast(T item) {
        resize(size * RESIZE_FACTOR);
        items[nextEnd] = item;
        size++;
        changeNextEnd();
    }
    @Override
    public int size() {
        return size;
    }
    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        int actualIndex = (start() + index) % items.length;
        return items[actualIndex];
    }
    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        int first = start();
        T item = items[first];
        items[first] = null;
        size--;
        nextStart = first;
        compress();
        return item;
    }
    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        int last = end();
        T item = items[last];
        items[last] = null;
        size--;
        nextEnd = last;
        compress();
        return item;
    }
    @Override
    public void printDeque() {
        if (isEmpty()) {
            return;
        }
        if (start() < end()) {
            for (int i = start(); i <= end(); i++) {
                System.out.print(items[i] + " ");
            }
        } else {
            for (int i = start(); i < length(); i++) {
                System.out.print(items[i] + " ");
            }
            for (int i = 0; i <= end(); i++) {
                System.out.print(items[i] + " ");
            }
        }
        System.out.println();
    }
    @Override
    public boolean equals(Object o) {
        if(o instanceof ArrayDeque other) {
            if(other == this) {
                return true;
            } else {
                for(int i = 0; i < size; i++) {
                    if(!(other.get(i).equals(get(i)))) {
                        return false;
                    }
                }
                return true;
            }
        } else  {
            return false;
        }
    }
}
