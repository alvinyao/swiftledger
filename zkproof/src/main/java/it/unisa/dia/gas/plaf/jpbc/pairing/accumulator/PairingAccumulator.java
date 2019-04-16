package it.unisa.dia.gas.plaf.jpbc.pairing.accumulator;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor.Accumulator;

/**
 * The interface Pairing accumulator.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public interface PairingAccumulator extends Accumulator<Element> {

    /**
     * Add pairing pairing accumulator.
     *
     * @param e1 the e 1
     * @param e2 the e 2
     * @return the pairing accumulator
     */
    public PairingAccumulator addPairing(Element e1, Element e2);

    /**
     * Add pairing inverse pairing accumulator.
     *
     * @param e1 the e 1
     * @param e2 the e 2
     * @return the pairing accumulator
     */
    public PairingAccumulator addPairingInverse(Element e1, Element e2);

    /**
     * Add pairing pairing accumulator.
     *
     * @param pairingPreProcessing the pairing pre processing
     * @param e2                   the e 2
     * @return the pairing accumulator
     */
    public PairingAccumulator addPairing(PairingPreProcessing pairingPreProcessing, Element e2);

}
