package deque;

public class ArrayDeque<T> {

    public static final int ARRAY_SIZE = 8;
    public static final int RESIZE_FACTOR = 2;
    private T[] items = (T[]) new Object[ARRAY_SIZE];

    private int nextStart;
    private int nextEnd;
    private int size;

    public ArrayDeque() {
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

    public void addFirst(T item) {
        resize(size * RESIZE_FACTOR);
        items[nextStart] = item;
        size++;
        changeNextStart();
    }

    public void addLast(T item) {
        resize(size * RESIZE_FACTOR);
        items[nextEnd] = item;
        size++;
        changeNextEnd();
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        int actualIndex = (start() + index) % items.length;
        return items[actualIndex];
    }

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
}
