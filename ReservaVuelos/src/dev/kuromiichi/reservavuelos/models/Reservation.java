package dev.kuromiichi.reservavuelos.models;

public class Reservation {
    private final int flightId;
    private final int clientId;
    private final int amount;

    public Reservation(int flightId, int clientId, int amount) {
        this.flightId = flightId;
        this.clientId = clientId;
        this.amount = amount;
    }

    public String toString() {
        return "Flight " + flightId + " reserved by client " + clientId
               + " for " + amount + " seats";
    }
}
