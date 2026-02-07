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
    private static final int NODE_RADIUS = 12;
    private static final int ARROW_GAP = 2;
    private boolean mostrarConexiones = true;

    // Para conectar 
    private Nodo nodoSeleccionado = null;

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
        // MODO COLOCAR NODOS (CLICK IZQUIERDO)
        if (SwingUtilities.isLeftMouseButton(e) && controller.isModoColocar()) {
            controller.agregarNodo(x, y);
            repaint();
            return;
        }

        // MARCAR INICIO / FIN / BLOQUEAR (CLICK IZQUIERDO)
        if (SwingUtilities.isLeftMouseButton(e) &&
                (controller.isModoInicio() || controller.isModoFin() || controller.isModoBloquear())) {

            Nodo clickeado = controller.obtenerNodoCercano(x, y);
            controller.seleccionarNodo(clickeado);
            repaint();
            return;
        }

        // BORRAR (CLICK IZQUIERDO) -> ELIMINA el nodo
        if (SwingUtilities.isLeftMouseButton(e) && controller.isModoBorrar()) {
            Nodo clickeado = controller.obtenerNodoCercano(x, y);
            if (clickeado == null) return;

            if (nodoSeleccionado == clickeado) {
                nodoSeleccionado = null;
            }
            controller.eliminarNodo(clickeado);
            repaint();
            return;
        }

       // CONEXIONES 
        if (SwingUtilities.isRightMouseButton(e) &&
                (controller.isModoConectarBi() || controller.isModoConectarDir() || controller.isModoEliminarConexion())) {

            Nodo clickeado = controller.obtenerNodoCercano(x, y);
            if (clickeado == null) return;

            // Primer click derecho: selecciona origen
            if (nodoSeleccionado == null) {
                nodoSeleccionado = clickeado;
                repaint();
                return;
            }

            // Segundo click derecho: acción según modo
            if (controller.isModoEliminarConexion()) {
                controller.eliminarConexion(nodoSeleccionado, clickeado);
            } else if (controller.isModoConectarBi()) {
                controller.conectarBidireccional(nodoSeleccionado, clickeado);
            } else {
                controller.conectarDireccional(nodoSeleccionado, clickeado);
            }

            nodoSeleccionado = null;
            repaint();
        }
    }

    // Devuelve el color según el tipo de nodo
    private Color getColorNodo(Nodo n) {
        switch (n.getTipo()) {
            case INICIO:
                return Color.GREEN;
            case FIN:
                return Color.RED;
            case VISITADO:
                return new Color(150, 0, 200);
            case CAMINO:
                return new Color(255, 215, 0);
            case BLOQUEADO:
                return Color.BLACK; 
            default:
                return Color.BLUE;
        }
    }

    // Dibuja una flecha desde (x1,y1) hacia (x2,y2) (termina en el borde del nodo)
    private void drawArrow(Graphics2D g2, int x1, int y1, int x2, int y2) {
        double phi = Math.toRadians(30);
        int barb = 12;
        double dx = x2 - x1;
        double dy = y2 - y1;
        double dist = Math.hypot(dx, dy);
        if (dist == 0) return;
        double ux = dx / dist;
        double uy = dy / dist;
        int x2c = (int) Math.round(x2 - (NODE_RADIUS + ARROW_GAP) * ux);
        int y2c = (int) Math.round(y2 - (NODE_RADIUS + ARROW_GAP) * uy);
        int x1c = (int) Math.round(x1 + (NODE_RADIUS + ARROW_GAP) * ux);
        int y1c = (int) Math.round(y1 + (NODE_RADIUS + ARROW_GAP) * uy);
        g2.drawLine(x1c, y1c, x2c, y2c);

        double theta = Math.atan2(y2c - y1c, x2c - x1c);
        for (int j = 0; j < 2; j++) {
            double rho = theta + (j == 0 ? phi : -phi);
            int x = (int) Math.round(x2c - barb * Math.cos(rho));
            int y = (int) Math.round(y2c - barb * Math.sin(rho));
            g2.drawLine(x2c, y2c, x, y);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar mapa
        g.drawImage(mapa, 0, 0, getWidth(), getHeight(), this);
        Graphics2D g2 = (Graphics2D) g;

        // Dibujar conexiones y una flecha si es direccional
        if (mostrarConexiones) {
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(3));
            Map<Nodo, List<Nodo>> conexiones = controller.getConexiones();
            for (Nodo origen : conexiones.keySet()) {
                for (Nodo destino : conexiones.get(origen)) {
                    int x1 = origen.getX();
                    int y1 = origen.getY();
                    int x2 = destino.getX();
                    int y2 = destino.getY();

                    boolean esBidireccional =
                            conexiones.containsKey(destino) &&
                            conexiones.get(destino).contains(origen);

                    if (esBidireccional) {
                        g2.drawLine(x1, y1, x2, y2);
                    } else {
                        drawArrow(g2, x1, y1, x2, y2);
                    }
                }
            }
        }

        // Dibujar nodos
        for (Nodo n : controller.getNodos()) {

            if (nodoSeleccionado == n) {
                g2.setColor(Color.ORANGE);
                g2.fillOval(n.getX() - 15, n.getY() - 15, 30, 30);
            }
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

            if (index[0] < visitados.size()) {

                Nodo n = visitados.get(index[0]);

                if (n.getTipo() == TipoNodo.NORMAL) {
                    n.setTipo(TipoNodo.VISITADO);
                }
                repaint();
                index[0]++;
            } else {
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

    public void toggleConexiones() {
        mostrarConexiones = !mostrarConexiones;
        repaint();
    }

    public boolean isMostrandoConexiones() {
        return mostrarConexiones;
    }
}