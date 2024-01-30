package ej1;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Ejercicio1 {
    public static void main(String[] args) {
        System.out.println("Ejercicio 1");
        System.out.println("Elige una opción:");
        System.out.println("1 - Abrir VS Code");
        System.out.println("2 - Ping IP");

        String input;
        Scanner sc = new Scanner(System.in);
        do {
            input = sc.nextLine();
            if (!input.equals("1") && !input.equals("2")) {
                System.out.println("Opción no válida");
            }
        } while (!input.equals("1") && !input.equals("2"));

        if (input.equals("1")) {
            abrirFirefox();
        } else {
            pingIp();
        }
    }

    private static void abrirFirefox() {
        try {
            Process firefox = new ProcessBuilder("firefox").start();
            boolean cerradoPorUsuario = firefox.waitFor(10L, TimeUnit.SECONDS);
            if (cerradoPorUsuario) {
                System.out.println("Firefox cerrado por el usuario");
            } else {
                firefox.destroy();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void pingIp() {
        String ip;
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce una IP para comprobar la conexión:");
        ip = sc.nextLine();
        try {
            Process ping = new ProcessBuilder("ping", "-c", "1", ip)
                .redirectOutput(new File("./src/ej1/salida.txt"))
                .redirectError(new File("./src/ej1/errores.txt"))
                .start();
            ping.waitFor();
            if (ping.exitValue() == 0) {
                System.out.println("Conexión con " + ip + ": OK");
            } else {
                System.out.println("Conexión con " + ip + ": ERROR");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (
            IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
