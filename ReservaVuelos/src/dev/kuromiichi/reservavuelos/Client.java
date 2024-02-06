package dev.kuromiichi.reservavuelos;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 6119;

        Socket connection = null;
        try {
            connection = new Socket(hostname, port);
            System.out.println("Connected to " + hostname + ":" + port + "\n");
        } catch (IOException e) {
            System.out.println("Could not connect to " + hostname + ":" + port);
        }
        if (connection == null) return;

        try {
            BufferedReader streamReader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
            PrintWriter streamWriter = new PrintWriter(
                new OutputStreamWriter(connection.getOutputStream()), true);

            String option;
            do {
                String menu = """
                    Flight Reservation System

                    Select an option:
                    1. List flights and available seats
                    2. Reserve seats
                    0. Exit
                    """;
                System.out.println(menu);

                String response = "";
                do {
                    option = sc.nextLine();
                    if (!option.equals("listReservations")) {
                        streamWriter.println(option);
                        response = streamReader.readLine();
                        if (response == null) throw new NullPointerException();
                    }
                    if (response.equals("error") || option.equals("listReservations")) {
                        System.out.println("Invalid option. Please try again.");
                    }
                } while (response.equals("error") || option.equals("listReservations"));

                switch (option) {
                    case "1" -> {
                        streamWriter.println("1");
                        listFlights(streamReader);
                    }
                    case "2" -> {
                        streamWriter.println("2");
                        reserveSeats(streamReader, streamWriter);
                    }
                    case "0" -> {
                        streamWriter.println("0");
                        System.out.println("Exiting...");
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } while (!option.equals("0"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Connection closed by server.");
            System.exit(1);
        }
    }

    private static void listFlights(BufferedReader streamReader) {
        try {
            String response = streamReader.readLine();
            if (response == null) throw new NullPointerException();
            System.out.println(response);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Connection closed by server.");
        }
    }

    private static void reserveSeats(BufferedReader streamReader, PrintWriter streamWriter) {
        try {
            String response;
            while ((response = streamReader.readLine()).equals("OK")) {
                System.out.println("Enter flight ID:");
                streamWriter.println(sc.nextLine());
                response = streamReader.readLine();
                if (response.equals("error2")) {
                    System.out.println("Invalid flight ID. Please try again.");
                } else if (response.equals("error3")) {
                    System.out.println("Flight is fully booked. Please try again.");
                }
            }
            if (response.equals("error1")) {
                System.out.println("No available flights.");
            }

            while ((response = streamReader.readLine()).equals("OK")) {
                System.out.println("Enter seat amount:");
                streamWriter.println(sc.nextLine());
                response = streamReader.readLine();
                if (response.equals("error2")) {
                    System.out.println("Invalid seat amount. Please try again.");
                } else if (response.equals("error3")) {
                    System.out.println("Not enough seats available. Please try again.");
                }
            }
            if (response.equals("error1")) {
                System.out.println("Flight is fully booked.");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Connection closed by server.");
        }
    }
}
