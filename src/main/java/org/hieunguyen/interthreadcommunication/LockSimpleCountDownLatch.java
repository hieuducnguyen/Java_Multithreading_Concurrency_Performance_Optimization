package org.hieunguyen.interthreadcommunication;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockSimpleCountDownLatch {
    private int count;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public LockSimpleCountDownLatch(int count) {
        this.count = count;
        if (count < 0) {
            throw new IllegalArgumentException("count cannot be negative");
        }
    }

    /**
     * Causes the current thread to wait until the latch has counted down to zero.
     * If the current count is already zero then this method returns immediately.
     */
    public void await() throws InterruptedException {
        lock.lock();
        try {
            while (count != 0) {
                condition.await();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Decrements the count of the latch, releasing all waiting threads when the count reaches zero.
     * If the current count already equals zero then nothing happens.
     */
    public void countDown() {
        lock.lock();
        try {
            count = Math.max(count - 1, 0);
            if (count == 0) {
                condition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns the current count.
     */
    public int getCount() {
        return count;
    }
}
