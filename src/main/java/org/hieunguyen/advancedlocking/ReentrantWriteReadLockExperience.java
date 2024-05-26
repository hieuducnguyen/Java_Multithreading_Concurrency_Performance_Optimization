package org.hieunguyen.advancedlocking;

import java.util.NavigableMap;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantWriteReadLockExperience {
    public static final int HIGHEST_PRICE = 1000;


    public static void main(String[] args) {
        Database database = new Database();
        PriceReader priceReader = new PriceReader(database);
        PriceAdder priceAdder = new PriceAdder(database);
        PriceRemover priceRemover = new PriceRemover(database);

        priceAdder.setDaemon(true);
        priceRemover.setDaemon(true);
        priceAdder.start();
        priceRemover.start();

        long start = System.currentTimeMillis();
        priceReader.test();
        long end = System.currentTimeMillis();

        System.out.println("Duration: " + (end - start));
    }

    public static class PriceReader {
        private final Database database;

        public PriceReader(Database database) {
            this.database = database;
        }

        public void test() {
            for (long i = 0; i < 50000000L; i++) {
                Random random = new Random();
                int firstVal = random.nextInt(HIGHEST_PRICE);
                int secondVal = random.nextInt(HIGHEST_PRICE);
                int lowestVal = Math.min(firstVal, secondVal);
                int highestVal = Math.max(firstVal, secondVal);
                database.subMap(lowestVal, highestVal);
            }
        }
    }

    public static class PriceAdder extends Thread {
        private final Database database;

        public PriceAdder(Database database) {
            this.database = database;
        }

        @Override
        public void run() {
            Random random = new Random();
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                database.addPrice(random.nextInt(HIGHEST_PRICE));
            }
        }
    }

    public static class PriceRemover extends Thread {
        private final Database database;

        public PriceRemover(Database database) {
            this.database = database;
        }

        @Override
        public void run() {
            Random random = new Random();
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                database.remove(random.nextInt(HIGHEST_PRICE));
            }
        }
    }

    private static class Database {
        private final TreeMap<Integer, Integer> pricesFrequency = new TreeMap<>();
        private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

        public void addPrice(Integer price) {
            writeLock.lock();
            try {
                Optional.ofNullable(pricesFrequency.get(price))
                        .ifPresentOrElse(frequency -> {
                                    pricesFrequency.put(price, ++frequency);
                                },
                                () -> pricesFrequency.put(price, 1));
            } finally {
                writeLock.unlock();
            }
        }

        public void remove(Integer price) {
            writeLock.lock();
            try {
                Integer frequency = pricesFrequency.getOrDefault(price, 0);
                if (frequency > 1) {
                    pricesFrequency.put(price, --frequency);
                } else {
                    pricesFrequency.remove(price);
                }
            } finally {
                writeLock.unlock();
            }
        }

        public NavigableMap<Integer, Integer> subMap(Integer lowestPrice, Integer highestPrice) {
            readLock.lock();
            try {
                Integer ceilingKey = pricesFrequency.ceilingKey(lowestPrice);
                Integer floorKey = pricesFrequency.floorKey(highestPrice);
                if (ceilingKey != null && floorKey != null && ceilingKey <= floorKey) {
                    return pricesFrequency.subMap(ceilingKey, true, floorKey, true);
                } else {
                    return null;
                }
            } finally {
                readLock.unlock();
            }

        }
    }
}
