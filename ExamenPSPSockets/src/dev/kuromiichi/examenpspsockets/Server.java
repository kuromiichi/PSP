package dev.kuromiichi.examenpspsockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;

public class Server {
    // Se usa el protocolo TCP
    public static void main(String[] args) {
        int port = 12345;
        ServerSocket serverSocket;

        HashMap<String, Vector<String>> clients = new HashMap<>();

        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket connection = serverSocket.accept();
                System.out.println("Conexión aceptada");
                new Thread(new ConnectionHandler(connection, clients)).start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
