package models;

import java.util.List;

public class ResultadoPaso {

    private List<Nodo> visitados;
    private List<Nodo> camino;

    public ResultadoPaso(List<Nodo> visitados, List<Nodo> camino) {
        this.visitados = visitados;
        this.camino = camino;
    }

    public List<Nodo> getVisitados() {
        return visitados;
    }

    public List<Nodo> getCamino() {
        return camino;
    }
}