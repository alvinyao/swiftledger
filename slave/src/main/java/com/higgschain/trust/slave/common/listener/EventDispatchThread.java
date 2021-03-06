package com.higgschain.trust.slave.common.listener;

import com.higgschain.trust.network.utils.Threads;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.*;

/**
 * The type Event dispatch thread.
 *
 * @author duhongming
 * @date 2018 /12/17
 */
public class EventDispatchThread {
    private static final Logger logger = LoggerFactory.getLogger("blockchain");
    private static EventDispatchThread eventDispatchThread;

    private static final int[] queueSizeWarnLevels = new int[]{0, 10_000, 50_000, 100_000, 250_000, 500_000, 1_000_000, 10_000_000};

    private final BlockingQueue<Runnable> executorQueue = new LinkedBlockingQueue<Runnable>();
    private final ExecutorService executor = new ThreadPoolExecutor(1, 1, 0L,
            TimeUnit.MILLISECONDS, executorQueue, Threads.namedThreads("EDT-%d", logger));

    private long taskStart;
    private Runnable lastTask;
    private int lastQueueSizeWarnLevel = 0;
    private int counter;

    /**
     * Returns the default instance for initialization of Autowired instances
     * to be used in tests
     *
     * @return the default
     */
    public static EventDispatchThread getDefault() {
        if (eventDispatchThread == null) {
            eventDispatchThread = new EventDispatchThread() {
                @Override
                public void invokeLater(Runnable r) {
                    r.run();
                }
            };
        }
        return eventDispatchThread;
    }

    /**
     * Invoke later.
     *
     * @param task the task
     */
    public void invokeLater(final Runnable task) {
        if (executor.isShutdown()) {
            return;
        }
        if (counter++ % 1000 == 0) {
            logStatus();
        }

        executor.submit(() -> {
            try {
                lastTask = task;
                taskStart = System.nanoTime();
                task.run();
                long t = (System.nanoTime() - taskStart) / 1_000_000;
                taskStart = 0;
                if (t > 1000) {
                    logger.warn("EDT task executed in more than 1 sec: " + t + "ms, " + "Executor queue size: " + executorQueue.size());
                }
            } catch (Exception e) {
                logger.error("EDT task exception", e);
            }
        });
    }

    private void logStatus() {
        int curLevel = getSizeWarnLevel(executorQueue.size());
        if (lastQueueSizeWarnLevel == curLevel) {
            return;
        }

        synchronized (this) {
            if (curLevel > lastQueueSizeWarnLevel) {
                long t = taskStart == 0 ? 0 : (System.nanoTime() - taskStart) / 1_000_000;
                String msg = "EDT size grown up to " + executorQueue.size() + " (last task executing for " + t + " ms: " + lastTask;
                if (curLevel < 3) {
                    logger.info(msg);
                } else {
                    logger.warn(msg);
                }
            } else if (curLevel < lastQueueSizeWarnLevel) {
                logger.info("EDT size shrunk down to " + executorQueue.size());
            }
            lastQueueSizeWarnLevel = curLevel;
        }
    }

    private static int getSizeWarnLevel(int size) {
        int idx = Arrays.binarySearch(queueSizeWarnLevels, size);
        return idx >= 0 ? idx : -(idx + 1) - 1;
    }

    /**
     * Shutdown.
     */
    public void shutdown() {
        executor.shutdownNow();
        try {
            executor.awaitTermination(10L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.warn("shutdown: executor interrupted: {}", e.getMessage());
        }
    }
}
