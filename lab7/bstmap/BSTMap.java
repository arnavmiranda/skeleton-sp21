package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable,V> implements Map61B<K,V> {

    private class BSTNode {
        private final K key;
        private final V value;

        public BSTNode() {
            key = null;
            value = null;
        }

        public BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K key() {
            return key;
        }

        public V value() {
            return value;
        }
    }

    private BSTNode node;
    private BSTMap<K,V> right;
    private BSTMap<K,V> left;

    public BSTMap() {
        node = null;
        right = null;
        left = null;
    }
    public void put(K key, V value) {
        if(node == null) {
            node = new BSTNode(key, value);
        }
        int comparison = key.compareTo(node.key());
        if(comparison > 0) {
            if(right == null) {
                right = new BSTMap<>();
            }
            right.put(key,value);
        }
        if(comparison < 0) {
            if(left == null) {
                left = new BSTMap<>();
            }
            left.put(key,value);
        }
    }
    /** Removes all the mappings from this map. */
    public void clear() {
        node = null;
        right = null;
        left = null;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        if (node == null) {
            return false;
        }
        int comparison = key.compareTo(node.key());
        if (comparison == 0) {
            return true;
        }
        if (comparison > 0) {
            return right.containsKey(key);
        }
        if (comparison < 0) {
            return left.containsKey(key);
        }
        return false;
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key) {
        V val = null;
        if(node != null) {
            int comparison = key.compareTo(node.key());
            if (comparison == 0) {
                val = node.value();
            }
            if(comparison > 0) {
                if (right != null) {
                    val = right.get(key);
                }
            }
            if(comparison < 0) {
                if (left != null) {
                    val = left.get(key);
                }
            }
        }
        return val;
    }
    /* Returns the number of key-value mappings in this map. */
    public int size() {
        int l = 0, r = 0;
        if (node == null) {
            return 0;
        }
        if(left != null) {
            l = left.size();
        }
        if(right != null) {
            r = right.size();
        }
        return 1 + l + r;
    }



    /* Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException. */
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException. */
    public V remove(K key) {
        throw new UnsupportedOperationException();

    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 7. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
}
