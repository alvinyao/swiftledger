/*
 * Copyright (c) [2016] [ <ether.camp> ]
 * This file is part of the ethereumJ library.
 *
 * The ethereumJ library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The ethereumJ library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the ethereumJ library. If not, see <http://www.gnu.org/licenses/>.
 */
package com.higgschain.trust.evmcontract.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Queues execution tasks into a single pipeline where some tasks can be executed in parallel
 * but preserve 'messages' order so the next task process messages on a single thread in
 * the same order they were added to the previous executor
 * <p>
 * Created by Anton Nashatyrev on 23.02.2016.
 *
 * @param <In>  the type parameter
 * @param <Out> the type parameter
 */
public class ExecutorPipeline<In, Out> {

    private BlockingQueue<Runnable> queue;
    private ThreadPoolExecutor exec;
    private boolean preserveOrder = false;
    private Function<In, Out> processor;
    private Consumer<Throwable> exceptionHandler;
    private ExecutorPipeline<Out, ?> next;

    private AtomicLong orderCounter = new AtomicLong();
    private long nextOutTaskNumber = 0;
    private Map<Long, Out> orderMap = new HashMap<>();
    private ReentrantLock lock = new ReentrantLock();
    private String threadPoolName;

    private static AtomicInteger pipeNumber = new AtomicInteger(1);
    private AtomicInteger threadNumber = new AtomicInteger(1);

    /**
     * Instantiates a new Executor pipeline.
     *
     * @param threads          the threads
     * @param queueSize        the queue size
     * @param preserveOrder    the preserve order
     * @param processor        the processor
     * @param exceptionHandler the exception handler
     */
    public ExecutorPipeline(int threads, int queueSize, boolean preserveOrder, Function<In, Out> processor,
                            Consumer<Throwable> exceptionHandler) {
        queue = new LimitedQueue<>(queueSize);
        exec = new ThreadPoolExecutor(threads, threads, 0L, TimeUnit.MILLISECONDS, queue, r ->
                new Thread(r, threadPoolName + "-" + threadNumber.getAndIncrement())
        );
        this.preserveOrder = preserveOrder;
        this.processor = processor;
        this.exceptionHandler = exceptionHandler;
        this.threadPoolName = "pipe-" + pipeNumber.getAndIncrement();
    }

    /**
     * Add executor pipeline.
     *
     * @param threads   the threads
     * @param queueSize the queue size
     * @param consumer  the consumer
     * @return the executor pipeline
     */
    public ExecutorPipeline<Out, Void> add(int threads, int queueSize, final Consumer<Out> consumer) {
        return add(threads, queueSize, false, out -> {
            consumer.accept(out);
            return null;
        });
    }

    /**
     * Add executor pipeline.
     *
     * @param <NextOut>     the type parameter
     * @param threads       the threads
     * @param queueSize     the queue size
     * @param preserveOrder the preserve order
     * @param processor     the processor
     * @return the executor pipeline
     */
    public <NextOut> ExecutorPipeline<Out, NextOut> add(int threads, int queueSize, boolean preserveOrder,
                                                        Function<Out, NextOut> processor) {
        ExecutorPipeline<Out, NextOut> ret = new ExecutorPipeline<>(threads, queueSize, preserveOrder, processor, exceptionHandler);
        next = ret;
        return ret;
    }

    private void pushNext(long order, Out res) {
        if (next != null) {
            if (!preserveOrder) {
                next.push(res);
            } else {
                lock.lock();
                try {
                    if (order == nextOutTaskNumber) {
                        next.push(res);
                        while (true) {
                            nextOutTaskNumber++;
                            Out out = orderMap.remove(nextOutTaskNumber);
                            if (out == null) {
                                break;
                            }else {
                                next.push(out);
                            }
                        }
                    } else {
                        orderMap.put(order, res);
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    /**
     * Push.
     *
     * @param in the in
     */
    public void push(final In in) {
        final long order = orderCounter.getAndIncrement();
        exec.execute(() -> {
            try {
                pushNext(order, processor.apply(in));
            } catch (Throwable e) {
                exceptionHandler.accept(e);
            }
        });
    }

    /**
     * Push all.
     *
     * @param list the list
     */
    public void pushAll(final List<In> list) {
        for (In in : list) {
            push(in);
        }
    }

    /**
     * Sets thread pool name.
     *
     * @param threadPoolName the thread pool name
     * @return the thread pool name
     */
    public ExecutorPipeline<In, Out> setThreadPoolName(String threadPoolName) {
        this.threadPoolName = threadPoolName;
        return this;
    }

    /**
     * Gets queue.
     *
     * @return the queue
     */
    public BlockingQueue<Runnable> getQueue() {
        return queue;
    }

    /**
     * Gets order map.
     *
     * @return the order map
     */
    public Map<Long, Out> getOrderMap() {
        return orderMap;
    }

    /**
     * Shutdown.
     */
    public void shutdown() {
        try {
            exec.shutdown();
        } catch (Exception e) {
        }
        if (next != null) {
            exec.shutdown();
        }
    }

    /**
     * Is shutdown boolean.
     *
     * @return the boolean
     */
    public boolean isShutdown() {
        return exec.isShutdown();
    }

    /**
     * Shutdowns executors and waits until all pipeline
     * submitted tasks complete
     *
     * @throws InterruptedException the interrupted exception
     */
    public void join() throws InterruptedException {
        exec.shutdown();
        exec.awaitTermination(10, TimeUnit.MINUTES);
        if (next != null) {
            next.join();
        }
    }

    private static class LimitedQueue<E> extends LinkedBlockingQueue<E> {
        /**
         * Instantiates a new Limited queue.
         *
         * @param maxSize the max size
         */
        public LimitedQueue(int maxSize) {
            super(maxSize);
        }

        @Override
        public boolean offer(E e) {
            // turn offer() and add() into a blocking calls (unless interrupted)
            try {
                put(e);
                return true;
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            return false;
        }
    }
}
