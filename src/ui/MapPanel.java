package ui;

import models.Nodo;
import models.Nodo.TipoNodo;
import controller.LaberintoController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;

public class MapPanel extends JPanel {

    private Image mapa;
    private LaberintoController controller;

    private Nodo nodoSeleccionado = null; // para conectar

    public MapPanel(LaberintoController controller) {
        this.controller = controller;
        this.mapa = new ImageIcon(getClass().getResource("/assets/Mapa.png")).getImage();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                manejarClick(e);
            }
        });
    }

    private void manejarClick(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();

        // 🟢 MODO COLOCAR NODOS (CLICK IZQUIERDO)
        if (SwingUtilities.isLeftMouseButton(e) && controller.isModoColocar()) {
            controller.agregarNodo(x, y);
            repaint();
            return;
        }

        // 🟡 MARCAR INICIO / FIN / BORRAR (CLICK IZQUIERDO)
        if (SwingUtilities.isLeftMouseButton(e) &&
            (controller.isModoInicio() || controller.isModoFin() || controller.isModoBorrar())) {

            Nodo clickeado = controller.obtenerNodoCercano(x, y);
            controller.seleccionarNodo(clickeado);
            repaint();
            return;
        }

        // 🔵 MODO CONECTAR NODOS (CLICK DERECHO)
        if (SwingUtilities.isRightMouseButton(e) && controller.isModoConectar()) {

            Nodo clickeado = controller.obtenerNodoCercano(x, y);
            if (clickeado == null) return;

            if (nodoSeleccionado == null) {
                nodoSeleccionado = clickeado; // primer nodo
            } else {
                controller.conectarNodos(nodoSeleccionado, clickeado); // segundo nodo
                nodoSeleccionado = null; // reiniciar selección
            }

            repaint();
        }
    }

    // 🎨 Devuelve el color según el tipo de nodo
    private Color getColorNodo(Nodo n) {
        switch (n.getTipo()) {
            case INICIO:
                return Color.GREEN;

            case FIN:
                return Color.RED;

            case VISITADO:
                return new Color(150, 0, 200); // morado

            case CAMINO:
                return new Color(255, 215, 0); // dorado

            default:
                return Color.BLUE; // normal
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar mapa
        g.drawImage(mapa, 0, 0, getWidth(), getHeight(), this);

        Graphics2D g2 = (Graphics2D) g;

        // Dibujar conexiones
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(3));
        Map<Nodo, List<Nodo>> conexiones = controller.getConexiones();
        for (Nodo n : conexiones.keySet()) {
            for (Nodo vecino : conexiones.get(n)) {
                g2.drawLine(n.getX(), n.getY(), vecino.getX(), vecino.getY());
            }
        }

        // Dibujar nodos con color dinámico
        for (Nodo n : controller.getNodos()) {

            g2.setColor(getColorNodo(n));
            g2.fillOval(n.getX() - 12, n.getY() - 12, 24, 24);

            g2.setColor(Color.BLACK);
            g2.drawOval(n.getX() - 12, n.getY() - 12, 24, 24);

            g2.setColor(Color.WHITE);
            g2.drawString(n.getId(), n.getX() - 6, n.getY() - 16);
        }
    }

    public void animarBusqueda(List<Nodo> visitados, List<Nodo> caminoFinal) {
        Timer timer = new Timer(400, null); 

        final int[] index = {0};
        timer.addActionListener(e -> {

            // 🟣 FASE 1 — Animar nodos visitados
            if (index[0] < visitados.size()) {

                Nodo n = visitados.get(index[0]);

                if (n.getTipo() == TipoNodo.NORMAL) {
                    n.setTipo(TipoNodo.VISITADO);
                }
                repaint();
                index[0]++;
            }
            // Pintar el camino final completo
            else {
                timer.stop();
                for (Nodo n : caminoFinal) {
                    if (n.getTipo() != TipoNodo.INICIO && n.getTipo() != TipoNodo.FIN) {
                        n.setTipo(TipoNodo.CAMINO);
                    }
                }
                repaint();
            }
        });

        timer.start();
    }
}