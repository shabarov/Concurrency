package ru.shabarov.concurrency.sync;

public class Ticker {

    public static final byte TICK = 0;
    public static final byte TAK = 1;

    // If volatile keyword skip, the threads will stuck in a wrong invisible state
    private volatile byte state;

    public Ticker(byte state) {
        this.state = state;
    }

    public void tick() {
        System.out.print("TICK-");
        this.state = TAK;
    }

    public void tack() {
        System.out.println("TAK");
        this.state = TICK;
    }

    public byte getState() {
        return state;
    }
}
