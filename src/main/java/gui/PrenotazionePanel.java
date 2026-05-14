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

        String[] colonne = {"ID", "Data Inizio", "Data Fine", "Stato", "Auto", "ID Cliente"};

        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            // FIX: Sovrascriviamo getColumnClass per evitare errori di cast quando leggiamo lo Stato
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Integer.class;
                if (columnIndex == 3) return Prenotazione.StatoPren.class;
                return Object.class;
            }
        };

        tabella = new JTable(tableModel);
        tabella.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(tabella), BorderLayout.CENTER);

        // Se l'utente è un operatore (clienteLoggato == null), aggiungiamo i controlli
        if (clienteLoggato == null) {
            add(creaToolbarOperatore(), BorderLayout.SOUTH);
            // Listener per attivare/disattivare bottoni solo per l'operatore
            tabella.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    aggiornaBottoni();
                }
            });
        }

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
        // Rende il colore visibile anche su sistemi Mac/Linux
        btnConferma.setOpaque(true);
        btnConferma.setBorderPainted(false);

        btnAnnulla.setBackground(new Color(220, 53, 69));
        btnAnnulla.setForeground(Color.WHITE);
        btnAnnulla.setOpaque(true);
        btnAnnulla.setBorderPainted(false);

        btnConferma.addActionListener(e -> {
            try {
                aggiornaStato(Prenotazione.StatoPren.CONFERMATA);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        btnAnnulla.addActionListener(e -> {
            try {
                aggiornaStato(Prenotazione.StatoPren.ANNULLATA);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

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

            if (lista != null) {
                for (Prenotazione p : lista) {
                    tableModel.addRow(new Object[]{
                            p.getIdPrenotazione(),
                            p.getDataInizio(),
                            p.getDataFine(),
                            p.getStato(), // Questo è un oggetto Enum StatoPren
                            (p.getAuto() != null) ? p.getAuto().getModello() : "N/D",
                            (p.getCliente() != null) ? p.getCliente().getIdUtente() : "N/D"
                    });
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errore caricamento: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void aggiornaBottoni() {
        // Se siamo un cliente o i bottoni non sono stati inizializzati, usciamo
        if (clienteLoggato != null || btnConferma == null) return;

        int row = tabella.getSelectedRow();
        if (row == -1) {
            btnConferma.setEnabled(false);
            btnAnnulla.setEnabled(false);
            return;
        }

        // Recupero sicuro dello stato
        Object value = tableModel.getValueAt(row, 3);
        if (value instanceof Prenotazione.StatoPren stato) {
            boolean inAttesa = (stato == Prenotazione.StatoPren.IN_ATTESA);
            btnConferma.setEnabled(inAttesa);
            btnAnnulla.setEnabled(inAttesa);
        }
    }

    private void aggiornaStato(Prenotazione.StatoPren stato) throws Exception {
        int row = tabella.getSelectedRow();
        if (row == -1) return;

        int id = (Integer) tableModel.getValueAt(row, 0);

        try {
            if (stato == Prenotazione.StatoPren.CONFERMATA) {
                prenotazioneService.confermaPrenotazione(id);
            } else {
                prenotazioneService.annullaPrenotazione(id);
            }

            JOptionPane.showMessageDialog(this, "Prenotazione aggiornata con successo");
            caricaDati();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errore aggiornamento: " + e.getMessage());
        }
    }
}