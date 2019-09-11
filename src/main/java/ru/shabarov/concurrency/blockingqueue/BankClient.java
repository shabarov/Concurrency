package ru.shabarov.concurrency.blockingqueue;

import java.math.BigDecimal;

public class BankClient implements Runnable {

    private static final double AMOUNT_BASE = 5.0;

    private String name;

    private String accountName;

    private AccountService accountService;

    public BankClient(String name, String accountName, AccountService accountService) {
        this.name = name;
        this.accountName = accountName;
        this.accountService = accountService;
    }

    public String getName() {
        return name;
    }

    public String getAccountName() {
        return accountName;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                double amount = Math.floor(Math.random() * 2 * AMOUNT_BASE) - AMOUNT_BASE;
                boolean isWithdraw = Math.random() - 0.5 < 0;
                BigDecimal bigDecimalAmount = new BigDecimal(Math.abs(amount));
                if (isWithdraw) {
                    AccountAction action = new AccountAction(AccountAction.Action.WITHDRAW, bigDecimalAmount, this.accountName);
                    accountService.createTransaction(action);
                    //System.out.printf("Requested withdraw = %f from client = %s\n", amount, name);
                } else {
                    AccountAction action = new AccountAction(AccountAction.Action.CHARGE, bigDecimalAmount, this.accountName);
                    accountService.createTransaction(action);
                    //System.out.printf("Requested charge = %f from client = %s\n", amount, name);
                }
                Thread.sleep(10);
            }
            System.out.printf("Bank client = %s finished\n", this.name);
        } catch (InterruptedException e) {
            System.out.println("Client has been interrupted");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.out.println("Client failure: " + e.getMessage());
        }
    }
}
