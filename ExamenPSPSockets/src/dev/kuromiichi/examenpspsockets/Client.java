package dev.kuromiichi.examenpspsockets;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    // Se usa el protocolo TCP
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 12345;
        Socket connection = null;

        try {
            connection = new Socket(hostname, port);
        } catch (IOException e) {
            System.out.println("Error al conectar");
            System.out.println(e.getMessage());
        }

        try {
            BufferedReader reader =
                new BufferedReader(new InputStreamReader(connection.getInputStream()));
            PrintWriter writer =
                new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));

            // Mandar nombre al servidor
            String clientName = "";
            while (clientName.isBlank()) {
                System.out.println("Introduce tu nombre:");
                clientName = scanner.nextLine().trim();
            }

            writer.write(clientName);
            writer.flush();
            String response = reader.readLine();
            if (!response.equals("OK")) {
                System.out.println("Error al guardar nombre");
                return;
            }

            // Menú
            String option = "";
            while (!option.equals("0")) {
                System.out.println("""
                    MENÚ
                                        
                    1. Enviar mensaje
                    2. Leer mensajes recibidos
                    0. Salir
                                        
                    Elige una opción:
                    """);
                option = scanner.nextLine().trim();

                switch (option) {
                    case "1" -> sendMessage(writer, reader, clientName);
                    case "2" -> readMessages(writer, reader, clientName);
                    case "0" -> {
                        writer.write("0");
                        writer.flush();
                    }
                    default -> {}
                }
            }

            System.out.println("Cerrando conexión...");
            connection.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Envía un mensaje al usuario deseado.
     *
     * @param writer PrintWriter a utilizar
     * @param reader BufferedReader a utilizar
     */
    private static void sendMessage(PrintWriter writer, BufferedReader reader, String clientName) throws IOException {
        // Mandar petición al servidor
        writer.write("1");
        writer.flush();
        String response = reader.readLine();
        if (!response.equals("OK")) {
            System.out.println("Se ha producido un error");
            return;
        }

        String receiver = "";
        while (receiver.isBlank()) {
            System.out.println("Escribe el nombre del usuario al que quieres enviar el mensaje:");
            receiver = scanner.nextLine().trim();
        }

        // Mandar nombre al servidor
        writer.write(receiver);
        writer.flush();
        response = reader.readLine();
        if (!response.equals("OK")) {
            System.out.println("El usuario no existe");
            return;
        }

        // Escribir mensaje
        String message = "";
        while (message.isBlank()) {
            System.out.println("Escribe el mensaje:");
            message = scanner.nextLine().trim();
            if (message.isBlank()) {
                System.out.println("El mensaje no puede estar vacío");
            }
        }

        // Añadir nombre de cliente al mensaje
        message = clientName + "> " + message;

        writer.write(message);
        writer.flush();
        response = reader.readLine();
        if (!response.equals("OK")) {
            System.out.println("Se ha producido un error");
        } else {
            System.out.println("Mensaje enviado correctamente");
        }
        System.out.println();
    }

    /**
     * Lee los mensajes recibidos.
     *
     * @param writer     PrintWriter a utilizar
     * @param reader     BufferedReader a utilizar
     * @param clientName Nombre del usuario en este cliente
     */
    private static void readMessages(PrintWriter writer, BufferedReader reader,
                                     String clientName) throws IOException {
        // Mandar petición al servidor
        writer.write("2");
        writer.flush();
        String response = reader.readLine();
        if (!response.equals("OK")) {
            System.out.println("Se ha producido un error");
            return;
        }

        // Recibir mensajes del servidor
        writer.write(clientName);
        writer.flush();
        response = reader.readLine();
        if (response.equals("ERR")) {
            System.out.println("No hay mensajes recibidos");
        } else {
            System.out.println(response);
        }
    }
}
