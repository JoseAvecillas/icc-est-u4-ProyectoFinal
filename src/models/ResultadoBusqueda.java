package models;

import java.util.List;

public class ResultadoBusqueda {

    private List<Nodo> visitados;
    private List<Nodo> camino;

    public ResultadoBusqueda(List<Nodo> visitados, List<Nodo> camino) {
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