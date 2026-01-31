package controller;

import models.*;
import models.Nodo.TipoNodo;

import java.util.*;

public class LaberintoController {

    private Grafo grafo;
    private List<Nodo> nodos;
    private int contadorNodos = 1;

    // 🔹 Modos de interacción
    private boolean modoColocar = false;
    private boolean modoConectar = false;
    private boolean modoInicio = false;
    private boolean modoFin = false;
    private boolean modoBorrar = false;

    public LaberintoController() {
        grafo = new Grafo();
        nodos = new ArrayList<>();
    }

    // ==============================
    // MODOS
    // ==============================
    public void activarModoColocar() {
        desactivarModos();
        modoColocar = true;
    }

    public void activarModoConectar() {
        desactivarModos();
        modoConectar = true;
    }

    public void activarModoInicio() {
        desactivarModos();
        modoInicio = true;
    }

    public void activarModoFin() {
        desactivarModos();
        modoFin = true;
    }

    public void activarModoBorrar() {
        desactivarModos();
        modoBorrar = true;
    }

    private void desactivarModos() {
        modoColocar = false;
        modoConectar = false;
        modoInicio = false;
        modoFin = false;
        modoBorrar = false;
    }

    public boolean isModoColocar() { return modoColocar; }
    public boolean isModoConectar() { return modoConectar; }
    public boolean isModoInicio() { return modoInicio; }
    public boolean isModoFin() { return modoFin; }
    public boolean isModoBorrar() { return modoBorrar; }

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
            grafo.conectar(a, b);
        }
    }

    // ==============================
    // ENCONTRAR NODO CERCANO
    // ==============================
    public Nodo obtenerNodoCercano(int x, int y) {
        for (Nodo n : nodos) {
            double distancia = Math.hypot(n.getX() - x, n.getY() - y);
            if (distancia <= 12) {
                return n;
            }
        }
        return null;
    }

    // ==============================
    // SELECCIONAR NODO (inicio/fin/borrar)
    // ==============================
    public void seleccionarNodo(Nodo nodo) {
        if (nodo == null) return;

        if (modoInicio) {
            for (Nodo n : nodos)
                if (n.getTipo() == TipoNodo.INICIO)
                    n.setTipo(TipoNodo.NORMAL);

            nodo.setTipo(TipoNodo.INICIO);
        }
        else if (modoFin) {
            for (Nodo n : nodos)
                if (n.getTipo() == TipoNodo.FIN)
                    n.setTipo(TipoNodo.NORMAL);

            nodo.setTipo(TipoNodo.FIN);
        }
        else if (modoBorrar) {
            nodo.setTipo(TipoNodo.NORMAL);
        }
    }

    // ==============================
    // OBTENER DATOS PARA DIBUJO
    // ==============================
    public List<Nodo> getNodos() { return nodos; }

    public Map<Nodo, List<Nodo>> getConexiones() {
        return grafo.getListaAdyacencia();
    }

    // ==============================
    // BÚSQUEDAS
    // ==============================
    public ResultadoBusqueda buscarConBFS(Nodo inicio, Nodo fin) {
        return AlgoritmosBusqueda.bfs(grafo, inicio, fin);
    }

    public ResultadoBusqueda buscarConDFS(Nodo inicio, Nodo fin) {
        return AlgoritmosBusqueda.dfs(grafo, inicio, fin);
    }

    // ==============================
    // PINTAR RESULTADOS
    // ==============================
    public void marcarResultado(ResultadoBusqueda resultado) {
        for (Nodo n : resultado.getVisitados()) {
            if (n.getTipo() == TipoNodo.NORMAL)
                n.setTipo(TipoNodo.VISITADO);
        }

        for (Nodo n : resultado.getCamino()) {
            n.setTipo(TipoNodo.CAMINO);
        }
    }

    public void limpiarEstadosBusqueda() {
        for (Nodo n : nodos) {
            if (n.getTipo() == TipoNodo.VISITADO || n.getTipo() == TipoNodo.CAMINO)
                n.setTipo(TipoNodo.NORMAL);
        }
    }

    public boolean hayInicioYFin() {
        boolean inicio = false, fin = false;
        for (Nodo n : nodos) {
            if (n.getTipo() == TipoNodo.INICIO) inicio = true;
            if (n.getTipo() == TipoNodo.FIN) fin = true;
        }
        return inicio && fin;
    }

    public void reiniciar() {
        grafo = new Grafo();
        nodos.clear();
        contadorNodos = 1;
    }

    // ==============================
    // ALTERNAR INICIO / FIN / NORMAL
    // ==============================
    public void alternarInicioFin(Nodo nodo) {
        if (nodo == null) return;

        switch (nodo.getTipo()) {

            case NORMAL:
                // Quitar inicio anterior
                for (Nodo n : nodos) {
                    if (n.getTipo() == TipoNodo.INICIO) {
                        n.setTipo(TipoNodo.NORMAL);
                    }
                }
                nodo.setTipo(TipoNodo.INICIO);
                break;

            case INICIO:
                // Quitar fin anterior
                for (Nodo n : nodos) {
                    if (n.getTipo() == TipoNodo.FIN) {
                        n.setTipo(TipoNodo.NORMAL);
                    }
                }
                nodo.setTipo(TipoNodo.FIN);
                break;

            case FIN:
                nodo.setTipo(TipoNodo.NORMAL);
                break;

            default:
                break;
        }
    }

    public List<Nodo> bfsPasoAPaso(Nodo inicio, Nodo fin) {
        Map<Nodo, Nodo> padre = new HashMap<>();
        Queue<Nodo> cola = new LinkedList<>();
        Set<Nodo> visitados = new HashSet<>();
        List<Nodo> ordenVisita = new ArrayList<>();

        cola.add(inicio);
        visitados.add(inicio);

        while (!cola.isEmpty()) {
            Nodo actual = cola.poll();
            ordenVisita.add(actual);

            if (actual.equals(fin)) break;

            for (Nodo vecino : grafo.getVecinos(actual)) {
                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    padre.put(vecino, actual);
                    cola.add(vecino);
                }
            }
        }

        return ordenVisita;
    }

    public List<Nodo> dfsPasoAPaso(Nodo inicio, Nodo fin) {
        List<Nodo> ordenVisita = new ArrayList<>();
        Set<Nodo> visitados = new HashSet<>();
        dfsRecursivoPaso(inicio, fin, visitados, ordenVisita);
        return ordenVisita;
    }

    private boolean dfsRecursivoPaso(Nodo actual, Nodo fin,
                                    Set<Nodo> visitados,
                                    List<Nodo> orden) {

        visitados.add(actual);
        orden.add(actual);

        if (actual.equals(fin)) return true;

        // 👇 USAR LAS CONEXIONES DEL GRAFO
        for (Nodo vecino : grafo.getVecinos(actual)) {
            if (!visitados.contains(vecino)) {
                if (dfsRecursivoPaso(vecino, fin, visitados, orden))
                    return true;
            }
        }
        return false;
    }

    public List<Nodo> ejecutarPasoAPaso(boolean usarBFS) {
        Nodo inicio = null;
        Nodo fin = null;

        for (Nodo n : nodos) {
            if (n.getTipo() == TipoNodo.INICIO) inicio = n;
            if (n.getTipo() == TipoNodo.FIN) fin = n;
        }

        if (inicio == null || fin == null) return new ArrayList<>();

        limpiarEstadosBusqueda();

        if (usarBFS)
            return bfsPasoAPaso(inicio, fin);
        else
            return dfsPasoAPaso(inicio, fin);
    }
}