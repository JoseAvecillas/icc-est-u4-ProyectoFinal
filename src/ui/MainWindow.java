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

        styleButton.accept(btnColocar);
        styleButton.accept(btnConectarBi);
        styleButton.accept(btnConectarDir);

        leftPanel.add(btnColocar);
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(btnConectarBi);
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(btnConectarDir);
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

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton infoButton = new JButton("Información");
        infoButton.setFont(fontBoton);
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

    private void ejecutarPaso(LaberintoController controller, MapPanel mapPanel, boolean bfs) {
        if (!controller.hayInicioYFin()) return;

        if (!controller.estaEnPasoAPaso()
                || controller.getPasoEsBFS() == null
                || controller.getPasoEsBFS() != bfs) {
            controller.iniciarPasoAPaso(bfs);
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