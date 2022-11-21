package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ChainedHashMap<K, V> extends AbstractIterableMap<K, V> {

    private ArrayMap<K, V>[] entries;
    private int size;
    private int elementnum = 0;
    private static final double DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD = 2;
    private static final int DEFAULT_INITIAL_CHAIN_COUNT = 1000;
    private static final int DEFAULT_INITIAL_CHAIN_CAPACITY = 5;


    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    AbstractIterableMap<K, V>[] chains;

    // You're encouraged to add extra fields (and helper methods) though!

    /**
     * Constructs a new ChainedHashMap with default resizing load factor threshold,
     * default initial chain count, and default initial chain capacity.
     */
    public ChainedHashMap() {
        this(DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD, DEFAULT_INITIAL_CHAIN_COUNT, DEFAULT_INITIAL_CHAIN_CAPACITY);
    }

    /**
     * Constructs a new ChainedHashMap with the given parameters.
     *
     * @param resizingLoadFactorThreshold the load factor threshold for resizing. When the load factor
     *                                    exceeds this value, the hash table resizes. Must be > 0.
     * @param initialChainCount the initial number of chains for your hash table. Must be > 0.
     * @param chainInitialCapacity the initial capacity of each ArrayMap chain created by the map.
     *                             Must be > 0.
     */
    public ChainedHashMap(double resizingLoadFactorThreshold, int initialChainCount, int chainInitialCapacity) {
        ArrayMap<K, V> internal = new ArrayMap<>(chainInitialCapacity);
        entries = new ArrayMap[DEFAULT_INITIAL_CHAIN_COUNT];
        for (int i = 0; i < DEFAULT_INITIAL_CHAIN_COUNT; i++) {
            ArrayMap<K, V> curr = new ArrayMap<>(chainInitialCapacity);
            entries[i] = curr;
        }
    }
    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code AbstractIterableMap<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     * @see ArrayMap createArrayOfEntries method for more background on why we need this method
     */
    @SuppressWarnings("unchecked")
    private AbstractIterableMap<K, V>[] createArrayOfChains(int arraySize) {
        return (AbstractIterableMap<K, V>[]) new AbstractIterableMap[arraySize];
    }

    /**
     * Returns a new chain.
     *
     * This method will be overridden by the grader so that your ChainedHashMap implementation
     * is graded using our solution ArrayMaps.
     *
     * Note: You do not need to modify this method.
     */
    protected AbstractIterableMap<K, V> createChain(int initialSize) {
        return new ArrayMap<>(initialSize);
    }

    @Override
    public V get(Object key) {
        int number;
        if (key == null) {
                for (int i = 0; i < DEFAULT_INITIAL_CHAIN_COUNT; i++) {
                    ArrayMap<K, V> curr = entries[i];
                    if (curr.containsKey(key)) {
                        return curr.get(key);
                    }
                }
        } else {
            number = key.hashCode();
            if (number < 0) {
                number = number + 1;
            } else {
                number = number - 1;
            }
            number = Math.abs(number);
            while (number > size) {
                number = number / 2;
            }
            AbstractIterableMap<K, V> curr = entries[number];
            return curr.get(key);
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        int number;
        if (key == null) {
            Random rand = new Random();
             number = rand.nextInt(DEFAULT_INITIAL_CHAIN_COUNT);
        } else {
             number = key.hashCode();
            if (number < 0) {
                number = number + 1;
            } else {
                number = number - 1;
            }
            number = Math.abs(number);
            while (number > size) {
                number = number / 2;
            }
        }
        ArrayMap<K, V> curr = entries[number];
        elementnum++;
        return curr.put(key, value);
    }

    @Override
    public V remove(Object key) {
        int number;
        if (key == null) {
            Random rand = new Random();
            number = rand.nextInt(DEFAULT_INITIAL_CHAIN_COUNT);
        } else {
            number = key.hashCode();
            if (number < 0) {
                number = number + 1;
            } else {
                number = number - 1;
            }
            number = Math.abs(number);
            while (number > size) {
                number = number / 2;
            }
        }
        ArrayMap<K, V> curr = entries[number];
        elementnum--;
        return curr.remove(key);
    }

    @Override
    public void clear() {
        entries = new ArrayMap[DEFAULT_INITIAL_CHAIN_COUNT];
        elementnum = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        int number;
        if (key == null) {
        for (int i = 0; i < DEFAULT_INITIAL_CHAIN_COUNT; i++) {
            ArrayMap<K, V> curr = entries[i];
            if (curr.containsKey(key)) {
                return true;
            }
        }
        } else {
            number = key.hashCode();
            if (number < 0) {
                number = number + 1;
            } else {
                number = number - 1;
            }
            number = Math.abs(number);
            while (number > size) {
                number = number / 2;
            }
            ArrayMap<K, V> curr = entries[number];
            return curr.containsKey(key);
        }
        return false;
    }

    @Override
    public int size() {
        return elementnum;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: you won't need to change this method (unless you add more constructor parameters)
        return new ChainedHashMapIterator<>(this.entries);
    }

    /*
    See the assignment webpage for tips and restrictions on implementing this iterator.
     */
    private static class ChainedHashMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private AbstractIterableMap<K, V>[] chains;
        // You may add more fields and constructor parameters
        private AbstractIterableMap<K, V> curr;
        private int chainIndex = 0;
        private Iterator iter;
        public ChainedHashMapIterator(AbstractIterableMap<K, V>[] chains) {
            this.chains = chains;
            while (this.chains[chainIndex] == null) {
                chainIndex++;
            }
            curr = this.chains[chainIndex];
            iter = curr.iterator();
        }

        @Override
        public boolean hasNext() {
            if (iter.hasNext()) {
                return true;
            } else {
                while (chains[chainIndex] == null) {
                    chainIndex++;
                }
                return (iter.hasNext());
            }

        }
        @Override
        public Map.Entry<K, V> next() {
            if (iter.hasNext()) {
                return (Entry<K, V>) iter.next();
            } else {
                while (chains[chainIndex] == null) {
                    chainIndex++;
                }
                curr = chains[chainIndex];
                if (iter.hasNext()) {
                    return (Entry<K, V>) iter.next();
                }
            }
                throw new NoSuchElementException();
        }
    }
}
