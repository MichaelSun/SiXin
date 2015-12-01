package com.renren.mobile.web;

import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * at 上午10:46, 12-4-26
 *
 * @author afpro
 */
public class ResultQueue<T> {
    final Queue<T> queue = new ConcurrentLinkedQueue<T>();
    final Semaphore sem = new Semaphore(0);

    T get() throws InterruptedException {
        sem.acquire();
        return queue.poll();
    }

    T get(long timeout, TimeUnit timeUnit) throws InterruptedException, TimeoutException {
        if (!sem.tryAcquire(timeout, timeUnit)) {
            throw new TimeoutException("time out");
        }
        return queue.poll();
    }

    void add(T t) {
        queue.add(t);
        sem.release();
    }

    void addAll(T... ts) {
        if (ts != null && ts.length > 0) {
            Collections.addAll(queue, ts);
            sem.release(ts.length);
        }
    }
}
