package ru.shabarov.concurrency.waitnotify;

import java.util.Arrays;

public class Bank {
    private final double [] accounts;

    public Bank(int n, double initialBalance) {
        this.accounts = new double[n];
        Arrays.fill(accounts, initialBalance);
    }

    public synchronized void transfer(int from, int to, double amount) throws InterruptedException {
        while (this.accounts[from] < amount) {
            this.wait();
        }
        System.out.println("Thread.currentThread() = " + Thread.currentThread());
        this.accounts[from] -= amount;
        System.out.printf(" %10.2f from %d to %d", amount, from, to);
        accounts[to] += amount;
        System.out.printf(" Total balance: %10.2f%n", getTotalBalance());
        notifyAll();
    }

    public synchronized double getTotalBalance() {
        double sum = 0;
        for (double a : this.accounts) {
            sum += a;
        }
        return sum;
    }

    public int size() {
        return this.accounts.length;
    }
}
