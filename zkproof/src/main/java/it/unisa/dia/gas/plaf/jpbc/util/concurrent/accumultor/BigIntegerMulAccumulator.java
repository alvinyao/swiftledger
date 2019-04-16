package it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor;

import java.math.BigInteger;

/**
 * The type Big integer mul accumulator.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class BigIntegerMulAccumulator extends AbstractAccumulator<BigInteger> {

    /**
     * Instantiates a new Big integer mul accumulator.
     */
    public BigIntegerMulAccumulator() {
        this.result = BigInteger.ONE;
    }


    protected void reduce(BigInteger value) {
        result = result.multiply(value);
    }

}
