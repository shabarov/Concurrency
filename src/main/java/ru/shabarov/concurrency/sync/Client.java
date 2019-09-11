package ru.shabarov.concurrency.sync;

public class Client implements Runnable {

    private final byte state;

    private final Ticker ticker;

    public Client(byte state, Ticker ticker) {
        this.state = state;
        this.ticker = ticker;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 1000; i++) {
                while (this.state != this.ticker.getState()) {
                    //Thread.sleep(1);
                }
                if (this.ticker.getState() == Ticker.TICK) {
                    this.ticker.tick();
                } else {
                    this.ticker.tack();
                }
            }
        } catch (Exception e) {
            System.out.println("TickTacker interrupted");
            throw new RuntimeException(e);
        }
    }
}
