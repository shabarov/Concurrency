package ru.shabarov.concurrency.blockingqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AccountService {

//    private BlockingQueue<AccountAction> queue = new LinkedBlockingQueue<>();
//    private BlockingQueue<AccountAction> queue = new MyBlockingQueue<>();
    private BlockingQueue<AccountAction> queue = new MyBlockingQueue2<>();

    public void createTransaction(AccountAction action) throws InterruptedException {
        this.queue.put(action);
    }

    public AccountAction getNextTransaction() throws InterruptedException {
        return this.queue.take();
    }
}
