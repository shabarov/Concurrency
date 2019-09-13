package ru.shabarov.concurrency.synchronizers;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class SynchronousQueueExample {

    public static void main(String[] args) {
        BlockingQueue<String> queue = new SynchronousQueue<>();
        new Thread(new Friend("Mike", queue, false)).start();
        new Thread(new Friend("John", queue, true)).start();
        new Thread(new Friend("Peter", queue, false)).start();
    }

    private static class Friend implements Runnable {
        private String name;
        private BlockingQueue<String> queue;
        private boolean birthdayBoy;

        public Friend(String name, BlockingQueue<String> queue, boolean birthdayBoy) {
            this.name = name;
            this.queue = queue;
            this.birthdayBoy = birthdayBoy;
        }

        @Override
        public void run() {
            try {
                if (birthdayBoy) {
                    String gift;
                    while (true) {
                        gift = queue.poll(1, TimeUnit.SECONDS);
                        if (gift == null) break;
                        System.out.printf("Friend %s gets a gift %s\n", name, gift);
                    }
                    System.out.printf("Friend %s gets no gifts\n", name);
                } else {
                    String gift = String.format("'Gift from %s'", name);
                    queue.put(gift);
                    System.out.printf("Friend %s gives a gift %s\n", name, gift);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
