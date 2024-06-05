package org.hieunguyen.threadmodel;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IOBoundApplication {
    private static final int NUMBER_OF_TASK = 1_000;

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Press enter to start");
        s.nextLine();
        System.out.printf("Running %d tasks...\n", NUMBER_OF_TASK);
        long start = System.currentTimeMillis();
        performTask();
        System.out.printf("Task took %.3f seconds\n", (System.currentTimeMillis() - start) / 1000.0);
    }

    private static void performTask() {
        try (ExecutorService executorService = Executors.newCachedThreadPool()) {

            for (int i = 0; i < NUMBER_OF_TASK; i++) {
                executorService.execute(() -> {
                    for (int i1 = 0; i1 < 100; i1++) {
                        blockingIoOperation();
                    }
                });
            }
        }
    }

    private static void blockingIoOperation() {
        System.out.println("Executing a blocking task from thread: " + Thread.currentThread());
        try {
            Thread.sleep(10L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
