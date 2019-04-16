package it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor;

import java.math.BigInteger;

/**
 * The type Big integer add accumulator.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class BigIntegerAddAccumulator extends AbstractAccumulator<BigInteger> {

    /**
     * Instantiates a new Big integer add accumulator.
     */
    public BigIntegerAddAccumulator() {
        this.result = BigInteger.ZERO;
    }


    protected void reduce(BigInteger value) {
        result = result.add(value);
    }

}
