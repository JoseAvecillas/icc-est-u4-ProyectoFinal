package controller;

import models.*;

import java.util.*;

public class LaberintoController {

    private Grafo grafo;
    private List<Nodo> nodos;
    private int contadorNodos = 1;

    // 🔹 Modos de interacción
    private boolean modoColocar = false;
    private boolean modoConectar = false;

    public LaberintoController() {
        grafo = new Grafo();
        nodos = new ArrayList<>();
    }

    // ==============================
    // MODOS
    // ==============================
    public void activarModoColocar() {
        modoColocar = true;
        modoConectar = false;
    }

    public void activarModoConectar() {
        modoConectar = true;
        modoColocar = false;
    }

    public boolean isModoColocar() {
        return modoColocar;
    }

    public boolean isModoConectar() {
        return modoConectar;
    }

    // ==============================
    // CREAR NODOS
    // ==============================
    public Nodo agregarNodo(int x, int y) {
        Nodo nuevo = new Nodo(x, y, "N" + contadorNodos++);
        nodos.add(nuevo);
        grafo.agregarNodo(nuevo);
        return nuevo;
    }

    // ==============================
    // CONECTAR NODOS
    // ==============================
    public void conectarNodos(Nodo a, Nodo b) {
        if (a != null && b != null && !a.equals(b)) {
            grafo.conectar(a, b); // grafo no dirigido
        }
    }

    // ==============================
    // ENCONTRAR NODO CERCANO (clic)
    // ==============================
    public Nodo obtenerNodoCercano(int x, int y) {
        for (Nodo n : nodos) {
            double distancia = Math.hypot(n.getX() - x, n.getY() - y);
            if (distancia <= 10) { // radio del nodo
                return n;
            }
        }
        return null;
    }

    // ==============================
    // OBTENER NODOS
    // ==============================
    public List<Nodo> getNodos() {
        return nodos;
    }

    // ==============================
    // OBTENER CONEXIONES (para dibujar)
    // ==============================
    public Map<Nodo, List<Nodo>> getConexiones() {
        return grafo.getListaAdyacencia();
    }

    // ==============================
    // BFS
    // ==============================
    public ResultadoBusqueda buscarConBFS(Nodo inicio, Nodo fin) {
        return AlgoritmosBusqueda.bfs(grafo, inicio, fin);
    }

    // ==============================
    // DFS
    // ==============================
    public ResultadoBusqueda buscarConDFS(Nodo inicio, Nodo fin) {
        return AlgoritmosBusqueda.dfs(grafo, inicio, fin);
    }

    // ==============================
    // LIMPIAR Todo
    // ==============================
    public void reiniciar() {
        grafo = new Grafo();
        nodos.clear();
        contadorNodos = 1;
    }
}