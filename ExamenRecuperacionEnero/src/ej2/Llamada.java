package ej2;

public class Llamada {
    public enum TipoLlamada { INTERNA, EXTERNA }

    int planta;
    TipoLlamada tipo;

    public Llamada(int planta, TipoLlamada tipo) {
        this.planta = planta;
        this.tipo = tipo;
    }
}
