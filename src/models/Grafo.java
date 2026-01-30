package models;

import java.util.*;

public class Grafo {

    private Map<Nodo, List<Nodo>> adyacencia = new HashMap<>();

    public void agregarNodo(Nodo nodo) {
        adyacencia.putIfAbsent(nodo, new ArrayList<>());
    }

    public void conectar(Nodo a, Nodo b) {
        adyacencia.get(a).add(b);
        adyacencia.get(b).add(a); // grafo no dirigido
    }

    public List<Nodo> getVecinos(Nodo nodo) {
        return adyacencia.getOrDefault(nodo, new ArrayList<>());
    }

    public Set<Nodo> getNodos() {
        return adyacencia.keySet();
    }

    // ==============================
    // 🔥 ESTE ES EL QUE FALTABA
    // ==============================
    public Map<Nodo, List<Nodo>> getListaAdyacencia() {
        return adyacencia;
    }
}