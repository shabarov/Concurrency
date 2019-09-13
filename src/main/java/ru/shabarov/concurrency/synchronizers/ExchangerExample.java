package ru.shabarov.concurrency.synchronizers;

import java.util.concurrent.Exchanger;

public class ExchangerExample {

    public static void main(String [] args) {
        Exchanger<String> exchanger = new Exchanger<>();
        new Thread(new MyThread("One", exchanger)).start();
        new Thread(new MyThread("Two", exchanger)).start();
    }

    private static class MyThread implements Runnable {

        private String name;
        private Exchanger<String> exchanger;

        public MyThread(String name, Exchanger<String> exchanger) {
            this.name = name;
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            try {
                String other = exchanger.exchange(name);
                System.out.printf("Thread %s exchanged with %s\n", name, other);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
