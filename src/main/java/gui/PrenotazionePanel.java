package gui;

import controller.Controller;
import model.Prenotazione;
import model.Utente;

import model.Operatore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PrenotazionePanel extends JPanel {

    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private JTable tabella;
    private JPanel toolbarOperatore;
    private JButton btnConferma;
    private JButton btnAnnulla;

    private DefaultTableModel tableModel;
    private final Controller controller;

    public PrenotazionePanel(Controller controller) {
        this.controller = controller;

        add(mainPanel);


        String[] colonne = {"ID", "Data Inizio", "Data Fine", "Stato", "Auto", "ID Cliente"};
        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Celle non modificabili direttamente
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Integer.class;
                return Object.class;
            }
        };

        tabella.setModel(tableModel);
        tabella.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabella.getTableHeader().setReorderingAllowed(false);


        Utente utenteLoggato = controller.getUtenteLoggato();
        boolean isStaff = false;

        if (utenteLoggato instanceof Operatore) {
            Operatore op = (Operatore) utenteLoggato;
            isStaff = op.getRuolo() == Operatore.Ruolo.ADMIN ||
                    op.getRuolo() == Operatore.Ruolo.ADDETTO_NOLEGGIO  ||
                    op.getRuolo() == Operatore.Ruolo.MANUTENTORE;

        }

        if (isStaff) {
            toolbarOperatore.setVisible(true);


            btnConferma.setBackground(new Color(40, 167, 69));
            btnConferma.setForeground(Color.WHITE);
            btnAnnulla.setBackground(new Color(220, 53, 69));
            btnAnnulla.setForeground(Color.WHITE);

            tabella.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    aggiornaBottoni();
                }
            });

            btnConferma.addActionListener(e -> aggiornaStato(Prenotazione.StatoPren.CONFERMATA));
            btnAnnulla.addActionListener(e -> aggiornaStato(Prenotazione.StatoPren.ANNULLATA));
        } else {

            toolbarOperatore.setVisible(false);
        }


        caricaDati();
    }

    private void caricaDati() {
        try {
            tableModel.setRowCount(0);
            Utente utenteLoggato = controller.getUtenteLoggato();
            List<Prenotazione> lista;

            if (utenteLoggato != null && !(utenteLoggato instanceof Operatore)) {
                lista = controller.getPrenotazioniCliente(utenteLoggato.getIdUtente());
            } else {
                lista = controller.getTuttePrenotazioni();
            }

            if (lista != null) {
                for (Prenotazione p : lista) {
                    tableModel.addRow(new Object[]{
                            p.getIdPrenotazione(),
                            p.getDataInizio(),
                            p.getDataFine(),
                            p.getStato(),
                            (p.getAuto() != null) ? p.getAuto().getModello() : "N/D",
                            (p.getCliente() != null) ? p.getCliente().getIdUtente() : "N/D"
                    });
                }
            }

            aggiornaBottoni();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errore caricamento prenotazioni: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aggiornaBottoni() {
        if (!toolbarOperatore.isVisible()) return;

        int row = tabella.getSelectedRow();
        if (row == -1) {
            btnConferma.setEnabled(false);
            btnAnnulla.setEnabled(false);
            return;
        }

        Object value = tableModel.getValueAt(row, 3);
        if (value == null) return;

        String statoStr = value.toString();
        boolean inAttesa = statoStr.equalsIgnoreCase("IN_ATTESA");

        btnConferma.setEnabled(inAttesa);
        btnAnnulla.setEnabled(inAttesa);
    }

    private void aggiornaStato(Prenotazione.StatoPren nuovoStato) {
        int row = tabella.getSelectedRow();
        if (row == -1) return;

        int idPre = (int) tableModel.getValueAt(row, 0);

        try {
            if (nuovoStato == Prenotazione.StatoPren.CONFERMATA) {
                controller.confermaPrenotazione(idPre);
                JOptionPane.showMessageDialog(this, "Prenotazione Confermata con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            } else if (nuovoStato == Prenotazione.StatoPren.ANNULLATA) {
                controller.annullaPrenotazione(idPre);
                JOptionPane.showMessageDialog(this, "Prenotazione Annullata. L'auto è tornata disponibile.", "Successo", JOptionPane.INFORMATION_MESSAGE);
            }

            caricaDati();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore durante la modifica dello stato: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}