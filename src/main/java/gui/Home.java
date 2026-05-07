package gui;

import controller.PrenotazioneController;
import implementazionePostgresDAO.ImpAutoDAO;
import implementazionePostgresDAO.ImpPrenotazioneDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Home {
    private JPanel mainPanel;
    private JButton btnPrenota;
    private JButton btnVisualizzaAuto;
    private JButton btnGestioneNoleggi;
    private JLabel lblWelcome;

    private static JFrame frameHome;
    private PrenotazioneController prenotazioneController;

    public Home() {
        // Inizializziamo i controller necessari passando i DAO
        // In un progetto reale, questi verrebbero passati dal login
        prenotazioneController = new PrenotazioneController(new ImpPrenotazioneDAO(), new ImpAutoDAO());

        // Setup del Layout se non usi il GUI Designer di IntelliJ
        if (mainPanel == null) {
            setupManualUI();
        }

        // Listener per il tasto "Prenota"
        btnPrenota.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Qui aprirai la finestra per creare una prenotazione
                JOptionPane.showMessageDialog(frameHome, "Apertura modulo prenotazione...");
                // Esempio: new PrenotazioneGUI(prenotazioneController).setVisible(true);
            }
        });

        // Listener per visualizzare le auto
        btnVisualizzaAuto.addActionListener(e -> {
            JOptionPane.showMessageDialog(frameHome, "Caricamento lista auto dal database...");
        });
    }

    private void setupManualUI() {
        mainPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        lblWelcome = new JLabel("Sistema Noleggio Auto - Home", SwingConstants.CENTER);
        btnPrenota = new JButton("Effettua una Prenotazione");
        btnVisualizzaAuto = new JButton("Catalogo Auto");
        btnGestioneNoleggi = new JButton("I miei Noleggi");

        mainPanel.add(lblWelcome);
        mainPanel.add(btnPrenota);
        mainPanel.add(btnVisualizzaAuto);
        mainPanel.add(btnGestioneNoleggi);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    public static void main(String[] args) {
        frameHome = new JFrame("Piattaforma Noleggio - Home");
        Home h = new Home();
        frameHome.setContentPane(h.mainPanel);
        frameHome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameHome.setSize(400, 300);
        frameHome.setLocationRelativeTo(null); // Centra la finestra
        frameHome.setVisible(true);
    }
}