package it.unisa.dia.gas.plaf.jpbc.pairing.parameters;

import it.unisa.dia.gas.jpbc.PairingParameters;

import java.math.BigInteger;

/**
 * The interface Mutable pairing parameters.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public interface MutablePairingParameters extends PairingParameters {

    /**
     * Put object.
     *
     * @param key   the key
     * @param value the value
     */
    void putObject(String key, Object value);

    /**
     * Put big integer at.
     *
     * @param key   the key
     * @param index the index
     * @param value the value
     */
    void putBigIntegerAt(String key, int index, BigInteger value);

    /**
     * Put big integer.
     *
     * @param key   the key
     * @param value the value
     */
    void putBigInteger(String key, BigInteger value);

    /**
     * Put boolean.
     *
     * @param key   the key
     * @param value the value
     */
    void putBoolean(String key, boolean value);

}
