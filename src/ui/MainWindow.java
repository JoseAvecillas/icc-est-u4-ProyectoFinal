package ui;

import javax.swing.*;
import controller.LaberintoController;
import models.Nodo;
import java.awt.*;

public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("Resolución de Laberintos");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        LaberintoController controller = new LaberintoController();
        MapPanel mapPanel = new MapPanel(controller);

        final Font fontTitulo = new Font("Arial", Font.BOLD, 14);
        final Font fontBoton  = new Font("Arial", Font.PLAIN, 14);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftPanel.setPreferredSize(new Dimension(220, 0));

        java.util.function.Consumer<JButton> styleButton = (btn) -> {
            btn.setFont(fontBoton);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            btn.setPreferredSize(new Dimension(200, 35));
        };

        java.util.function.Function<String, JLabel> titleLabel = (text) -> {
            JLabel lbl = new JLabel(text);
            lbl.setFont(fontTitulo);
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            return lbl;
        };

        leftPanel.add(titleLabel.apply("Nodos"));
        leftPanel.add(Box.createVerticalStrut(8));

        JButton btnColocar = new JButton("Colocar");
        JButton btnConectarBi = new JButton("Conexión Bidireccional");
        JButton btnConectarDir = new JButton("Conexión Direccional");
        JButton btnEliminarConexion = new JButton("Eliminar Conexión");

        styleButton.accept(btnColocar);
        styleButton.accept(btnConectarBi);
        styleButton.accept(btnConectarDir);
        styleButton.accept(btnEliminarConexion);

        leftPanel.add(btnColocar);
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(btnConectarBi);
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(btnConectarDir);
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(btnEliminarConexion);
        leftPanel.add(Box.createVerticalStrut(18));

        leftPanel.add(titleLabel.apply("Algoritmo"));
        leftPanel.add(Box.createVerticalStrut(8));

        JButton btnBFS = new JButton("BFS");
        JButton btnDFS = new JButton("DFS");
        JButton btnPasoBFS = new JButton("Paso a Paso BFS");
        JButton btnPasoDFS = new JButton("Paso a Paso DFS");

        styleButton.accept(btnBFS);
        styleButton.accept(btnDFS);
        styleButton.accept(btnPasoBFS);
        styleButton.accept(btnPasoDFS);

        leftPanel.add(btnBFS);
        leftPanel.add(Box.createVerticalStrut(6));
        leftPanel.add(btnDFS);
        leftPanel.add(Box.createVerticalStrut(6));
        leftPanel.add(btnPasoBFS);
        leftPanel.add(Box.createVerticalStrut(6));
        leftPanel.add(btnPasoDFS);
        leftPanel.add(Box.createVerticalStrut(18));

        JButton btnLimpiar = new JButton("Limpiar");
        styleButton.accept(btnLimpiar);
        leftPanel.add(btnLimpiar);
        leftPanel.add(Box.createVerticalStrut(20));

        leftPanel.add(titleLabel.apply("Modo Edición"));
        leftPanel.add(Box.createVerticalStrut(8));

        JButton btnInicio = new JButton("Inicio");
        JButton btnFin = new JButton("Fin");
        JButton btnBloquear = new JButton("Bloquear");
        JButton btnBorrar = new JButton("Borrar");

        styleButton.accept(btnInicio);
        styleButton.accept(btnFin);
        styleButton.accept(btnBloquear);
        styleButton.accept(btnBorrar);

        leftPanel.add(btnInicio);
        leftPanel.add(Box.createVerticalStrut(6));
        leftPanel.add(btnFin);
        leftPanel.add(Box.createVerticalStrut(6));
        leftPanel.add(btnBloquear);
        leftPanel.add(Box.createVerticalStrut(6));
        leftPanel.add(btnBorrar);

        add(leftPanel, BorderLayout.WEST);
        add(mapPanel, BorderLayout.CENTER);

        btnColocar.addActionListener(e -> controller.activarModoColocar());
        btnConectarBi.addActionListener(e -> controller.activarModoConectarBi());
        btnConectarDir.addActionListener(e -> controller.activarModoConectarDir());
        btnEliminarConexion.addActionListener(e -> controller.activarModoEliminarConexion());

        btnInicio.addActionListener(e -> controller.activarModoInicio());
        btnFin.addActionListener(e -> controller.activarModoFin());
        btnBloquear.addActionListener(e -> controller.activarModoBloquear());
        btnBorrar.addActionListener(e -> controller.activarModoBorrar());

        btnLimpiar.addActionListener(e -> {
            controller.reiniciar();
            mapPanel.repaint();
        });

        btnBFS.addActionListener(e -> ejecutarBusqueda(controller, mapPanel, true));
        btnDFS.addActionListener(e -> ejecutarBusqueda(controller, mapPanel, false));

        btnPasoBFS.addActionListener(e -> ejecutarPaso(controller, mapPanel, true));
        btnPasoDFS.addActionListener(e -> ejecutarPaso(controller, mapPanel, false));

        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel centerButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnNoVerConexiones = new JButton("No ver conexiones");
        JButton btnInfoBusqueda = new JButton("Información");

        btnNoVerConexiones.setFont(fontBoton);
        btnInfoBusqueda.setFont(fontBoton);

        centerButtons.add(btnNoVerConexiones);
        centerButtons.add(btnInfoBusqueda);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton infoButton = new JButton("Acerca de");
        infoButton.setFont(fontBoton);
        infoButton.addActionListener(e -> mostrarInformacion());
        rightPanel.add(infoButton);

        bottomPanel.add(centerButtons, BorderLayout.CENTER);
        bottomPanel.add(rightPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        btnInfoBusqueda.addActionListener(e -> {
            JDialog dialog = new JDialog(this, "Historial de búsqueda", true);
            dialog.setLayout(new BorderLayout());

            String[] columnas = {"Conexión", "Método", "Tiempo", "Encontró camino"};
            var historial = controller.getHistorial();

            Object[][] data = new Object[historial.size()][4];
            for (int i = 0; i < historial.size(); i++) {
                var r = historial.get(i);
                data[i][0] = r.conexion;
                data[i][1] = r.metodo;
                data[i][2] = r.tiempo;
                data[i][3] = r.encontro;
            }

            javax.swing.table.DefaultTableModel model =
                    new javax.swing.table.DefaultTableModel(data, columnas) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    };

            JTable table = new JTable(model);
            table.setRowHeight(26);
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

            JScrollPane scroll = new JScrollPane(table);
            scroll.setPreferredSize(new Dimension(650, 220));
            dialog.add(scroll, BorderLayout.CENTER);

            JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnGuardar = new JButton("Guardar");
            JButton btnCargar = new JButton("Cargar");
            JButton btnCerrar = new JButton("Cerrar");

            bottom.add(btnCargar);
            bottom.add(btnGuardar);
            bottom.add(btnCerrar);

            btnGuardar.addActionListener(ev -> {
                JFileChooser fc = new JFileChooser();
                if (fc.showSaveDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                    try {
                        controller.guardarConfiguracion(fc.getSelectedFile());
                        JOptionPane.showMessageDialog(dialog, "Configuración guardada.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dialog, "Error al guardar: " + ex.getMessage());
                    }
                }
            });

            btnCargar.addActionListener(ev -> {
                JFileChooser fc = new JFileChooser();
                if (fc.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                    try {
                        controller.cargarConfiguracion(fc.getSelectedFile());
                        mapPanel.repaint();
                        JOptionPane.showMessageDialog(dialog, "Configuración cargada.");
                        dialog.dispose();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dialog, "Error al cargar: " + ex.getMessage());
                    }
                }
            });

            btnCerrar.addActionListener(ev -> dialog.dispose());

            dialog.add(bottom, BorderLayout.SOUTH);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });

        setVisible(true);
    }

    private void ejecutarBusqueda(LaberintoController controller, MapPanel mapPanel, boolean bfs) {
        if (!controller.hayInicioYFin()) return;
        controller.limpiarEstadosBusqueda();

        Nodo inicio = controller.getNodos().stream()
                .filter(n -> n.getTipo() == Nodo.TipoNodo.INICIO)
                .findFirst().orElse(null);

        Nodo fin = controller.getNodos().stream()
                .filter(n -> n.getTipo() == Nodo.TipoNodo.FIN)
                .findFirst().orElse(null);

        long t1 = System.nanoTime();

        if (bfs) {
            var res = controller.buscarConBFS(inicio, fin);
            long t2 = System.nanoTime();
            controller.registrarUltimaBusqueda(inicio, fin, "BFS", (t2 - t1), !res.getCamino().isEmpty());
            controller.marcarResultado(res);
        } else {
            var res = controller.buscarConDFS(inicio, fin);
            long t2 = System.nanoTime();
            controller.registrarUltimaBusqueda(inicio, fin, "DFS", (t2 - t1), !res.getCamino().isEmpty());
            controller.marcarResultado(res);
        }

        mapPanel.repaint();
    }

    private void ejecutarPaso(LaberintoController controller, MapPanel mapPanel, boolean bfs) {
        if (!controller.hayInicioYFin()) return;

        if (!controller.estaEnPasoAPaso()
                || controller.getPasoEsBFS() == null
                || controller.getPasoEsBFS() != bfs) {

            Nodo inicio = controller.getNodos().stream()
                    .filter(n -> n.getTipo() == Nodo.TipoNodo.INICIO)
                    .findFirst().orElse(null);

            Nodo fin = controller.getNodos().stream()
                    .filter(n -> n.getTipo() == Nodo.TipoNodo.FIN)
                    .findFirst().orElse(null);

            long t1 = System.nanoTime();
            controller.iniciarPasoAPaso(bfs);
            long t2 = System.nanoTime();

            controller.registrarUltimaBusqueda(inicio, fin,
                    bfs ? "Paso a Paso BFS" : "Paso a Paso DFS",
                    (t2 - t1),
                    true);
        }

        controller.avanzarPaso();
        mapPanel.repaint();
    }

    private void mostrarInformacion() {
        JOptionPane.showMessageDialog(
            this,
            "Nombre: Jose Avecillas\n" +
            "Carrera: Ingenieria en Sistemas\n" +
            "Materia: Estructura de Datos\n" +
            "Proyecto: Resolución de Laberintos por BFS Y DFS\n" +
            "Universidad Politécnica Salesiana",
            "Información",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}