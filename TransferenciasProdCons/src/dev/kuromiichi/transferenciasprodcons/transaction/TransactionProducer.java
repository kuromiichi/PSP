package dev.kuromiichi.transferenciasprodcons.transaction;

import dev.kuromiichi.transferenciasprodcons.BankAccount;

import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class TransactionProducer implements Runnable {
    private static int nextId = 1;
    private static int producedTransactions = 0;

    int id;
    Vector<Transaction> transactionBuffer;
    private int maxTransactions;
    private BankAccount[] accounts;

    public TransactionProducer(Vector<Transaction> transactionBuffer, int maxTransactions,
                               BankAccount[] accounts) {
        this.id = nextId++;
        this.transactionBuffer = transactionBuffer;
        this.maxTransactions = maxTransactions;
        this.accounts = accounts;
    }

    public static int getProducedTransactions() {
        return producedTransactions;
    }

    private synchronized boolean allTransactionsProduced() {
        return producedTransactions >= maxTransactions;
    }

    private synchronized void addProducedTransaction() {
        producedTransactions++;
    }

    @Override
    public void run() {
        try {
            while (!allTransactionsProduced()) {
                synchronized (transactionBuffer) {
                    while (transactionBuffer.size() >= transactionBuffer.capacity()) {
                        transactionBuffer.wait();
                    }
                }
                synchronized (this) {
                    if (!allTransactionsProduced()) {
                        ThreadLocalRandom random = ThreadLocalRandom.current();
                        int from = random.nextInt(accounts.length);
                        int to;
                        do {
                            to = random.nextInt(accounts.length);
                        } while (from == to);
                        Transaction transaction = new Transaction(accounts[from], accounts[to]);
                        transactionBuffer.add(transaction);
                        addProducedTransaction();
                        synchronized (transactionBuffer) {
                            System.out.println("producer " + id + " produced transaction " + transaction.id);
                            transactionBuffer.notifyAll();
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
