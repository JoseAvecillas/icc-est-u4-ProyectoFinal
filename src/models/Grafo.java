package models;
import java.util.*;

public class Grafo {
    private Map<Nodo, List<Nodo>> adyacencia = new HashMap<>();

    public void agregarNodo(Nodo nodo) {
        adyacencia.putIfAbsent(nodo, new ArrayList<>());
    }

    // Conexión BIDIRECCIONAL
    public void conectar(Nodo a, Nodo b) {
        if (a == null || b == null) return;

        adyacencia.get(a).add(b);
        adyacencia.get(b).add(a);
    }

    // Conexión DIRECCIONAL: a -> b
    public void conectarDirigido(Nodo origen, Nodo destino) {
        if (origen == null || destino == null) return;

        adyacencia.putIfAbsent(origen, new ArrayList<>());
        adyacencia.get(origen).add(destino);
    }

    // Eliminar nodo y todas sus conexiones
    public void eliminarNodo(Nodo nodo) {
        if (nodo == null) return;

        adyacencia.remove(nodo);

        for (List<Nodo> vecinos : adyacencia.values()) {
            vecinos.remove(nodo);
        }
    }

    public List<Nodo> getVecinos(Nodo nodo) {
        if (nodo == null) return new ArrayList<>();
        // Si el nodo actual está bloqueado, no se puede salir de él
        if (nodo.getTipo() == Nodo.TipoNodo.BLOQUEADO) {
            return new ArrayList<>();
        }
        List<Nodo> vecinos = adyacencia.getOrDefault(nodo, new ArrayList<>());
        List<Nodo> filtrados = new ArrayList<>();
        // No permitir entrar a bloqueados
        for (Nodo v : vecinos) {
            if (v.getTipo() != Nodo.TipoNodo.BLOQUEADO) {
                filtrados.add(v);
            }
        }
        return filtrados;
    }

    public Set<Nodo> getNodos() {
        return adyacencia.keySet();
    }

    public Map<Nodo, List<Nodo>> getListaAdyacencia() {
        return adyacencia;
    }
}