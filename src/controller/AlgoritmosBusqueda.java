package controller;

import models.*;

import java.util.*;

public class AlgoritmosBusqueda {

    // ===================== BFS =====================
    public static ResultadoBusqueda bfs(Grafo grafo, Nodo inicio, Nodo fin) {

        Queue<Nodo> cola = new LinkedList<>();
        Set<Nodo> visitados = new HashSet<>();
        Map<Nodo, Nodo> padre = new HashMap<>();
        List<Nodo> ordenVisita = new ArrayList<>();

        cola.add(inicio);
        visitados.add(inicio);
        padre.put(inicio, null);

        while (!cola.isEmpty()) {
            Nodo actual = cola.poll();
            ordenVisita.add(actual);

            if (actual.equals(fin)) {
                return new ResultadoBusqueda(ordenVisita, construirCamino(padre, fin));
            }

            for (Nodo vecino : grafo.getVecinos(actual)) {
                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    padre.put(vecino, actual);
                    cola.add(vecino);
                }
            }
        }

        return new ResultadoBusqueda(ordenVisita, new ArrayList<>());
    }

    // ===================== DFS =====================
    public static ResultadoBusqueda dfs(Grafo grafo, Nodo inicio, Nodo fin) {

        Set<Nodo> visitados = new HashSet<>();
        Map<Nodo, Nodo> padre = new HashMap<>();
        List<Nodo> ordenVisita = new ArrayList<>();

        padre.put(inicio, null);

        boolean encontrado = dfsRecursivo(grafo, inicio, fin, visitados, padre, ordenVisita);

        if (encontrado) {
            return new ResultadoBusqueda(ordenVisita, construirCamino(padre, fin));
        }

        return new ResultadoBusqueda(ordenVisita, new ArrayList<>());
    }

    private static boolean dfsRecursivo(
            Grafo grafo,
            Nodo actual,
            Nodo fin,
            Set<Nodo> visitados,
            Map<Nodo, Nodo> padre,
            List<Nodo> ordenVisita
    ) {
        visitados.add(actual);
        ordenVisita.add(actual);

        if (actual.equals(fin)) {
            return true;
        }

        for (Nodo vecino : grafo.getVecinos(actual)) {
            if (!visitados.contains(vecino)) {
                padre.put(vecino, actual);

                if (dfsRecursivo(grafo, vecino, fin, visitados, padre, ordenVisita)) {
                    return true; // se detiene cuando encuentra el fin
                }
            }
        }

        return false;
    }

    // ===================== CONSTRUIR CAMINO =====================
    private static List<Nodo> construirCamino(Map<Nodo, Nodo> padre, Nodo fin) {
        List<Nodo> camino = new ArrayList<>();
        for (Nodo at = fin; at != null; at = padre.get(at)) {
            camino.add(at);
        }
        Collections.reverse(camino);
        return camino;
    }
}