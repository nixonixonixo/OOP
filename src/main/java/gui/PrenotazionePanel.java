package gui;

import dao.PrenotazioneDAO;
import implementazionePostgresDAO.ImpPrenotazioneDAO;
import model.Cliente;
import model.Prenotazione;

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
    private PrenotazioneDAO prenotazioneDAO;
    private Cliente clienteLoggato;

    public PrenotazionePanel(Cliente cliente) {
        this.clienteLoggato = cliente;
        this.prenotazioneDAO = new ImpPrenotazioneDAO();

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

        tabella.getSelectionModel().addListSelectionListener(e -> gestisciStatoBottoni());

        caricaDati();
    }

    private JPanel creaToolbarOperatore() {
        JPanel pnlAzioni = new JPanel(new FlowLayout(FlowLayout.LEFT));

        btnConferma = new JButton("Conferma Prenotazione");
        btnAnnulla = new JButton("Annulla Prenotazione");

        btnConferma.setEnabled(false);
        btnAnnulla.setEnabled(false);
        btnConferma.setBackground(new Color(40, 167, 69));
        btnConferma.setForeground(Color.WHITE);
        btnAnnulla.setBackground(new Color(220, 53, 69));
        btnAnnulla.setForeground(Color.WHITE);

        btnConferma.addActionListener(e -> aggiornaStato(Prenotazione.StatoPren.CONFERMATA));
        btnAnnulla.addActionListener(e -> aggiornaStato(Prenotazione.StatoPren.ANNULLATA));

        pnlAzioni.add(new JLabel("Azioni Operatore: "));
        pnlAzioni.add(btnConferma);
        pnlAzioni.add(btnAnnulla);

        return pnlAzioni;
    }

    private void caricaDati() {
        try {
            tableModel.setRowCount(0);
            List<Prenotazione> lista;

            if (clienteLoggato != null) {
                lista = prenotazioneDAO.trovaPrenotazioniCliente(clienteLoggato.getIdUtente());
            } else {
                lista = prenotazioneDAO.trovaTuttePrenotazioni();
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
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errore nel caricamento: " + e.getMessage());
        }
    }

    private void gestisciStatoBottoni() {
        if (clienteLoggato != null) return;

        int riga = tabella.getSelectedRow();
        if (riga != -1) {
            String stato = tabella.getValueAt(riga, 3).toString();
            boolean inAttesa = stato.equalsIgnoreCase("IN_ATTESA");
            btnConferma.setEnabled(inAttesa);
            btnAnnulla.setEnabled(inAttesa);
        } else {
            btnConferma.setEnabled(false);
            btnAnnulla.setEnabled(false);
        }
    }

    private void aggiornaStatoAuto(Prenotazione.StatoPren nuovoStato) {
        int riga = tabella.getSelectedRow();
        if (riga != -1) {
            int id = (int) tabella.getValueAt(riga, 0);
            try {
                prenotazioneDAO.aggiornaStatoPrenotazione(id, nuovoStato);
                JOptionPane.showMessageDialog(this, "Stato aggiornato correttamente in: " + nuovoStato);
                caricaDati();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Errore durante l'aggiornamento: " + ex.getMessage());
            }
        }
    }

    private void aggiornaStato(Prenotazione.StatoPren nuovoStato) {
        int riga = tabella.getSelectedRow();
        if (riga != -1) {
            int idPrenotazione = (int) tabella.getValueAt(riga, 0);

            try {
                Prenotazione p = prenotazioneDAO.trovaPrenotazionePerId(idPrenotazione);

                if (p != null) {
                    prenotazioneDAO.aggiornaStatoPrenotazione(idPrenotazione, nuovoStato);

                    if (nuovoStato == Prenotazione.StatoPren.CONFERMATA) {
                        dao.AutoDAO autoDAO = new implementazionePostgresDAO.ImpAutoDAO();
                        autoDAO.aggiornaStatoAuto(p.getAuto().getIdAuto(), model.Auto.StatoAuto.NOLEGGIATA);
                        JOptionPane.showMessageDialog(this, "Prenotazione confermata e auto impostata su NOLEGGIATA!");
                    }
                    else if (nuovoStato == Prenotazione.StatoPren.ANNULLATA) {
                        JOptionPane.showMessageDialog(this, "Prenotazione annullata.");
                    }

                    caricaDati();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Errore durante l'operazione: " + ex.getMessage());
            }
        }
    }
}