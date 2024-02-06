package dev.kuromiichi.reservavuelos.models;

import java.util.Random;

public class Flight {
    private static final Random random = new Random();
    private static final int MAX_SEATS = 50;
    private static int nextId = 1;

    private final int id;
    private int availableSeats;

    public Flight() {
        this.id = nextId++;
        this.availableSeats = random.nextInt(MAX_SEATS) + 1;
    }

    public String toString() {
        return "Flight " + id + " (" + availableSeats + " available seats)";
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public synchronized boolean reserveSeat(int amount) {
        if (availableSeats < amount) return false;

        availableSeats -= amount;
        return true;
    }
}
