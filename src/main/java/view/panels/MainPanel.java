package main.java.view.panels;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    private CardLayout cardLayout;

    public MainPanel() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        // Registrar todos os painéis
        add(new MenuPanel(this), "MENU");
        add(new UsuarioPanel(this), "USUARIOS");
        add(new ObrasPanel(this), "OBRAS");

        // Mostrar o painel inicial
        showPanel("MENU");
    }

    public void showPanel(String panelName) {
        cardLayout.show(this, panelName);
    }
}