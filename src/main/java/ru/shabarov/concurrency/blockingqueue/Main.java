package ru.shabarov.concurrency.blockingqueue;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final String INITIAL_BALANCE = "50.0";

    public static void main(String [] args) {
        Bank bank = new Bank();

        AccountService accountService = new AccountService();
        BankHolder bankHolder = new BankHolder(bank, accountService);

        BankClient mike = new BankClient("Mike", "MikeAccount", accountService);
        bank.addAccount(mike, new BigDecimal(INITIAL_BALANCE));

        BankClient wife = new BankClient("Mike's wife", "MikeAccount", accountService);

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(mike);
        executorService.submit(wife);
        executorService.submit(bankHolder);
        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(3, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("-------------------------------------");
        System.out.printf("Final amount = %s", bank.getAmount("MikeAccount"));
    }
}
