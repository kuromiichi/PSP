package dev.kuromiichi.reservavuelos;

import dev.kuromiichi.reservavuelos.models.Flight;
import dev.kuromiichi.reservavuelos.models.Reservation;

import java.io.*;
import java.net.Socket;
import java.util.Vector;

public class ConnectionHandler implements Runnable {
    private final int id;
    private final Socket connection;
    private final Flight[] flights;
    private final Vector<Reservation> reservations;

    public ConnectionHandler(int id, Socket connection, Flight[] flights,
                             Vector<Reservation> reservations) {
        this.id = id;
        this.connection = connection;
        this.flights = flights;
        this.reservations = reservations;
    }

    @Override
    public void run() {
        try {
            BufferedReader streamReader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
            PrintWriter streamWriter = new PrintWriter(
                new OutputStreamWriter(connection.getOutputStream()), true);

            while (true) {
                String request = streamReader.readLine();
                if (request == null) throw new NullPointerException();
                switch (request) {
                    case "1" -> listFlights(streamWriter);
                    case "2" -> reserveSeats(streamReader, streamWriter);
                    case "0" -> System.exit(0);
                    case "listReservations" -> listReservations(streamWriter);
                    default -> streamWriter.println("error");
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (NullPointerException e) {
            System.out.println("Connection closed by client.");
            System.exit(1);
        }
    }

    private void listFlights(PrintWriter streamWriter) {
        StringBuilder sb = new StringBuilder();
        for (Flight flight : flights) {
            sb.append(flight.toString()).append("\n");
        }
        streamWriter.println(sb);
    }

    private void reserveSeats(BufferedReader streamReader, PrintWriter streamWriter) {
        int flightId = getFlightId(streamReader, streamWriter);
        if (flightId < 0) return;

        int amount = getSeatAmount(streamReader, streamWriter, flightId);
        if (amount < 0) return;

        reservations.add(new Reservation(flightId, id, amount));
        streamWriter.println("Reserved " + amount + " seats for flight " + flightId);
    }

    private int getFlightId(BufferedReader streamReader, PrintWriter streamWriter) {
        int flightId = 0;
        do {
            try {
                boolean availableFlights = false;
                for (Flight flight : flights) {
                    if (flight.getAvailableSeats() > 0) {
                        availableFlights = true;
                        break;
                    }
                }
                if (availableFlights) {
                    streamWriter.println("OK");
                } else {
                    streamWriter.println("error1");
                    return -1;
                }

                String input = streamReader.readLine();
                if (input == null) throw new NullPointerException();
                flightId = Integer.parseInt(input);

                if (flightId < 1 || flightId > flights.length) {
                    streamWriter.println("error2");
                    flightId = 0;
                }

                if (flights[flightId - 1].getAvailableSeats() == 0) {
                    streamWriter.println("error3");
                    flightId = 0;
                }
            } catch (NumberFormatException e) {
                streamWriter.println("error2");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (NullPointerException e) {
                System.out.println("Connection closed by client.");
                return -1;
            }
        } while (flightId < 1 || flightId > flights.length);
        streamWriter.println("OK");
        return flightId;
    }

    private Integer getSeatAmount(BufferedReader streamReader, PrintWriter streamWriter,
                                  Integer flightId) {
        int amount = 0;
        boolean success;
        do {
            do {
                try {
                    if (flights[flightId - 1].getAvailableSeats() == 0) {
                        streamWriter.println("error1");
                        return -1;
                    } else {
                        streamWriter.println("OK");
                    }

                    String input = streamReader.readLine();
                    if (input == null) throw new NullPointerException();
                    amount = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    streamWriter.println("error2");
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                } catch (NullPointerException e) {
                    System.out.println("Connection closed by client.");
                    return -1;
                }
            } while (amount < 1);
            success = flights[flightId - 1].reserveSeat(amount);
            if (!success) {
                streamWriter.println("error3");
            }
        } while (!success);
        return amount;
    }

    private void listReservations(PrintWriter streamWriter) {
        StringBuilder sb = new StringBuilder();
        for (Reservation reservation : reservations) {
            sb.append(reservation.toString()).append("\n");
        }
        streamWriter.println(sb);
    }
}
