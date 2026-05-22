package main;

import controller.Controller;
import dao.*;
import gui.DashboardFrame; // Importiamo la tua vera dashboard
import implementazionePostgresDAO.*;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // 1. Rende l'aspetto grafico moderno e coerente con il sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Impossibile impostare il Look and Feel di sistema.");
        }

        // 2. Avvia l'interfaccia nel thread sicuro di Swing (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                // 3. Istanziamo le IMPLEMENTAZIONI concrete dei tuoi DAO Postgres
                UtenteDAO utenteDAO = new ImpUtenteDAO();
                ClienteDAO clienteDAO = new ImpClienteDAO();
                OperatoreDAO operatoreDAO = new ImpOperatoreDAO();
                AutoDAO autoDAO = new ImpAutoDAO();
                NoleggioDAO noleggioDAO = new ImpNoleggioDAO();
                PagamentoDAO pagamentoDAO = new ImpPagamentoDAO();
                PrenotazioneDAO prenotazioneDAO = new ImpPrenotazioneDAO();

                // 4. Inizializzazione del Controller centrale passando le istanze concrete
                Controller controller = new Controller(
                        utenteDAO, clienteDAO, operatoreDAO,
                        autoDAO, noleggioDAO, pagamentoDAO, prenotazioneDAO
                );

                // 5. Avvio della Dashboard principale del programma
                // Passiamo il controller appena configurato alla finestra
                DashboardFrame dashboard = new DashboardFrame(controller);
                dashboard.setVisible(true);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Errore fatale durante l'inizializzazione dei componenti:\n" + e.getMessage(),
                        "Errore di Avvio",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
}