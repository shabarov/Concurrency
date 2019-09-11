package ru.shabarov.concurrency.blockingqueue;

import java.math.BigDecimal;

public class AccountAction {
    private Action action;
    private BigDecimal amount;
    private String clientName;

    public AccountAction(Action action, BigDecimal amount, String clientName) {
        this.action = action;
        this.amount = amount;
        this.clientName = clientName;
    }

    public Action getAction() {
        return action;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getClientName() {
        return clientName;
    }

    public enum Action {
        WITHDRAW,
        CHARGE
    }
}
