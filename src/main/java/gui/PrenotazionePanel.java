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
    private final Cliente clienteLoggato;

    public PrenotazionePanel(Cliente cliente, PrenotazioneService prenotazioneService) {

        this.clienteLoggato = cliente;
        this.prenotazioneService = prenotazioneService;

        setLayout(new BorderLayout());

        String[] colonne = {"ID", "Data Inizio", "Data Fine", "Stato", "Auto", "Cliente"};

        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabella = new JTable(tableModel);
        tabella.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(tabella), BorderLayout.CENTER);

        if (clienteLoggato == null) {
            add(creaToolbarOperatore(), BorderLayout.SOUTH);
        }

        tabella.getSelectionModel().addListSelectionListener(e -> aggiornaBottoni());

        caricaDati();
    }

    private JPanel creaToolbarOperatore() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        btnConferma = new JButton("Conferma");
        btnAnnulla = new JButton("Annulla");

        btnConferma.setEnabled(false);
        btnAnnulla.setEnabled(false);

        btnConferma.setBackground(new Color(40, 167, 69));
        btnConferma.setForeground(Color.WHITE);

        btnAnnulla.setBackground(new Color(220, 53, 69));
        btnAnnulla.setForeground(Color.WHITE);

        btnConferma.addActionListener(e -> aggiornaStato(Prenotazione.StatoPren.CONFERMATA));
        btnAnnulla.addActionListener(e -> aggiornaStato(Prenotazione.StatoPren.ANNULLATA));

        panel.add(btnConferma);
        panel.add(btnAnnulla);

        return panel;
    }

    private void caricaDati() {

        try {
            tableModel.setRowCount(0);

            List<Prenotazione> lista = (clienteLoggato != null)
                    ? prenotazioneService.getPrenotazioniCliente(clienteLoggato.getIdUtente())
                    : prenotazioneService.getTuttePrenotazioni();

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

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Errore caricamento prenotazioni: " + e.getMessage()
            );
        }
    }

    private void aggiornaBottoni() {

        if (clienteLoggato != null || btnConferma == null) return;

        int row = tabella.getSelectedRow();

        if (row == -1) {
            btnConferma.setEnabled(false);
            btnAnnulla.setEnabled(false);
            return;
        }

        Prenotazione.StatoPren stato =
                (Prenotazione.StatoPren) tableModel.getValueAt(row, 3);

        boolean inAttesa = stato == Prenotazione.StatoPren.IN_ATTESA;

        btnConferma.setEnabled(inAttesa);
        btnAnnulla.setEnabled(inAttesa);
    }

    private void aggiornaStato(Prenotazione.StatoPren stato) {

        int row = tabella.getSelectedRow();
        if (row == -1) return;

        int id = (Integer) tableModel.getValueAt(row, 0);

        try {
            if (stato == Prenotazione.StatoPren.CONFERMATA) {
                prenotazioneService.confermaPrenotazione(id);
            } else {
                prenotazioneService.annullaPrenotazione(id);
            }

            caricaDati();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Errore aggiornamento: " + e.getMessage()
            );
        }
    }
}