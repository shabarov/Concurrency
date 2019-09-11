package ru.shabarov.concurrency.sync;

public class Client2 implements Runnable {

    private final byte state;

    private final Ticker ticker;

    public Client2(byte state, Ticker ticker) {
        this.state = state;
        this.ticker = ticker;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 1000; i++) {
                synchronized (this.ticker) {
                    while (this.state != this.ticker.getState()) {
                        this.ticker.wait();
                    }
                    if (this.state == Ticker.TICK) {
                        this.ticker.tick();
                    } else {
                        this.ticker.tack();
                    }
                    this.ticker.notify();
                }
            }
        } catch (Exception e) {
            System.out.println("TickTacker interrupted");
            throw new RuntimeException(e);
        }
    }
}
