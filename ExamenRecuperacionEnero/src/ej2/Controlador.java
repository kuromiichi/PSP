package ej2;

import java.util.Vector;

public class Controlador {
    int numPlantas;
    Vector<Vector<Llamada>> llamadas;

    public Controlador(int numPlantas) {
        this.numPlantas = numPlantas;
        llamadas = new Vector<>(numPlantas);
        for (int i = 0; i < numPlantas; i++) {
            llamadas.add(new Vector<>());
        }
    }

    public void crearLlamada(Llamada llamada) {
        int planta = llamada.planta;
        llamadas.get(planta).add(llamada);
        synchronized (this) {
            this.notifyAll();
        }
    }

    public boolean hayLlamadas() {
        for (int i = 0; i < numPlantas; i++) {
            if (!llamadas.get(i).isEmpty()) return true;
        }
        return false;
    }

    public boolean continuar(int planta, boolean sube) {
        if (sube) {
            for (int i = planta; i < numPlantas; i++) {
                if (hayLlamadasEnPlanta(i)) return true;
            }
        } else {
            for (int i = planta; i >= 0; i--) {
                if (hayLlamadasEnPlanta(i)) return true;
            }
        }
        return false;
    }

    public boolean hayLlamadasEnPlanta(int planta) {
        return !llamadas.get(planta).isEmpty();
    }

    public int anularLlamadas(int planta) {
        int numLlamadas = llamadas.get(planta).size();
        llamadas.get(planta).clear();
        return numLlamadas;
    }
}
