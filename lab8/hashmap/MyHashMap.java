package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author arnav miranda
 */
public class MyHashMap<K, V> implements Map61B<K, V>, Iterable<K> {

    private class newIterator implements Iterator<K> {
        int pos = 0;
        public boolean hasNext() {
            return pos < keys.size();
        }
        public K next() {
            return keys.get(pos++);
        }
    }
    @Override
    public Iterator<K> iterator() {
        return new newIterator();
    }
    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private static final int RESIZE_FACTOR = 2;
    private Collection<Node>[] buckets;
    private int INITIAL_SIZE = 16;
    private int size;
    private double MAX_LOAD_FACTOR = 0.75;
    private ArrayList<K> keys;
    private HashSet<K> keySet;


    /** Constructors */
    public MyHashMap() {
        this(16, 0.75);
    }
    public MyHashMap(int size) {
        this(size, 0.75);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        size = 0;
        INITIAL_SIZE = initialSize;
        keys = new ArrayList<K>();
        keySet = new HashSet<K>();
        MAX_LOAD_FACTOR = maxLoad;
        buckets = createTable(INITIAL_SIZE);
        for(int i = 0; i < INITIAL_SIZE; i++) {
            buckets[i] = createBucket();
        }
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    private double presentLoadFactor() {
        return (double) size / buckets.length;
    }
    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    private int reduce(int hash, int l) {
        hash = Math.floorMod(hash, l);
        return hash;
    }

    private boolean resizeRequired() {
        return (presentLoadFactor() > MAX_LOAD_FACTOR);
    }

    private void resize() {
        int length = buckets.length;
        Collection<Node>[] temp = createTable(length * RESIZE_FACTOR);
        for(int i = 0; i < length * RESIZE_FACTOR; i++) {
            temp[i] = createBucket();
        }
        for(int i = 0; i < length; i++) {
            for(Node node : buckets[i]) {
                int hash = node.hashCode();
                int index = reduce(hash, length * RESIZE_FACTOR);
                temp[index].add(node);
            }
        }
        buckets = temp;
    }

    @Override
    /** Removes all the mappings from this map. */
    public void clear(){
        buckets = createTable(INITIAL_SIZE);
        keySet.clear();
        size = 0;
    }

    /** Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key){
        return keySet.contains(key);
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key){
        int hash = key.hashCode();
        int index = reduce(hash, buckets.length);
        ArrayList<Node> bucket = (ArrayList<Node>) buckets[index];
        for(Node node : bucket) {
            if(node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    /** Returns the number of key-value mappings in this map. */
    @Override
    public int size(){
        return size;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    @Override
    public void put(K key, V value){
        Node node = createNode(key, value);
        int hash = node.hashCode();
        int index = reduce(hash, buckets.length);
        ArrayList<Node> bucket = (ArrayList<Node>) buckets[index];
        bucket.add(node);
        size++;
        if(resizeRequired()) {
            resize();
        }
    }

    /** Returns a Set view of the keys contained in this map. */
    public Set<K> keySet(){
        return keySet;
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.
     */
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

}
