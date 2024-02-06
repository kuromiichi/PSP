package dev.kuromiichi.reservavuelos;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class AdminClient {
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
                    Flight Reservation System - Admin
                    
                    Select an option:
                    1. List reservations
                    0. Exit
                    """;
                System.out.println(menu);

                String response = "";
                do {
                    option = sc.nextLine();
                    if (!option.equals("1") && option.equals("0")) {
                        System.out.println("Invalid option. Please try again.");
                        continue;
                    }
                    streamWriter.println(option);
                    response = streamReader.readLine();
                    if (response == null) throw new NullPointerException();
                } while (response.equals("error"));

                if (option.equals("1")) {
                    streamWriter.println("listReservations");
                    response = streamReader.readLine();
                    if (response == null) throw new NullPointerException();
                    System.out.println(response);
                } else {
                    streamWriter.println("0");
                    System.out.println("Exiting...");
                }

            } while (!option.equals("0"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Connection closed by server.");
            System.exit(1);
        }
    }
}
