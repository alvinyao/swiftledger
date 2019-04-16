package it.unisa.dia.gas.plaf.jpbc.util.io.disk;

import java.nio.ByteBuffer;

/**
 * The interface Sector.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public interface Sector {

    /**
     * The enum Mode.
     */
    enum Mode {/**
     * Init mode.
     */
    INIT,
        /**
         * Read mode.
         */
        READ}

    /**
     * Gets length in bytes.
     *
     * @return the length in bytes
     */
    int getLengthInBytes();

    /**
     * Map to sector.
     *
     * @param mode   the mode
     * @param buffer the buffer
     * @return the sector
     */
    Sector mapTo(Mode mode, ByteBuffer buffer);

}
