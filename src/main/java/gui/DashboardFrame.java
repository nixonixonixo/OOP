package gui;

import controller.Controller;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Finestra principale dell'applicazione che funge da Dashboard per l'utente autenticato.
 */
public class DashboardFrame extends JFrame {

    private JPanel mainPanel;
    private JPanel headerPanel;
    private JLabel lblUserInfo;
    private JButton btnLogout;
    private JTabbedPane tabbedPane;

    private final Controller controller;

    /**
     * Crea il frame della dashboard e inizializza la struttura dei tab.
     *
     * @param controller il controller di sistema per la logica di business
     * @throws SQLException in caso di errori durante il caricamento iniziale dei dati
     */
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

    /**
     * Configura l'interfaccia utente popolando il {@link JTabbedPane} con i pannelli
     * specifici per il ruolo dell'utente (Operatore o Cliente).
     *
     * @throws SQLException se si verifica un errore durante il recupero delle informazioni utente
     */
    private void initDashboard() throws SQLException {
        Utente utente = controller.getUtenteLoggato();
        if (utente != null) {
            String tipoUtente = controller.isOperatoreLoggato() ? "Operatore" : "Cliente";
            setTitle("Noleggio Auto - " + utente.getNome() + " " + utente.getCognome() + " (" + tipoUtente + ")");
            lblUserInfo.setText("Utente: " + utente.getUsername() + " [" + utente.getClass().getSimpleName() + "]");
        }

        tabbedPane.removeAll();

        // Logica di autorizzazione: definisce quali tab mostrare in base al ruolo
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