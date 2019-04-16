package it.unisa.dia.gas.plaf.jpbc.pairing.accumulator;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

/**
 * The type Pairing accumulator factory.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class PairingAccumulatorFactory {

    private static final PairingAccumulatorFactory INSTANCE = new PairingAccumulatorFactory();

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static PairingAccumulatorFactory getInstance() {
        return INSTANCE;
    }

    private boolean multiThreadingEnabled;


    private PairingAccumulatorFactory() {
        this.multiThreadingEnabled = false; // Runtime.getRuntime().availableProcessors() > 1;
    }

    /**
     * Gets pairing multiplier.
     *
     * @param pairing the pairing
     * @return the pairing multiplier
     */
    public PairingAccumulator getPairingMultiplier(Pairing pairing) {
        return isMultiThreadingEnabled() ? new MultiThreadedMulPairingAccumulator(pairing)
                : new SequentialMulPairingAccumulator(pairing);
    }

    /**
     * Gets pairing multiplier.
     *
     * @param pairing the pairing
     * @param element the element
     * @return the pairing multiplier
     */
    public PairingAccumulator getPairingMultiplier(Pairing pairing, Element element) {
        return isMultiThreadingEnabled() ? new MultiThreadedMulPairingAccumulator(pairing, element)
                : new SequentialMulPairingAccumulator(pairing, element);
    }

    /**
     * Is multi threading enabled boolean.
     *
     * @return the boolean
     */
    public boolean isMultiThreadingEnabled() {
        return multiThreadingEnabled;
    }

    /**
     * Sets multi threading enabled.
     *
     * @param multiThreadingEnabled the multi threading enabled
     */
    public void setMultiThreadingEnabled(boolean multiThreadingEnabled) {
        this.multiThreadingEnabled = multiThreadingEnabled;
    }

}
