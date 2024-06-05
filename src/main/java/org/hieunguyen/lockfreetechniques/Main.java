package org.hieunguyen.lockfreetechniques;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException {
//        IStack<Integer> stack = new LockFreeStack();
        IStack<Integer> stack = new LockStack();
        Random rand = new Random();
        List<Thread> threads = new ArrayList<>();
        int numPopThreads = 2;
        int numPushThreads = 2;
        for (int i = 0; i < numPopThreads; i++) {
            Thread popThread = new Thread(() -> {
                while (true) {
                    stack.pop();
                }
            });
            popThread.setDaemon(true);
            threads.add(popThread);
        }

        for (int i = 0; i < numPushThreads; i++) {
            Thread pushThread = new Thread(() -> {
                while (true) {
                    stack.push(rand.nextInt());
                }
            });
            pushThread.setDaemon(true);
            threads.add(pushThread);
        }

        for (Thread thread : threads) {
            thread.start();
        }
        System.out.println("Start counting");
        Thread.sleep(10000);
        long counter = stack.getCounter();
        System.out.printf("Counter: %,d in 10s%n", counter);
    }
}
