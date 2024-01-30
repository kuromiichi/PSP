package dev.kuromiichi.transferenciasprodcons;

import dev.kuromiichi.transferenciasprodcons.transaction.Transaction;
import dev.kuromiichi.transferenciasprodcons.transaction.TransactionConsumer;
import dev.kuromiichi.transferenciasprodcons.transaction.TransactionProducer;

import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int INITIAL_BALANCE = 100;
    private static final int MAX_ACCOUNTS = 100;
    private static final int MAX_TRANSACTIONS = 200;
    private static final int NUM_TRANSACTIONS = 5_000_000;
    private static final int NUM_PRODUCERS = 10;
    private static final int NUM_CONSUMERS = 3;

    public static void main(String[] args) throws InterruptedException {
        BankAccount[] accounts = new BankAccount[MAX_ACCOUNTS];
        for (int i = 0; i < MAX_ACCOUNTS; i++) {
            accounts[i] = new BankAccount(INITIAL_BALANCE);
        }

        Vector<Transaction> transactions = new Vector<>(MAX_TRANSACTIONS);
        ExecutorService es = Executors.newFixedThreadPool(NUM_PRODUCERS + NUM_CONSUMERS);

        TransactionProducer[] producers = new TransactionProducer[NUM_PRODUCERS];
        for (int i = 0; i < NUM_PRODUCERS; i++) {
            producers[i] = new TransactionProducer(transactions, NUM_TRANSACTIONS, accounts);
            es.execute(producers[i]);
        }

        es.shutdown();
        boolean finished = es.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS);
        if (finished) {
            System.out.println("All transactions produced");
            System.out.println(TransactionProducer.getProducedTransactions());
        } else {
            System.out.println("Timed out waiting for transactions to be produced");
        }
    }
}
