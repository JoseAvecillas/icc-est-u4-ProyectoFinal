package controller;

import models.*;
import models.Nodo.TipoNodo;

import java.util.*;

public class LaberintoController {

    private Grafo grafo;
    private List<Nodo> nodos;
    private int contadorNodos = 1;

    //  Modos de interacción
    private boolean modoColocar = false;

    // Conexiones
    private boolean modoConectarBi = false;
    private boolean modoConectarDir = false;

    // Edición
    private boolean modoInicio = false;
    private boolean modoFin = false;
    private boolean modoBorrar = false;
    private boolean modoBloquear = false;

    private ResultadoPaso pasoActual;
    private int indiceVisitados;
    private int indiceCamino;
    private boolean faseCamino;
    private Boolean pasoEsBFS;

    public LaberintoController() {
        grafo = new Grafo();
        nodos = new ArrayList<>();
    }

    // MODOS
    public void activarModoColocar() {
        desactivarModos();
        modoColocar = true;
    }

    public void activarModoConectarBi() {
        desactivarModos();
        modoConectarBi = true;
    }

    public void activarModoConectarDir() {
        desactivarModos();
        modoConectarDir = true;
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

    public void activarModoBloquear() {
        desactivarModos();
        modoBloquear = true;
    }

    private void desactivarModos() {
        modoColocar = false;

        modoConectarBi = false;
        modoConectarDir = false;

        modoInicio = false;
        modoFin = false;
        modoBorrar = false;
        modoBloquear = false;
    }

    public boolean isModoColocar() { return modoColocar; }
    public boolean isModoConectarBi() { return modoConectarBi; }
    public boolean isModoConectarDir() { return modoConectarDir; }
    public boolean isModoInicio() { return modoInicio; }
    public boolean isModoFin() { return modoFin; }
    public boolean isModoBorrar() { return modoBorrar; }
    public boolean isModoBloquear() { return modoBloquear; }

    // CREAR NODOS
    public Nodo agregarNodo(int x, int y) {
        Nodo nuevo = new Nodo(x, y, "N" + contadorNodos++);
        nodos.add(nuevo);
        grafo.agregarNodo(nuevo);
        return nuevo;
    }

    // CONECTAR NODOS
    public void conectarBidireccional(Nodo a, Nodo b) {
        if (a != null && b != null && !a.equals(b)) {
            grafo.conectar(a, b);
        }
    }

    public void conectarDireccional(Nodo origen, Nodo destino) {
        if (origen != null && destino != null && !origen.equals(destino)) {
            grafo.conectarDirigido(origen, destino);
        }
    }

    // BORRAR NODO 
    public void eliminarNodo(Nodo nodo) {
        if (nodo == null) return;

        nodos.remove(nodo);
        grafo.eliminarNodo(nodo);

        limpiarEstadosBusqueda();
    }

    // ENCONTRAR NODO CERCANO
    public Nodo obtenerNodoCercano(int x, int y) {
        for (Nodo n : nodos) {
            double distancia = Math.hypot(n.getX() - x, n.getY() - y);
            if (distancia <= 12) {
                return n;
            }
        }
        return null;
    }

    // SELECCIONAR NODO (inicio/fin/bloquear)
    public void seleccionarNodo(Nodo nodo) {
        if (nodo == null) return;

        if ((modoInicio || modoFin) && nodo.getTipo() == TipoNodo.BLOQUEADO) return;

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
        else if (modoBloquear) {
            if (nodo.getTipo() == TipoNodo.BLOQUEADO) {
                nodo.setTipo(TipoNodo.NORMAL);
            } else {
                nodo.setTipo(TipoNodo.BLOQUEADO);
            }
        }
    }

    // ==============================
    // OBTENER DATOS PARA DIBUJO
    // ==============================
    public List<Nodo> getNodos() { return nodos; }

    public Map<Nodo, List<Nodo>> getConexiones() {
        return grafo.getListaAdyacencia();
    }

    // BÚSQUEDAS
    public ResultadoBusqueda buscarConBFS(Nodo inicio, Nodo fin) {
        return AlgoritmosBusqueda.bfs(grafo, inicio, fin);
    }

    public ResultadoBusqueda buscarConDFS(Nodo inicio, Nodo fin) {
        return AlgoritmosBusqueda.dfs(grafo, inicio, fin);
    }

    // PINTAR RESULTADOS
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

    // paso a paso 
    public ResultadoPaso ejecutarPasoAPaso(boolean usarBFS) {
        Nodo inicio = null;
        Nodo fin = null;

        for (Nodo n : nodos) {
            if (n.getTipo() == TipoNodo.INICIO) inicio = n;
            if (n.getTipo() == TipoNodo.FIN) fin = n;
        }

        if (inicio == null || fin == null)
            return new ResultadoPaso(new ArrayList<>(), new ArrayList<>());

        limpiarEstadosBusqueda();

        ResultadoBusqueda resultado;

        if (usarBFS)
            resultado = buscarConBFS(inicio, fin);
        else
            resultado = buscarConDFS(inicio, fin);

        return new ResultadoPaso(resultado.getVisitados(), resultado.getCamino());
    }

    public void iniciarPasoAPaso(boolean usarBFS) {
        pasoActual = ejecutarPasoAPaso(usarBFS);
        indiceVisitados = 0;
        indiceCamino = 0;
        faseCamino = false;
        pasoEsBFS = usarBFS;
    }

    public boolean avanzarPaso() {
        if (pasoActual == null) return false;

        if (!faseCamino) {
            while (indiceVisitados < pasoActual.getVisitados().size()) {
                Nodo n = pasoActual.getVisitados().get(indiceVisitados++);
                if (n.getTipo() == TipoNodo.NORMAL) {
                    n.setTipo(TipoNodo.VISITADO);
                    return true;
                }
            }
            faseCamino = true;
        }

        while (indiceCamino < pasoActual.getCamino().size()) {
            Nodo n = pasoActual.getCamino().get(indiceCamino++);
            if (n.getTipo() != TipoNodo.BLOQUEADO &&
                n.getTipo() != TipoNodo.INICIO &&
                n.getTipo() != TipoNodo.FIN) {
                n.setTipo(TipoNodo.CAMINO);
                return true;
            }
        }

        return false;
    }

    public boolean estaEnPasoAPaso() {
        return pasoActual != null;
    }

    public Boolean getPasoEsBFS() {
        return pasoEsBFS;
    }
}