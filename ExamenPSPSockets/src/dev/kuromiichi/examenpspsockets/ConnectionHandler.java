package dev.kuromiichi.examenpspsockets;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;
import java.util.stream.Collectors;

public class ConnectionHandler implements Runnable {
    static HashMap<String, Vector<String>> clients;
    Socket connection;

    public ConnectionHandler(Socket connection, HashMap<String, Vector<String>> clients) {
        this.connection = connection;
        this.clients = clients;
    }

    /**
     * Envía un mensaje al usuario deseado.
     *
     * @param writer PrintWriter a utilizar
     * @param reader BufferedReader a utilizar
     */
    private static void sendMessage(PrintWriter writer, BufferedReader reader) throws IOException {
        // Enviar estado OK
        writer.write("OK");
        writer.flush();

        // Recibir nombre del destinatario
        String receiver = reader.readLine();
        if (!clients.containsKey(receiver)) {
            writer.write("ERR");
            writer.flush();
            return;
        }
        writer.write("OK");
        writer.flush();

        // Recibir el mensaje
        String message = reader.readLine();
        clients.get(receiver).add(message);
        writer.write("OK");
        writer.flush();
    }

    private static void readMessages(PrintWriter writer, BufferedReader reader) throws IOException {
        // Enviar estado OK
        writer.write("OK");
        writer.flush();

        // Enviar mensajes (o error si no hay)
        String clientName = reader.readLine();
        if (!clients.containsKey(clientName)) {
            writer.write("ERR");
            writer.flush();
        } else if (clients.get(clientName).isEmpty()) {
            writer.write("ERR");
            writer.flush();
        } else {
            StringBuilder messages = new StringBuilder();
            Vector<String> buffer = clients.get(clientName);

            for (String s : buffer) {
                messages.append(s).append("\n");
            }

            writer.write(messages.toString());
            writer.flush();
        }
    }

    @Override
    public void run() {
        System.out.println("Creado handler para nueva conexión");
        try {
            BufferedReader reader =
                new BufferedReader(new InputStreamReader(connection.getInputStream()));
            PrintWriter writer =
                new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));

            // Guardar nombre del cliente y crear buffer de mensajes
            String clientName = reader.readLine();
            System.out.println("Recibido cliente: " + clientName);
            clients.put(clientName, new Vector<>());
            writer.write("OK");
            writer.flush();
            System.out.println("Guardado cliente: " + clientName);

            // Esperar petición del usuario (1 o 2 para mandar o recibir)
            String option = "";
            while (!option.equals("0")) {
                option = reader.readLine();
                switch (option) {
                    case "1" -> sendMessage(writer, reader);
                    case "2" -> readMessages(writer, reader);
                    default -> {}
                }
            }

            System.out.println("Cerrando conexión...");
            connection.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Un cliente se ha desconectado de forma inesperada");
        }
    }
}
