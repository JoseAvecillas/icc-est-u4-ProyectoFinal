package models;

import java.util.Objects;

public class Nodo {

    private int x;
    private int y;
    private String id; 
    private TipoNodo tipo = TipoNodo.NORMAL;


    public Nodo(int x, int y, String id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return id + " (" + x + "," + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Nodo)) return false;
        Nodo nodo = (Nodo) o;
        return Objects.equals(id, nodo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public enum TipoNodo {
        NORMAL,
        INICIO,
        FIN,
        VISITADO,
        CAMINO
    }

    public TipoNodo getTipo() {
        return tipo;
    }

    public void setTipo(TipoNodo tipo) {
        this.tipo = tipo;
    }
}