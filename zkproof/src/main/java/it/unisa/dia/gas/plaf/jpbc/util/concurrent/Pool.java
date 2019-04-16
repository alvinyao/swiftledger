package it.unisa.dia.gas.plaf.jpbc.util.concurrent;

import java.util.concurrent.Callable;

/**
 * The interface Pool.
 *
 * @param <T> the type parameter
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public interface Pool<T> {

    /**
     * Submit pool.
     *
     * @param callable the callable
     * @return the pool
     */
    Pool<T> submit(Callable<T> callable);

    /**
     * Submit pool.
     *
     * @param runnable the runnable
     * @return the pool
     */
    Pool<T> submit(Runnable runnable);

    /**
     * Await termination pool.
     *
     * @return the pool
     */
    Pool<T> awaitTermination();

}
