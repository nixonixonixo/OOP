package gui;

import controller.Controller;
import model.Utente;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private JPanel mainPanel;
    private JPanel headerPanel;
    private JLabel lblUserInfo;
    private JButton btnLogout;
    private JTabbedPane tabbedPane;

    private final Controller controller;

    public DashboardFrame(Controller controller) {
        this.controller = controller;

        // Setup base della finestra
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        setSize(1100, 750);
        setLocationRelativeTo(null);

        initDashboard();

        btnLogout.addActionListener(e -> {
            controller.logout();
            dispose();
            LoginFrame loginFrame = new LoginFrame(controller);
            loginFrame.setLocationRelativeTo(null);
            loginFrame.setVisible(true);
        });
    }

    private void initDashboard() {
        Utente utente = controller.getUtenteLoggato();
        if (utente != null) {
            String tipoUtente = controller.isOperatoreLoggato() ? "Operatore" : "Cliente";
            setTitle("Noleggio Auto - " + utente.getNome() + " " + utente.getCognome() + " (" + tipoUtente + ")");
            lblUserInfo.setText("Utente: " + utente.getUsername() + " [" + utente.getClass().getSimpleName() + "]");
        }

        if (controller.isOperatoreLoggato()) {
            tabbedPane.addTab("Gestione Parco Auto", new AutoPanel(controller));
        } else {
            tabbedPane.addTab("Catalogo Auto", new AutoPanel(controller));
            tabbedPane.addTab("Il mio Profilo", new ClientePanel(controller));
        }
    }
}