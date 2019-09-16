package ru.shabarov.concurrency.sync;

public class Counter {
    private int counter = 0;
    private synchronized void increment() { counter++;}
    private synchronized int getCounter() {return counter;}

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();
        for (int i = 0; i < 10000; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    counter.increment();
                }
            }).start();
        }
        Thread.sleep(3000);
        System.out.println(counter.getCounter());
    }
}
