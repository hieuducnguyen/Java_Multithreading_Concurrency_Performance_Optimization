package org.hieunguyen.interthreadcommunication;

public class ObjectNotifySimpleCountDownLatch {
    private int count;

    public ObjectNotifySimpleCountDownLatch(int count) {
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
        synchronized (this) {
            while (count != 0) {
                wait();
            }
        }
    }

    /**
     * Decrements the count of the latch, releasing all waiting threads when the count reaches zero.
     * If the current count already equals zero then nothing happens.
     */
    public void countDown() {
        synchronized (this) {
            count = Math.max(count - 1, 0);
            if (count == 0) {
                notifyAll();
            }
        }
    }

    /**
     * Returns the current count.
     */
    public int getCount() {
        return count;
    }
}
