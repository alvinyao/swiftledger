package it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor;

import it.unisa.dia.gas.plaf.jpbc.util.concurrent.Pool;

import java.util.concurrent.Callable;

/**
 * The interface Accumulator.
 *
 * @param <T> the type parameter
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public interface Accumulator<T> extends Pool<T> {

    /**
     * Accumulate accumulator.
     *
     * @param callable the callable
     * @return the accumulator
     */
    Accumulator<T> accumulate(Callable<T> callable);

    Accumulator<T> awaitTermination();

    /**
     * Await result t.
     *
     * @return the t
     */
    T awaitResult();

    /**
     * Gets result.
     *
     * @return the result
     */
    T getResult();

}
