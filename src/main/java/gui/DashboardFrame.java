package gui;

import controller.Controller;
import model.Utente;

import javax.swing.*;
import java.sql.SQLException;

public class DashboardFrame extends JFrame {

    private JPanel mainPanel;
    private JPanel headerPanel;
    private JLabel lblUserInfo;
    private JButton btnLogout;
    private JTabbedPane tabbedPane;

    private final Controller controller;

    public DashboardFrame(Controller controller) throws SQLException {
        this.controller = controller;

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

    private void initDashboard() throws SQLException {
        Utente utente = controller.getUtenteLoggato();
        if (utente != null) {
            String tipoUtente = controller.isOperatoreLoggato() ? "Operatore" : "Cliente";
            setTitle("Noleggio Auto - " + utente.getNome() + " " + utente.getCognome() + " (" + tipoUtente + ")");
            lblUserInfo.setText("Utente: " + utente.getUsername() + " [" + utente.getClass().getSimpleName() + "]");
        }

        tabbedPane.removeAll();

        if (controller.isOperatoreLoggato()) {
            tabbedPane.addTab("Gestione Parco Auto", new AutoPanel(controller));
            tabbedPane.addTab("Prenotazioni", new PrenotazionePanel(controller));
            tabbedPane.addTab("Noleggi Attivi", new NoleggioPanel(controller));
        } else {
            tabbedPane.addTab("Catalogo Auto", new AutoPanel(controller));
            tabbedPane.addTab("Le mie Prenotazioni", new PrenotazionePanel(controller));
            tabbedPane.addTab("I miei Pagamenti", new PagamentoPanel(controller));
            tabbedPane.addTab("Il mio Profilo", new ClientePanel(controller));
        }
    }
}