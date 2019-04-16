package it.unisa.dia.gas.plaf.jpbc.pairing.map;

import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import it.unisa.dia.gas.jpbc.Point;

/**
 * The type Abstract miller pairing pre processing.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public abstract class AbstractMillerPairingPreProcessing implements PairingPreProcessing {

    /**
     * The Processing info.
     */
    protected AbstractMillerPairingMap.MillerPreProcessingInfo processingInfo;

    /**
     * Instantiates a new Abstract miller pairing pre processing.
     */
    protected AbstractMillerPairingPreProcessing() {
    }

    /**
     * Instantiates a new Abstract miller pairing pre processing.
     *
     * @param in1                the in 1
     * @param processingInfoSize the processing info size
     */
    protected AbstractMillerPairingPreProcessing(Point in1, int processingInfoSize) {
        this.processingInfo = new AbstractMillerPairingMap.MillerPreProcessingInfo(processingInfoSize);
    }

    /**
     * Instantiates a new Abstract miller pairing pre processing.
     *
     * @param pairing the pairing
     * @param source  the source
     * @param offset  the offset
     */
    protected AbstractMillerPairingPreProcessing(Pairing pairing, byte[] source, int offset) {
        this.processingInfo = new AbstractMillerPairingMap.MillerPreProcessingInfo(pairing, source, offset);
    }

    public byte[] toBytes() {
        if (processingInfo != null)
            return processingInfo.toBytes();
        else
            return new byte[0];
    }
}
