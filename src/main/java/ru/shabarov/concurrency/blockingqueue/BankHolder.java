package ru.shabarov.concurrency.blockingqueue;

import java.math.BigDecimal;

public class BankHolder implements Runnable {

    private AccountService accountService;

    private Bank bank;

    public BankHolder(Bank bank, AccountService accountService) {
        this.accountService = accountService;
        this.bank = bank;
    }

    @Override
    public void run() {
        try {
            while (true) {
                AccountAction transaction = this.accountService.getNextTransaction();
                String clientName = transaction.getClientName();
                BigDecimal amount = transaction.getAmount();
                switch (transaction.getAction()) {
                    case CHARGE:
                        this.bank.charge(clientName, amount);
                        break;
                    case WITHDRAW:
                        this.bank.withdraw(clientName, amount);
                        break;
                    default:
                        throw new RuntimeException("Unknown action");
                }
                System.out.printf("Client = %s, amount = %s\n", clientName, bank.getAmount(clientName));
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            System.out.println("Bank holder has been interrupted");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.out.println("Bank holder failure: " + e.getMessage());
        }
    }
}
