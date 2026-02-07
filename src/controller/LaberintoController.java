package controller;

import models.*;
import models.Nodo.TipoNodo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import controller.LaberintoController.RegistroBusqueda;

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
    private boolean modoEliminarConexion = false;

    private ResultadoPaso pasoActual;
    private int indiceVisitados;
    private int indiceCamino;
    private boolean faseCamino;
    private Boolean pasoEsBFS;

    private final List<RegistroBusqueda> historial = new ArrayList<>();


    // Informacion
    private String ultimaConexion = null;
    private String ultimoMetodo = null;
    private long ultimoTiempoNanos = -1;
    private boolean ultimoEncontroCamino = false;

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

    public void activarModoEliminarConexion() {
        desactivarModos();
        modoEliminarConexion = true;
    }

    private void desactivarModos() {
        modoColocar = false;

        modoConectarBi = false;
        modoConectarDir = false;

        modoInicio = false;
        modoFin = false;
        modoBorrar = false;
        modoBloquear = false;
        modoEliminarConexion = false;
    }

    public boolean isModoColocar() { return modoColocar; }
    public boolean isModoConectarBi() { return modoConectarBi; }
    public boolean isModoConectarDir() { return modoConectarDir; }
    public boolean isModoInicio() { return modoInicio; }
    public boolean isModoFin() { return modoFin; }
    public boolean isModoBorrar() { return modoBorrar; }
    public boolean isModoBloquear() { return modoBloquear; }
    public boolean isModoEliminarConexion() { return modoEliminarConexion; }
    public String getUltimaConexion() { return ultimaConexion; }
    public String getUltimoMetodo() { return ultimoMetodo; }
    public long getUltimoTiempoNanos() { return ultimoTiempoNanos; }
    public boolean getUltimoEncontroCamino() { return ultimoEncontroCamino; }

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

    public void eliminarConexion(Nodo a, Nodo b) {
        if (a == null || b == null || a.equals(b)) return;

        grafo.eliminarConexion(a, b);
        grafo.eliminarConexion(b, a);

        limpiarEstadosBusqueda();
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

    public void registrarUltimaBusqueda(Nodo inicio, Nodo fin, String metodo, long tiempoNanos, boolean encontroCamino) {
        if (inicio == null || fin == null) return;
        ultimaConexion = inicio.getId() + " -> " + fin.getId();
        ultimoMetodo = metodo;
        ultimoTiempoNanos = tiempoNanos;
        ultimoEncontroCamino = encontroCamino;

        historial.add(new RegistroBusqueda(
                ultimaConexion,
                ultimoMetodo,
                getUltimoTiempoFormateado(),
                ultimoEncontroCamino ? "Sí" : "No"
        ));
    }

    public boolean hayUltimaBusqueda() {
        return ultimaConexion != null && ultimoMetodo != null && ultimoTiempoNanos >= 0;
    }

    public String getResumenUltimaBusqueda() {
        if (!hayUltimaBusqueda()) return "Aún no hay búsquedas registradas.";

        String tiempoStr;
        if (ultimoTiempoNanos < 1_000) tiempoStr = ultimoTiempoNanos + " ns";
        else if (ultimoTiempoNanos < 1_000_000) tiempoStr = (ultimoTiempoNanos / 1_000.0) + " µs";
        else if (ultimoTiempoNanos < 1_000_000_000) tiempoStr = (ultimoTiempoNanos / 1_000_000.0) + " ms";
        else tiempoStr = (ultimoTiempoNanos / 1_000_000_000.0) + " s";

        return "Conexion: " + ultimaConexion + "\n"
            + "Metodo: " + ultimoMetodo + "\n"
            + "Tiempo: " + tiempoStr + "\n"
            + "Encontro camino: " + (ultimoEncontroCamino ? "Si" : "No");
    }

    public String getUltimoTiempoFormateado() {
        if (ultimoTiempoNanos < 0) return "-";
        if (ultimoTiempoNanos < 1_000) return ultimoTiempoNanos + " ns";
        if (ultimoTiempoNanos < 1_000_000) return (ultimoTiempoNanos / 1_000.0) + " µs";
        if (ultimoTiempoNanos < 1_000_000_000) return (ultimoTiempoNanos / 1_000_000.0) + " ms";
        return (ultimoTiempoNanos / 1_000_000_000.0) + " s";
    }

    public static class RegistroBusqueda {
        public final String conexion;
        public final String metodo;
        public final String tiempo;
        public final String encontro;

        public RegistroBusqueda(String conexion, String metodo, String tiempo, String encontro) {
            this.conexion = conexion;
            this.metodo = metodo;
            this.tiempo = tiempo;
            this.encontro = encontro;
        }
    }

    public List<RegistroBusqueda> getHistorial() {
        return new ArrayList<>(historial);
    }

    // Cargar y Guardar
    public void guardarConfiguracion(File file) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {

            pw.println("NODES");
            for (Nodo n : nodos) {
                pw.println(n.getId() + ";" + n.getX() + ";" + n.getY() + ";" + n.getTipo().name());
            }

            pw.println("EDGES");
            for (Map.Entry<Nodo, List<Nodo>> entry : grafo.getListaAdyacencia().entrySet()) {
                Nodo origen = entry.getKey();
                for (Nodo destino : entry.getValue()) {
                    pw.println(origen.getId() + "->" + destino.getId());
                }
            }
        }
    }


    public void cargarConfiguracion(File file) throws IOException {
        reiniciar();
        Map<String, Nodo> porId = new HashMap<>();
        boolean leyendoNodos = false;
        boolean leyendoEdges = false;
        int maxNum = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.equals("NODES")) {
                    leyendoNodos = true;
                    leyendoEdges = false;
                    continue;
                }
                if (line.equals("EDGES")) {
                    leyendoNodos = false;
                    leyendoEdges = true;
                    continue;
                }

                if (leyendoNodos) {
                    String[] parts = line.split(";");
                    if (parts.length != 4) continue;

                    String id = parts[0];
                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    Nodo.TipoNodo tipo = Nodo.TipoNodo.valueOf(parts[3]);

                    Nodo n = new Nodo(x, y, id);
                    n.setTipo(tipo);

                    nodos.add(n);
                    grafo.agregarNodo(n);
                    porId.put(id, n);

                    if (id.startsWith("N")) {
                        try {
                            int num = Integer.parseInt(id.substring(1));
                            if (num > maxNum) maxNum = num;
                        } catch (Exception ignored) {}
                    }
                }

                if (leyendoEdges) {
                    String[] parts = line.split("->");
                    if (parts.length != 2) continue;

                    Nodo origen = porId.get(parts[0]);
                    Nodo destino = porId.get(parts[1]);
                    if (origen != null && destino != null) {
                        grafo.conectarDirigido(origen, destino);
                    }
                }
            }
        }

        contadorNodos = maxNum + 1;
        limpiarEstadosBusqueda();
    }
}