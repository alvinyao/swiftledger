package it.unisa.dia.gas.plaf.jpbc.util.io.disk;

/**
 * The interface Disk.
 *
 * @param <S> the type parameter
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 1.0.0
 */
public interface Disk<S extends Sector> {

    /**
     * Gets sector at.
     *
     * @param index the index
     * @return the sector at
     */
    S getSectorAt(int index);

    /**
     * Gets sector.
     *
     * @param key the key
     * @return the sector
     */
    S getSector(String key);

    /**
     * Flush.
     */
    void flush();
}
