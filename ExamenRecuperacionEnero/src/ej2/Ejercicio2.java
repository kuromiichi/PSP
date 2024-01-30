package ej2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;

public class Ejercicio2 {
    private static final int NUM_PLANTAS = 10;
    private static final int NUM_USUARIOS = 50;
    public static void main(String[] args) throws InterruptedException {
        Controlador controlador = new Controlador(NUM_PLANTAS);
        Ascensor ascensor = new Ascensor(controlador);

        new Thread(ascensor).start();

        for (int i = 0; i < NUM_USUARIOS; i++) {
            Thread t = new Thread(new Usuario(NUM_PLANTAS, controlador));
            t.start();
        }
    }
}
