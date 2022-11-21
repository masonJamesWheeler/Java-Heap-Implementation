package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @see AbstractIterableMap
 * @see Map
 */

public class ArrayMap<K, V> extends AbstractIterableMap<K, V> {
    private SimpleEntry[] elements;
    private int size;
    private static final int DEFAULT_INITIAL_CAPACITY = 100;
    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    SimpleEntry<K, V>[] entries;
    // You may add extra fields or helper methods though!

    /**
     * Constructs a new ArrayMap with default initial capacity.
     */
    public ArrayMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }


    /**
     * Constructs a new ArrayMap with the given initial capacity (i.e., the initial
     * size of the internal array).
     *
     * @param initialCapacity the initial capacity of the ArrayMap. Must be > 0.
     */
    public ArrayMap(int initialCapacity) {
        this.entries = this.createArrayOfEntries(initialCapacity);
        elements = new SimpleEntry[initialCapacity];
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code Entry<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     */
    @SuppressWarnings("unchecked")
    private SimpleEntry<K, V>[] createArrayOfEntries(int arraySize) {
        /*
        It turns out that creating arrays of generic objects in Java is complicated due to something
        known as "type erasure."

        We've given you this helper method to help simplify this part of your assignment. Use this
        helper method as appropriate when implementing the rest of this class.

        You are not required to understand how this method works, what type erasure is, or how
        arrays and generics interact.
        */

        return (SimpleEntry<K, V>[]) (new SimpleEntry[arraySize]);
    }

    @Override
    public V get(Object key) {
        int index = 0;
        while (index < size) {
            Entry curr = entries[index];
            if (java.util.Objects.equals(curr.getKey(), key)) {
                return (V) curr.getValue();
            } else {
                index++;
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        int index = 0;
        while (index < size) {
            Entry curr = entries[index];
            if (java.util.Objects.equals(curr.getKey(), key)) {
                V toReturn = (V) curr.getValue();
                curr.setValue(value);
                return toReturn;
            } else {
                index++;
            }
        }
        if (index >= size-10) {
            entries = resize(entries);
        }
        SimpleEntry insert = new SimpleEntry(key, value);
        entries[index] = insert;
        size++;
        return null;
    }
    @Override
    public V remove(Object key) {
        int index = 0;
        while (index < size) {
            Entry curr = entries[index];
            if (java.util.Objects.equals(curr.getKey(), key)) {
                V temp = (V) curr.getValue();
                for (int i = index; i < size-1; i++) {
                    entries[i] = entries[i+1];
                }
                size = size -1;
                return temp;
            } else {
                index++;
            }
        }
        return null;
    }
    private SimpleEntry[] resize(SimpleEntry[] e) {
        SimpleEntry[] temp = new SimpleEntry[e.length + 100];
        for (int i = 0; i < size; i++) {
            temp[i] = e[i];
        }
        return temp;
    }
    @Override
    public void clear() {
        elements = new SimpleEntry[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public boolean containsKey(Object key) {
        for (int i = 0; i < size; i++) {
            Entry curr = entries[i];
            if (java.util.Objects.equals(curr.getKey(), key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: You may or may not need to change this method, depending on whether you
        // add any parameters to the ArrayMapIterator constructor.
        return new ArrayMapIterator<>(this.entries);
    }

    private static class ArrayMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private final SimpleEntry<K, V>[] entries;
        // You may add more fields and constructor parameters
        private int index = -1;

        public ArrayMapIterator(SimpleEntry<K, V>[] entries) {
            this.entries = entries;
        }

        @Override
        public boolean hasNext() {

            return (entries[index+1] != null);
        }

        @Override
        public Map.Entry<K, V> next() {
            if (hasNext()) {
                index++;
                return entries[index];
            }
            else {
                throw new NoSuchElementException();
            }

        }
    }
}

