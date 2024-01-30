package dev.kuromiichi.transferenciasprodcons.transaction;

import dev.kuromiichi.transferenciasprodcons.BankAccount;

public class Transaction implements Runnable {
    private static final int AMOUNT = 100;
    private static int nextId = 1;

    int id;
    BankAccount from;
    BankAccount to;

    public Transaction(BankAccount from, BankAccount to) {
        this.id = nextId++;
        this.from = from;
        this.to = to;
    }

    @Override
    public void run() {
        if (from.getId() < to.getId()) {
            synchronized (from) {
                System.out.println("transaction " + id + " locked " + from.getId());
                synchronized (to) {
                    System.out.println("transaction " + id + " locked " + to.getId());
                    from.withdraw();
                    to.deposit();
                }
            }
            System.out.println("transaction " + id + " unlocked " + from.getId() + " and " + to.getId());
        } else {
            synchronized (to) {
                System.out.println("transaction " + id + " locked " + to.getId());
                synchronized (from) {
                    System.out.println("transaction " + id + " locked " + from.getId());
                    from.withdraw();
                    to.deposit();
                }
            }
            System.out.println("transaction " + id + " unlocked " + from.getId() + " and " + to.getId());
        }
    }
}
