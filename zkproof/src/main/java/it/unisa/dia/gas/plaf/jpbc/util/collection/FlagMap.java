package it.unisa.dia.gas.plaf.jpbc.util.collection;

/**
 * The type Flag map.
 *
 * @param <K> the type parameter
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class FlagMap<K> {

    /**
     * The Flags.
     */
    protected LatchHashMap<K, Boolean> flags;

    /**
     * Instantiates a new Flag map.
     */
    public FlagMap() {
        this.flags = new LatchHashMap<K, Boolean>();
    }

    /**
     * Get.
     *
     * @param key the key
     */
    public void get(K key) {
        flags.get(key);
    }

    /**
     * Set.
     *
     * @param key the key
     */
    public void set(K key) {
        flags.put(key, true);
    }

}
