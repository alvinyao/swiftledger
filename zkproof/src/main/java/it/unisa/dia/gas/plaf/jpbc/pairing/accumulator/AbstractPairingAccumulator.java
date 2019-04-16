package it.unisa.dia.gas.plaf.jpbc.pairing.accumulator;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor.AbstractAccumulator;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor.Accumulator;

import java.util.concurrent.Callable;

/**
 * The type Abstract pairing accumulator.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public abstract class AbstractPairingAccumulator extends AbstractAccumulator<Element> implements PairingAccumulator {

    /**
     * The Pairing.
     */
    protected Pairing pairing;

    /**
     * Instantiates a new Abstract pairing accumulator.
     *
     * @param pairing the pairing
     */
    public AbstractPairingAccumulator(Pairing pairing) {
        this(pairing, pairing.getGT().newOneElement());
    }

    /**
     * Instantiates a new Abstract pairing accumulator.
     *
     * @param pairing the pairing
     * @param value   the value
     */
    public AbstractPairingAccumulator(Pairing pairing, Element value) {
        this.pairing = pairing;
        this.result = value;
    }


    public Accumulator<Element> accumulate(Callable<Element> callable) {
        throw new IllegalStateException("Invalid call method!");
    }

    public PairingAccumulator addPairing(final Element e1, final Element e2) {
        super.accumulate(new Callable<Element>() {
            public Element call() throws Exception {
                return pairing.pairing(e1, e2);
            }
        });

        return this;
    }

    public PairingAccumulator addPairingInverse(final Element e1, final Element e2) {
        super.accumulate(new Callable<Element>() {
            public Element call() throws Exception {
                return pairing.pairing(e1, e2).invert();
            }
        });

        return this;
    }

    public PairingAccumulator addPairing(final PairingPreProcessing pairingPreProcessing, final Element e2) {
        super.accumulate(new Callable<Element>() {
            public Element call() throws Exception {
                return pairingPreProcessing.pairing(e2);
            }
        });

        return this;
    }

}
