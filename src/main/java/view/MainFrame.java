package main.java.view;

import main.java.view.panels.MainPanel;

import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Biblioteca App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Adiciona o painel principal com CardLayout
        add(new MainPanel());
    }
}