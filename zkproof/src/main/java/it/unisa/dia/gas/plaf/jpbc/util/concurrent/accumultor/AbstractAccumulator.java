package it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor;

import it.unisa.dia.gas.plaf.jpbc.util.concurrent.ExecutorServiceUtils;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.Pool;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.PoolExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

/**
 * The type Abstract accumulator.
 *
 * @param <T> the type parameter
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public abstract class AbstractAccumulator<T> extends PoolExecutor<T> implements Accumulator<T> {

    /**
     * The Result.
     */
    protected T result;

    /**
     * Instantiates a new Abstract accumulator.
     */
    public AbstractAccumulator() {
        this(ExecutorServiceUtils.getFixedThreadPool());
    }

    /**
     * Instantiates a new Abstract accumulator.
     *
     * @param executor the executor
     */
    public AbstractAccumulator(Executor executor) {
        super(executor);
    }


    public Accumulator<T> accumulate(Callable<T> callable) {
        submit(callable);

        return this;
    }

    public Pool<T> submit(Runnable runnable) {
        throw new IllegalStateException("Invalid call method!");
    }

    public Accumulator<T> awaitTermination() {
        try {
            for (int i = 0; i < counter; i++)
                reduce(pool.take().get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            counter = 0;
        }
        return this;
    }

    public T getResult() {
        return result;
    }

    public T awaitResult() {
        return awaitTermination().getResult();
    }

    /**
     * Reduce.
     *
     * @param value the value
     */
    protected abstract void reduce(T value);

}
