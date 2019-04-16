package it.unisa.dia.gas.plaf.jpbc.pairing.map;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import it.unisa.dia.gas.jpbc.Point;

/**
 * The interface Pairing map.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public interface PairingMap {

    /**
     * Pairing element.
     *
     * @param in1 the in 1
     * @param in2 the in 2
     * @return the element
     */
    Element pairing(Point in1, Point in2);

    /**
     * Is product pairing supported boolean.
     *
     * @return the boolean
     */
    boolean isProductPairingSupported();

    /**
     * Pairing element.
     *
     * @param in1 the in 1
     * @param in2 the in 2
     * @return the element
     */
    Element pairing(Element[] in1, Element[] in2);

    /**
     * Final pow.
     *
     * @param element the element
     */
    void finalPow(Element element);

    /**
     * Is almost coddh boolean.
     *
     * @param a the a
     * @param b the b
     * @param c the c
     * @param d the d
     * @return the boolean
     */
    boolean isAlmostCoddh(Element a, Element b, Element c, Element d);

    /**
     * Gets pairing pre processing length in bytes.
     *
     * @return the pairing pre processing length in bytes
     */
    int getPairingPreProcessingLengthInBytes();

    /**
     * Pairing pairing pre processing.
     *
     * @param in1 the in 1
     * @return the pairing pre processing
     */
    PairingPreProcessing pairing(Point in1);

    /**
     * Pairing pairing pre processing.
     *
     * @param source the source
     * @param offset the offset
     * @return the pairing pre processing
     */
    PairingPreProcessing pairing(byte[] source, int offset);


}
