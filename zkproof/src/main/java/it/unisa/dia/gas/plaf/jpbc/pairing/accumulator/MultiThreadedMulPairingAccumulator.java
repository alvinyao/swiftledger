package it.unisa.dia.gas.plaf.jpbc.pairing.accumulator;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

/**
 * The type Multi threaded mul pairing accumulator.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class MultiThreadedMulPairingAccumulator extends AbstractPairingAccumulator {

    /**
     * Instantiates a new Multi threaded mul pairing accumulator.
     *
     * @param pairing the pairing
     */
    public MultiThreadedMulPairingAccumulator(Pairing pairing) {
        super(pairing);
    }

    /**
     * Instantiates a new Multi threaded mul pairing accumulator.
     *
     * @param pairing the pairing
     * @param value   the value
     */
    public MultiThreadedMulPairingAccumulator(Pairing pairing, Element value) {
        super(pairing, value);
    }


    protected void reduce(Element value) {
        this.result.mul(value);
    }
}
