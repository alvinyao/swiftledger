package it.unisa.dia.gas.plaf.jpbc.pairing.immutable;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;

/**
 * The type Immutable pairing pre processing.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class ImmutablePairingPreProcessing implements PairingPreProcessing {

    private PairingPreProcessing pairingPreProcessing;

    /**
     * Instantiates a new Immutable pairing pre processing.
     *
     * @param pairingPreProcessing the pairing pre processing
     */
    public ImmutablePairingPreProcessing(PairingPreProcessing pairingPreProcessing) {
        this.pairingPreProcessing = pairingPreProcessing;
    }

    public Element pairing(Element in2) {
        return pairingPreProcessing.pairing(in2).getImmutable();
    }

    public byte[] toBytes() {
        return pairingPreProcessing.toBytes();
    }
}
