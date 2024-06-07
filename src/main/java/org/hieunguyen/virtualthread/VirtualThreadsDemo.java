package org.hieunguyen.virtualthread;

import java.util.ArrayList;
import java.util.List;

public class VirtualThreadsDemo {

    private static final int NUMBER_OF_VIRTUAL_THREADS = 3;

    public static void main(String[] args) throws InterruptedException {

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_VIRTUAL_THREADS; i++) {
            Thread virtualThread = Thread.ofVirtual().unstarted(new BlockTask());
            threads.add(virtualThread);
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

    }

    static class BlockTask implements Runnable {

        @Override
        public void run() {
            System.out.println("Inside block task (before): " + Thread.currentThread());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
            System.out.println("Inside block task (after): " + Thread.currentThread());
        }
    }
}
