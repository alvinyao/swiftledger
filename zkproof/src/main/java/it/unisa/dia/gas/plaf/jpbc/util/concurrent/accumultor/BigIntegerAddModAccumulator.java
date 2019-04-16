package it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor;

import java.math.BigInteger;

/**
 * The type Big integer add mod accumulator.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class BigIntegerAddModAccumulator extends AbstractAccumulator<BigInteger> {

    private BigInteger modulo;

    /**
     * Instantiates a new Big integer add mod accumulator.
     *
     * @param modulo the modulo
     */
    public BigIntegerAddModAccumulator(BigInteger modulo) {
        this.result = BigInteger.ZERO;
        this.modulo = modulo;
    }

    protected void reduce(BigInteger value) {
        result = result.add(value).mod(modulo);
    }

}
