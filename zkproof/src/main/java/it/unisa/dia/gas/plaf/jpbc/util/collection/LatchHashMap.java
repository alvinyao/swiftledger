package it.unisa.dia.gas.plaf.jpbc.util.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * The type Latch hash map.
 *
 * @param <K> the type parameter
 * @param <V> the type parameter
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class LatchHashMap<K, V> implements Map<K, V> {

    private Map<K, ValueLatch> internalMap;

    /**
     * Instantiates a new Latch hash map.
     */
    public LatchHashMap() {
        this.internalMap = new HashMap<K, ValueLatch>();
    }


    public int size() {
        return internalMap.size();
    }

    public boolean isEmpty() {
        return internalMap.isEmpty();
    }

    public boolean containsKey(Object key) {
        return internalMap.containsKey(key);
    }

    public boolean containsValue(Object value) {
        throw new IllegalStateException("Not implemented yet!");
    }

    public V get(Object key) {
        return getLatch(key).get();
    }

    public V put(K key, V value) {
        return getLatch(key).set(value);
    }

    public V remove(Object key) {
        throw new IllegalStateException("Not implemented yet!");
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        throw new IllegalStateException("Not implemented yet!");
    }

    public void clear() {
        throw new IllegalStateException("Not implemented yet!");
    }

    public Set<K> keySet() {
        throw new IllegalStateException("Not implemented yet!");
    }

    public Collection<V> values() {
        throw new IllegalStateException("Not implemented yet!");
    }

    public Set<Entry<K,V>> entrySet() {
        throw new IllegalStateException("Not implemented yet!");
    }

    @Override
    public boolean equals(Object o) {
        return internalMap.equals(o);
    }

    @Override
    public int hashCode() {
        return internalMap.hashCode();
    }

    /**
     * Gets latch.
     *
     * @param key the key
     * @return the latch
     */
    protected ValueLatch<V> getLatch(Object key) {
        ValueLatch<V> latch;
        synchronized (this) {
            if (containsKey(key))
                latch = internalMap.get(key);
            else {
                latch = new ValueLatch();
                internalMap.put((K) key, latch);
            }
        }
        return latch;
    }

    /**
     * The type Value latch.
     *
     * @param <V> the type parameter
     */
    class ValueLatch<V> extends CountDownLatch {

        /**
         * The Value.
         */
        V value;

        /**
         * Instantiates a new Value latch.
         */
        ValueLatch() {
            super(1);
        }

        /**
         * Set v.
         *
         * @param value the value
         * @return the v
         */
        V set(V value) {
            V old = this.value;
            this.value = value;

            countDown();

            return old;
        }

        /**
         * Get v.
         *
         * @return the v
         */
        V get() {
            try {
                await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return value;
        }
    }

}
