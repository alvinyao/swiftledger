package it.unisa.dia.gas.plaf.jpbc.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;

/**
 * The type Pool executor.
 *
 * @param <T> the type parameter
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class PoolExecutor<T> implements Pool<T> {

    /**
     * The Pool.
     */
    protected CompletionService<T> pool;
    /**
     * The Counter.
     */
    protected int counter;

    /**
     * Instantiates a new Pool executor.
     */
    public PoolExecutor() {
        this(ExecutorServiceUtils.getFixedThreadPool());
    }

    /**
     * Instantiates a new Pool executor.
     *
     * @param executor the executor
     */
    public PoolExecutor(Executor executor) {
        this.pool = new ExecutorCompletionService<T>(executor);
        this.counter = 0;
    }


    public Pool<T> submit(Callable<T> callable) {
        counter++;
        pool.submit(callable);

        return this;
    }

    public Pool<T> submit(Runnable runnable) {
        counter++;
        pool.submit(runnable, null);

        return this;
    }

    public Pool<T> awaitTermination() {
        try {
            for (int i = 0; i < counter; i++)
                pool.take().get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            counter = 0;
        }

        return this;
    }

}
