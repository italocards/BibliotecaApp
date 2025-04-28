package main.java.view.panels;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    private MainPanel mainPanel;

    public MenuPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton btnUsuarios = new JButton("Usuários");
        JButton btnObras = new JButton("Obras");
        JButton btnEmprestimos = new JButton("Empréstimos");

        btnUsuarios.addActionListener(e -> mainPanel.showPanel("USUARIOS"));
        btnObras.addActionListener(e -> mainPanel.showPanel("OBRAS"));
        btnEmprestimos.addActionListener(e -> mainPanel.showPanel("EMPRESTIMOS"));
        // Adicione listeners para os outros botões

        buttonPanel.add(btnUsuarios);
        buttonPanel.add(btnObras);
        buttonPanel.add(btnEmprestimos);

        // Centraliza os botões
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(buttonPanel);

        add(centerPanel, BorderLayout.CENTER);
    }
}