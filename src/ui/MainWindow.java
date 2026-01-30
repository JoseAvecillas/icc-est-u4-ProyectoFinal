package ui;

import javax.swing.*;
import controller.LaberintoController;
import java.awt.*;

public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("Resolución de Laberintos");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== PANEL IZQUIERDO =====
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftPanel.setPreferredSize(new Dimension(220, 0));

        // 🔹 Botón Colocar
        JButton btnColocar = new JButton("Colocar");
        btnColocar.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(btnColocar);
        leftPanel.add(Box.createVerticalStrut(8));

        // 🔹 Botón Conectar Nodos
        JButton btnConectar = new JButton("Conectar nodos");
        btnConectar.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(btnConectar);
        leftPanel.add(Box.createVerticalStrut(15));

        // 🔹 Algoritmos
        leftPanel.add(new JLabel("Algoritmo:"));
        leftPanel.add(Box.createVerticalStrut(5));

        JButton btnBFS = new JButton("BFS");
        JButton btnDFS = new JButton("DFS");

        Dimension smallSize = new Dimension(70, 25);
        btnBFS.setPreferredSize(smallSize);
        btnDFS.setPreferredSize(smallSize);
        btnBFS.setMaximumSize(smallSize);
        btnDFS.setMaximumSize(smallSize);

        leftPanel.add(btnBFS);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(btnDFS);
        leftPanel.add(Box.createVerticalStrut(15));

        // 🔹 Otros botones
        leftPanel.add(new JButton("Resolver"));
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(new JButton("Paso a Paso"));
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(new JButton("Limpiar"));
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(new JButton("Ver"));

        leftPanel.add(Box.createVerticalStrut(20));

        // 🔹 Modo Edición
        leftPanel.add(new JLabel("Modo Edición:"));
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(new JButton("Inicio"));
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(new JButton("Fin"));
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(new JButton("Bloqueo"));
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(new JButton("Borrar"));

        add(leftPanel, BorderLayout.WEST);

        // ===== PANEL CENTRAL (MAPA INTERACTIVO) =====
        LaberintoController controller = new LaberintoController();
        MapPanel mapPanel = new MapPanel(controller);
        add(mapPanel, BorderLayout.CENTER);

        // 🔥 CONECTAR BOTONES CON LOS MODOS
        btnColocar.addActionListener(e -> controller.activarModoColocar());
        btnConectar.addActionListener(e -> controller.activarModoConectar());

        // ===== PANEL INFERIOR =====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton infoButton = new JButton("Información");
        infoButton.addActionListener(e -> mostrarInformacion());
        bottomPanel.add(infoButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void mostrarInformacion() {
        JOptionPane.showMessageDialog(
            this,
            "Nombre: Jose Avecillas\n" +
            "Carrera: Ingenieria en Sistemas\n" +
            "Materia: Estructura de Datos\n" +
            "Proyecto: Resolución de Laberintos por BFS Y DFS\n" +
            "Correo: joseavecillas07@gmail.com\n\n" +
            "Universidad Politécnica Salesiana",
            "Información",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}