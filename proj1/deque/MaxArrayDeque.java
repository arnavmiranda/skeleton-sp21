package deque;
import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {

    private Comparator<T> comparator;
    public MaxArrayDeque(Comparator<T> c) {
        comparator = c;
    }

    public T max() {
        return max(comparator);
    }
    public T max(Comparator<T> c) {
        int max = 0;
        if(isEmpty()) return null;
        for(int i = 0; i < size(); i ++) {
            T item = (T) get(i);
            if(comparator.compare(item, get(max)) > 0) {
                max = i;
            }
        }
        return get(max);
    }
}