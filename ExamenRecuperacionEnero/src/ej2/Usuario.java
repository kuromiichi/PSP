package ej2;

import java.util.concurrent.ThreadLocalRandom;

public class Usuario implements Runnable {
    int numPlantas;
    Controlador controlador;

    public Usuario(int numPlantas, Controlador controlador) {
        this.numPlantas = numPlantas;
        this.controlador = controlador;
    }
    @Override
    public void run() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        try {
            Thread.sleep((1L + random.nextInt(100) * 1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        controlador.crearLlamada(new Llamada(
                random.nextInt(numPlantas),
                random.nextBoolean() ? Llamada.TipoLlamada.INTERNA : Llamada.TipoLlamada.EXTERNA
            )
        );
    }
}
