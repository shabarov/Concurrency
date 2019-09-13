package ru.shabarov.concurrency.synchronizers;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierExample {

    public static void main(String[] args) {
        CyclicBarrier b = new CyclicBarrier(3, () -> System.out.println("Barrier is passed"));
        for (int i = 0; i < 3; i++) {
            new Thread(new MyThread(String.valueOf(i), b)).start();
        }
    }

    private static class MyThread implements Runnable {

        private String name;
        private CyclicBarrier barrier;

        public MyThread(String name, CyclicBarrier barrier) {
            this.name = name;
            this.barrier = barrier;
        }

        @Override
        public void run() {
            System.out.printf("Thread %s before barrier\n", name);
            try {
                barrier.await();
                System.out.printf("Thread %s after barrier\n", name);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

}
