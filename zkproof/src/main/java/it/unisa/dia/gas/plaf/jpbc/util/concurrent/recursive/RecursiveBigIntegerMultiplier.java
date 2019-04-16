package it.unisa.dia.gas.plaf.jpbc.util.concurrent.recursive;

import java.math.BigInteger;
import java.util.concurrent.RecursiveTask;

/**
 * The type Recursive big integer multiplier.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class RecursiveBigIntegerMultiplier extends RecursiveTask<BigInteger> {
    /**
     * The Sequential threshold.
     */
    static final int SEQUENTIAL_THRESHOLD = 2;

    /**
     * The Values.
     */
    BigInteger[] values;
    /**
     * The Low.
     */
    int low;
    /**
     * The High.
     */
    int high;

    /**
     * Instantiates a new Recursive big integer multiplier.
     *
     * @param values the values
     * @param lo     the lo
     * @param hi     the hi
     */
    public RecursiveBigIntegerMultiplier(BigInteger[] values, int lo, int hi) {
        this.values = values;
        this.low = lo;
        this.high = hi;
    }

    protected BigInteger compute() {
        if (high == low) {
            return values[low];
        }

        if (high - low < SEQUENTIAL_THRESHOLD) {
            return values[low].multiply(values[high]);
        } else {
            int mid = low + (high - low) / 2;

            RecursiveBigIntegerMultiplier left = new RecursiveBigIntegerMultiplier(values, low, mid);
            RecursiveBigIntegerMultiplier right = new RecursiveBigIntegerMultiplier(values, mid + 1, high);
            left.fork();

            BigInteger rightAns = right.compute();
            BigInteger leftAns = left.join();
            return rightAns.multiply(leftAns);
        }
    }

}

