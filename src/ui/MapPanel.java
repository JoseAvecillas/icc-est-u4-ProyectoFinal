package ui;

import models.Nodo;
import controller.LaberintoController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;

public class MapPanel extends JPanel {

    private Image mapa;
    private LaberintoController controller;

    private Nodo nodoSeleccionado = null; // para conectar con clic derecho

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
        }

        // 🔵 MODO CONECTAR NODOS (CLICK DERECHO)
        if (SwingUtilities.isRightMouseButton(e) && controller.isModoConectar()) {
            Nodo clickeado = controller.obtenerNodoCercano(x, y);

            if (clickeado != null) {
                if (nodoSeleccionado == null) {
                    nodoSeleccionado = clickeado;
                } else {
                    controller.conectarNodos(nodoSeleccionado, clickeado);
                    nodoSeleccionado = null;
                    repaint();
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar mapa
        g.drawImage(mapa, 0, 0, getWidth(), getHeight(), this);

        // Dibujar conexiones
        g.setColor(Color.BLACK);
        Map<Nodo, List<Nodo>> conexiones = controller.getConexiones();
        for (Nodo n : conexiones.keySet()) {
            for (Nodo vecino : conexiones.get(n)) {
                g.drawLine(n.getX(), n.getY(), vecino.getX(), vecino.getY());
            }
        }

        // Dibujar nodos
        for (Nodo n : controller.getNodos()) {
            g.setColor(Color.BLUE);
            g.fillOval(n.getX() - 12, n.getY() - 12, 24, 24);

            g.setColor(Color.WHITE);
            g.drawString(n.getId(), n.getX() - 6, n.getY() - 10);
        }
    }
}