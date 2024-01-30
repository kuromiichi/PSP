package ej2;

public class Ascensor implements Runnable {
    int planta = 0;
    boolean sube = true;
    final Controlador controlador;

    public Ascensor(Controlador controlador) {
        this.controlador = controlador;
    }

    public void pararse() {
        System.out.println("Ascensor parado en planta " + planta);
        int numLlamadas = controlador.anularLlamadas(planta);
        System.out.println("Recogidas " + numLlamadas + " personas");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void subirPlanta() {
        sube = true;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        planta++;
        System.out.println("Ascensor ha subido a planta " + planta);
        if (controlador.hayLlamadasEnPlanta(planta)) {
            pararse();
        }
    }

    public void bajarPlanta() {
        sube = false;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        planta--;
        System.out.println("Ascensor ha bajado a planta " + planta);
        if (controlador.hayLlamadasEnPlanta(planta)) pararse();
    }

    public void moverse() {
        if (sube) subirPlanta();
        else bajarPlanta();
    }

    @Override
    public void run() {
        System.out.println("Ascensor empieza en planta " + planta);
        while (true) {
            try {
                synchronized (controlador) {
                    while (!controlador.hayLlamadas()) {
                        System.out.println("Ascensor en espera");
                        controlador.wait();
                    }
                    if (controlador.continuar(planta, sube)) moverse();
                    else {
                        sube = !sube;
                        moverse();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
