package dev.kuromiichi.transferenciasprodcons;

import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private static int nextId = 1;

    int id;
    int balance;
    ReentrantLock lock = new ReentrantLock();

    public BankAccount(int balance) {
        this.id = nextId++;
        this.balance = balance;
    }

    public void deposit() {
        balance += 100;
    }

    public void withdraw() {
        balance -= 100;
    }

    public int getId() {
        return id;
    }
}
