package ru.shabarov.concurrency.synchronizers;

import java.util.concurrent.Semaphore;

public class SemaphoreExample {

    public static void main(String[] args) {
        final int CASH_DESKS = 2;
        final int BUYERS = 7;
        Semaphore semaphore = new Semaphore(CASH_DESKS);
        for (int i = 0; i < BUYERS; i++) {
            new Thread(new Buyer("Buyer" + i, semaphore)).start();
        }

    }

    private static class Buyer implements Runnable {

        private String name;
        private Semaphore semaphore;

        public Buyer(String name, Semaphore semaphore) {
            this.name = name;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                System.out.printf("Buyer %s wait for free cash desk\n", name);
                semaphore.acquire();
                System.out.printf("Buyer %s is purchasing...\n", name);
                Thread.sleep((long) (1000 * Math.random()));
                System.out.printf("Buyer %s leave cash desks\n", name);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }

    }
}
