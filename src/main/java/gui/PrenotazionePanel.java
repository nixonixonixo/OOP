package gui;

import model.Cliente;
import model.Prenotazione;
import service.PrenotazioneService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PrenotazionePanel extends JPanel {

    private JTable tabella;
    private DefaultTableModel tableModel;
    private JButton btnConferma;
    private JButton btnAnnulla;

    private final PrenotazioneService prenotazioneService;
    private Cliente clienteLoggato;

    public PrenotazionePanel(Cliente cliente, PrenotazioneService service) {

        this.clienteLoggato = cliente;
        this.prenotazioneService = service;

        setLayout(new BorderLayout());

        String[] colonne = {"ID", "Data Inizio", "Data Fine", "Stato", "Auto", "Cliente"};

        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tabella = new JTable(tableModel);
        add(new JScrollPane(tabella), BorderLayout.CENTER);

        if (clienteLoggato == null) {
            add(creaToolbarOperatore(), BorderLayout.SOUTH);
        }

        caricaDati();
    }

    private JPanel creaToolbarOperatore() {

        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT));

        btnConferma = new JButton("Conferma");
        btnAnnulla = new JButton("Annulla");

        btnConferma.setEnabled(false);
        btnAnnulla.setEnabled(false);

        btnConferma.addActionListener(e -> confermaPrenotazione());
        btnAnnulla.addActionListener(e -> annullaPrenotazione());

        pnl.add(btnConferma);
        pnl.add(btnAnnulla);

        return pnl;
    }

    private void caricaDati() {
        try {

            tableModel.setRowCount(0);

            List<Prenotazione> lista;

            if (clienteLoggato != null) {
                lista = prenotazioneService
                        .getPrenotazioniCliente(clienteLoggato.getIdUtente());
            } else {
                lista = prenotazioneService.getTuttePrenotazioni();
            }

            for (Prenotazione p : lista) {
                tableModel.addRow(new Object[]{
                        p.getIdPrenotazione(),
                        p.getDataInizio(),
                        p.getDataFine(),
                        p.getStato(),
                        p.getAuto().getModello(),
                        p.getCliente().getIdUtente()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void confermaPrenotazione() {
        int row = tabella.getSelectedRow();
        if (row == -1) return;

        int id = (int) tableModel.getValueAt(row, 0);

        try {
            prenotazioneService.confermaPrenotazione(id);
            caricaDati();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void annullaPrenotazione() {
        int row = tabella.getSelectedRow();
        if (row == -1) return;

        int id = (int) tableModel.getValueAt(row, 0);

        try {
            prenotazioneService.annullaPrenotazione(id);
            caricaDati();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}