package it.unisa.dia.gas.plaf.jpbc.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The type Executor service utils.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class ExecutorServiceUtils {

    private static ExecutorService fixedThreadPool;
    private static ExecutorService cachedThreadPool;

    static {
        fixedThreadPool = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() * 4
        );
        cachedThreadPool = Executors.newCachedThreadPool();
    }


    private ExecutorServiceUtils() {
    }

    /**
     * Gets fixed thread pool.
     *
     * @return the fixed thread pool
     */
    public static ExecutorService getFixedThreadPool() {
        return fixedThreadPool;
    }

    /**
     * Gets cached thread pool.
     *
     * @return the cached thread pool
     */
    public static ExecutorService getCachedThreadPool() {
        return cachedThreadPool;
    }

    /**
     * Shutdown.
     */
    public static void shutdown() {
        fixedThreadPool.shutdown();
        cachedThreadPool.shutdown();
    }

    /**
     * The type Index callable.
     *
     * @param <T> the type parameter
     * @author Angelo De Caro (jpbclib@gmail.com)
     * @since 2.0.0
     */
    public abstract static class IndexCallable<T> implements Callable<T> {
        /**
         * The .
         */
        protected int i, /**
         * The J.
         */
        j;

        /**
         * Instantiates a new Index callable.
         *
         * @param i the
         */
        public IndexCallable(int i) {
            this.i = i;
        }

        /**
         * Instantiates a new Index callable.
         *
         * @param i the
         * @param j the j
         */
        public IndexCallable(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    /**
     * The type Index runnable.
     *
     * @author Angelo De Caro (jpbclib@gmail.com)
     * @since 1.2.2
     */
    public abstract static class IndexRunnable implements Runnable {
        /**
         * The .
         */
        protected int i, /**
         * The J.
         */
        j;

        /**
         * Instantiates a new Index runnable.
         *
         * @param i the
         */
        public IndexRunnable(int i) {
            this.i = i;
        }

        /**
         * Instantiates a new Index runnable.
         *
         * @param i the
         * @param j the j
         */
        public IndexRunnable(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    /**
     * The type Interval callable.
     *
     * @param <T> the type parameter
     * @author Angelo De Caro (jpbclib@gmail.com)
     * @since 2.0.0
     */
    public abstract static class IntervalCallable<T> implements Callable<T> {
        /**
         * The From.
         */
        protected int from, /**
         * The To.
         */
        to;

        /**
         * Instantiates a new Interval callable.
         *
         * @param from the from
         * @param to   the to
         */
        protected IntervalCallable(int from, int to) {
            this.from = from;
            this.to = to;
        }
    }

}
