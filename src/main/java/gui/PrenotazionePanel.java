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

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Integer.class;
                return Object.class;
            }
        };

        tabella = new JTable(tableModel);
        tabella.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(tabella), BorderLayout.CENTER);

        // Se l'utente è un operatore (clienteLoggato è null), mostriamo la toolbar
        if (clienteLoggato == null) {
            add(creaToolbarOperatore(), BorderLayout.SOUTH);
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

        // Styling
        btnConferma.setBackground(new Color(40, 167, 69));
        btnConferma.setForeground(Color.WHITE);
        btnConferma.setOpaque(true);
        btnConferma.setBorderPainted(false);

        btnAnnulla.setBackground(new Color(220, 53, 69));
        btnAnnulla.setForeground(Color.WHITE);
        btnAnnulla.setOpaque(true);
        btnAnnulla.setBorderPainted(false);

        // Listeners corretti dentro il metodo
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
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errore caricamento: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
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

        Object value = tableModel.getValueAt(row, 3);
        // Controlliamo se lo stato è "IN_ATTESA" per abilitare i tasti
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
                prenotazioneService.confermaPrenotazione(idPre);
                JOptionPane.showMessageDialog(this, "Prenotazione Confermata!");
            } else if (nuovoStato == Prenotazione.StatoPren.ANNULLATA) {
                prenotazioneService.annullaPrenotazione(idPre);
                JOptionPane.showMessageDialog(this, "Prenotazione Annullata. L'auto è di nuovo disponibile.");
            }

            caricaDati(); // Rinfresca la tabella
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
        }
    }
}