package ru.shabarov.concurrency.synchronizers;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchExample {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        for (int i = 0; i < 3; i++) {
            new Thread(new MyThread(String.valueOf(i), latch)).start();
        }

        Thread.sleep(100);

        System.out.print("Say: three! ");
        latch.countDown();
        System.out.print("two! ");
        latch.countDown();
        System.out.println("one! Go!");
        latch.countDown();
    }

    private static class MyThread implements Runnable {

        private String name;
        private CountDownLatch latch;

        public MyThread(String name, CountDownLatch latch) {
            this.name = name;
            this.latch = latch;
        }

        @Override
        public void run() {
            System.out.printf("Thread %s before latch\n", name);
            try {
                latch.await();
                System.out.printf("Thread %s after latch\n", name);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
