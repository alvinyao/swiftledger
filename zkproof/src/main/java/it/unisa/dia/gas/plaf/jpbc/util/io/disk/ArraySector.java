package it.unisa.dia.gas.plaf.jpbc.util.io.disk;

/**
 * The interface Array sector.
 *
 * @param <T> the type parameter
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public interface ArraySector<T> extends Sector {

    /**
     * Gets size.
     *
     * @return the size
     */
    int getSize();

    /**
     * Gets at.
     *
     * @param index the index
     * @return the at
     */
    T getAt(int index);

    /**
     * Sets at.
     *
     * @param index the index
     * @param value the value
     */
    void setAt(int index, T value);

    /**
     * Gets at.
     *
     * @param label the label
     * @return the at
     */
    T getAt(String label);

    /**
     * Sets at.
     *
     * @param label the label
     * @param value the value
     */
    void setAt(String label, T value);

}
