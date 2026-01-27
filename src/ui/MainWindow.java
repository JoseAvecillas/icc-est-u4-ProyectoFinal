package ui;

import javax.swing.*;
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

        leftPanel.add(new JButton("Generar"));
        leftPanel.add(Box.createVerticalStrut(5));

        leftPanel.add(new JLabel("Algoritmo:"));
        leftPanel.add(new JComboBox<>(new String[]{"BFS", "DFS"}));
        leftPanel.add(Box.createVerticalStrut(10));

        leftPanel.add(new JButton("Resolver"));
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(new JButton("Paso a Paso"));
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(new JButton("Limpiar"));
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(new JButton("Ver"));

        leftPanel.add(Box.createVerticalStrut(20));

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

        // ===== PANEL CENTRAL (IMAGEN) =====
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imageLabel.setIcon(new ImageIcon(
                getClass().getResource("/assets/Mapa.png")
        ));

        centerPanel.add(imageLabel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

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