package gui;

import controller.Controller;
import model.Noleggio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Pannello dell'interfaccia grafica per la gestione dei noleggi attivi.
 */
public class NoleggioPanel extends JPanel {
    private JPanel mainPanel;
    private JTable tabellaNoleggi;
    private JButton btnAggiorna;
    private JButton btnChiudi;

    private DefaultTableModel model;
    private final Controller controller;

    /**
     * Inizializza il pannello e carica l'elenco dei noleggi correnti.
     *
     * @param controller il controller di sistema
     * @throws SQLException in caso di errore nel caricamento iniziale dei dati
     */
    public NoleggioPanel(Controller controller) throws SQLException {
        this.controller = controller;

        add(mainPanel);

        model = new DefaultTableModel(new String[]{"ID", "Cliente", "Auto", "Stato"}, 0);
        tabellaNoleggi.setModel(model);

        btnAggiorna.addActionListener(e -> {
            try {
                caricaNoleggi();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Errore aggiornamento: " + ex.getMessage());
            }
        });

        btnChiudi.addActionListener(e -> {
            try {
                chiudiNoleggio();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
            }
        });

        caricaNoleggi();
    }

    /**
     * Aggiorna la tabella con i dati dei noleggi attualmente attivi recuperati dal controller.
     *
     * @throws SQLException se la comunicazione con il database fallisce
     */
    private void caricaNoleggi() throws SQLException {
        model.setRowCount(0);
        List<Noleggio> lista = controller.getNoleggiAttivi();
        for (Noleggio n : lista) {
            model.addRow(new Object[]{
                    n.getIdNoleggio(),
                    n.getPrenotazione().getCliente().getNome() + " " + n.getPrenotazione().getCliente().getCognome(),
                    n.getPrenotazione().getAuto().getModello(),
                    "ATTIVO"
            });
        }
    }

    /**
     * Gestisce la terminazione di un noleggio selezionato nella tabella.
     *
     * @throws Exception se si verifica un errore durante l'aggiornamento dello stato nel DB
     */
    private void chiudiNoleggio() throws Exception {
        int row = tabellaNoleggi.getSelectedRow();
        if (row != -1) {
            int idNoleggio = (int) model.getValueAt(row, 0);
            controller.terminaNoleggio(idNoleggio);
            JOptionPane.showMessageDialog(this, "Noleggio terminato correttamente.");
            caricaNoleggi();
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un noleggio dalla tabella.");
        }
    }



}