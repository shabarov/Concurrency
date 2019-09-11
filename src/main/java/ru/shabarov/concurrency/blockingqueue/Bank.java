package ru.shabarov.concurrency.blockingqueue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Bank {

    private Map<String, BigDecimal> accounts = new HashMap<>();

    public void withdraw(String clientName, BigDecimal amount) {
        if (this.accounts.containsKey(clientName)) {
            BigDecimal current = this.accounts.get(clientName);
            if (current.compareTo(amount) < 0) {
                throw new RuntimeException("Balance is not enough");
            }
            this.accounts.compute(clientName, (k, v) -> current.subtract(amount));
            System.out.printf("Withdraw: client = %s, amount = %f\n", clientName, amount.doubleValue());
        } else {
            throw new RuntimeException("No account found for = " + clientName);
        }
    }

    public void charge(String clientName, BigDecimal amount) {
        if (this.accounts.containsKey(clientName)) {
            this.accounts.compute(clientName, (k, v) -> v.add(amount));
            System.out.printf("Charge: client = %s, amount = %f\n", clientName, amount.doubleValue());
        } else {
            throw new RuntimeException("No account found for = " + clientName);
        }
    }

    public String getAmount(String clientName) {
        if (this.accounts.containsKey(clientName)) {
            return this.accounts.get(clientName).toString();
        } else {
            throw new RuntimeException("No account found for = " + clientName);
        }
    }

    public void addAccount(BankClient client, BigDecimal initAmount) {
        this.accounts.putIfAbsent(client.getAccountName(), initAmount);
    }
}
