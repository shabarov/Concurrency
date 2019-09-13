package ru.shabarov.concurrency.waitnotify;

/**
 * Example from 'Cay Horstmann 'Core Java Volume 1'
 */
public class BankConsumer {

    private static final int NACCOUNTS = 100;
//    private static final int NACCOUNTS = 10;
    private static final double INITIAL_BALANCE = 1000;
    private static final double MAX_AMOUNT = 1000;
    // This max amount value ends up with thread blocking because balance is not enough,
    // and threads wait until other thread increment the balance:
//    private static final double MAX_AMOUNT = 2 * INITIAL_BALANCE;
    private static final int DELAY = 10;

    public static void main(String[] args) {
        final Bank bank = new Bank(NACCOUNTS, INITIAL_BALANCE);
        for (int i = 0; i < NACCOUNTS; i++) {
            final int fromAccount = i;
            Runnable r = () -> {
                try {
                    while (true) {
                        int toAccount = (int) (bank.size() * Math.random());
                        double amount = MAX_AMOUNT * Math.random();
                        bank.transfer(fromAccount, toAccount, amount);
                        Thread.sleep((int) (DELAY * Math.random()));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            };
            Thread t = new Thread(r);
            t.start();
        }
    }
}
