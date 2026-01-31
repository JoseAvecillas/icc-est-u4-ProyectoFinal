package ui;

import javax.swing.*;
import controller.LaberintoController;
import models.Nodo;
import java.awt.*;
import java.util.List;

public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("Resolución de Laberintos");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        LaberintoController controller = new LaberintoController();
        MapPanel mapPanel = new MapPanel(controller);

        // Variable para saber qué algoritmo usar en Paso a Paso
        final boolean[] usarBFS = {true};

        // ===== PANEL IZQUIERDO =====
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftPanel.setPreferredSize(new Dimension(220, 0));

        // 🔹 Sección Nodos
        JLabel lblNodos = new JLabel("Nodos");
        lblNodos.setFont(new Font("Arial", Font.BOLD, 14));
        lblNodos.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(lblNodos);
        leftPanel.add(Box.createVerticalStrut(5));

        JButton btnColocar = new JButton("Colocar");
        JButton btnConectar = new JButton("Conectar nodos");

        leftPanel.add(btnColocar);
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(btnConectar);
        leftPanel.add(Box.createVerticalStrut(15));

        // 🔹 Algoritmos
        leftPanel.add(new JLabel("Algoritmo:"));
        JButton btnBFS = new JButton("BFS");
        JButton btnDFS = new JButton("DFS");
        JButton btnPaso = new JButton("Paso a Paso");

        leftPanel.add(btnBFS);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(btnDFS);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(btnPaso);
        leftPanel.add(Box.createVerticalStrut(15));

        // 🔹 Limpiar
        JButton btnLimpiar = new JButton("Limpiar");
        leftPanel.add(btnLimpiar);
        leftPanel.add(Box.createVerticalStrut(20));

        // 🔹 Modo Edición
        leftPanel.add(new JLabel("Modo Edición:"));
        JButton btnInicio = new JButton("Inicio");
        JButton btnFin = new JButton("Fin");
        JButton btnBorrar = new JButton("Borrar");

        leftPanel.add(btnInicio);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(btnFin);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(btnBorrar);

        add(leftPanel, BorderLayout.WEST);
        add(mapPanel, BorderLayout.CENTER);

        // ===== ACCIONES =====

        btnColocar.addActionListener(e -> controller.activarModoColocar());
        btnConectar.addActionListener(e -> controller.activarModoConectar());
        btnInicio.addActionListener(e -> controller.activarModoInicio());
        btnFin.addActionListener(e -> controller.activarModoFin());
        btnBorrar.addActionListener(e -> controller.activarModoBorrar());

        btnLimpiar.addActionListener(e -> {
            controller.reiniciar();
            mapPanel.repaint();
        });

        // Solo seleccionan algoritmo
        btnBFS.addActionListener(e -> usarBFS[0] = true);
        btnDFS.addActionListener(e -> usarBFS[0] = false);

        // Ejecutar búsqueda normal inmediata
        btnBFS.addActionListener(e -> ejecutarBusqueda(controller, mapPanel, true));
        btnDFS.addActionListener(e -> ejecutarBusqueda(controller, mapPanel, false));

        // 🚀 PASO A PASO
        btnPaso.addActionListener(e -> {

            if (!controller.hayInicioYFin()) return;

            List<Nodo> recorrido = controller.ejecutarPasoAPaso(usarBFS[0]);
            mapPanel.animarBusqueda(recorrido);
        });

        // ===== PANEL INFERIOR =====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton infoButton = new JButton("Información");
        infoButton.addActionListener(e -> mostrarInformacion());
        bottomPanel.add(infoButton);
        add(bottomPanel, BorderLayout.SOUTH);

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

        if (bfs)
            controller.marcarResultado(controller.buscarConBFS(inicio, fin));
        else
            controller.marcarResultado(controller.buscarConDFS(inicio, fin));

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