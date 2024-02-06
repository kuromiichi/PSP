package dev.kuromiichi.reservavuelos;

import dev.kuromiichi.reservavuelos.models.Flight;
import dev.kuromiichi.reservavuelos.models.Reservation;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
    public static void main(String[] args) {
        int port = 6119;
        ServerSocket serverSocket = null;

        Flight[] flights = new Flight[10];
        for (int i = 0; i < flights.length; i++) {
            flights[i] = new Flight();
        }

        Vector<Reservation> reservations = new Vector<>(10, 5);

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Could not start server on port " + port + ": " + e.getMessage());
            System.exit(1);
        }

        int nextClientId = 1;

        while (true) {
            try {
                System.out.println("Waiting for connections on port " + port + "...");
                Socket connection = serverSocket.accept();
                System.out.println("Accepted connection from "
                                   + connection.getInetAddress().getHostAddress());

                ConnectionHandler handler
                    = new ConnectionHandler(nextClientId++, connection, flights, reservations);
                new Thread(handler).start();
            } catch (IOException e) {
                System.out.println("Could not accept connection on port " + port + ": "
                                   + e.getMessage());
                System.exit(1);
            }
        }
    }
}
