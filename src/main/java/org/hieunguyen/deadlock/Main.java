package org.hieunguyen.deadlock;

public class Main {

    public static void main(String[] args) {
        Intersection intersection = new Intersection();
        Thread trainA = new Thread(new TrainA(intersection));
        Thread trainB = new Thread(new TrainB(intersection));
        trainA.start();
        trainB.start();

    }

    public static class TrainA implements Runnable {
        private final Intersection intersection;

        public TrainA(Intersection intersection) {
            this.intersection = intersection;
        }

        @Override
        public void run() {
            intersection.takeRoadA();
        }

    }

    public static class TrainB implements Runnable {
        private final Intersection intersection;

        public TrainB(Intersection intersection) {
            this.intersection = intersection;
        }

        @Override
        public void run() {
            intersection.takeRoadB();
        }

    }

    public static class Intersection {
        final Object roadA = new Object();
        final Object roadB = new Object();

        public void takeRoadA() {
            synchronized (roadA) {
                System.out.println("road A is blocked by thread: " + Thread.currentThread().getName());
                synchronized (roadB) {
                    System.out.println("Train can pass the intersection");
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        public void takeRoadB() {
            synchronized (roadB) {
                System.out.println("road B is blocked by thread: " + Thread.currentThread().getName());
                synchronized (roadA) {
                    System.out.println("Train can pass the intersection");
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
