package ru.shabarov.concurrency.sync;

public class Main {

    public static void main(String[] args) {
        Ticker ticker = new Ticker(Ticker.TICK);

        Thread tick = new Thread(new Client2(Ticker.TICK, ticker));
        Thread tak = new Thread(new Client2(Ticker.TAK, ticker));

        tick.start();
        tak.start();
    }

}
