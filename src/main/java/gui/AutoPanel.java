package gui;

import model.Auto;
import model.Cliente;
import model.Prenotazione;
import service.AutoService;
import service.PrenotazioneService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class AutoPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private final AutoService autoService;
    private final PrenotazioneService prenotazioneService;
    private final Cliente clienteLoggato;

    public AutoPanel(AutoService autoService,
                     PrenotazioneService prenotazioneService,
                     Cliente cliente) {

        this.autoService = autoService;
        this.prenotazioneService = prenotazioneService;
        this.clienteLoggato = cliente;

        setLayout(new BorderLayout(10, 10));

        model = new DefaultTableModel(
                new Object[]{"ID", "Targa", "Modello", "Costo Giornaliero", "Stato"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnAggiorna = new JButton("Aggiorna");
        JButton btnPrenota = new JButton("Prenota Auto");

        if (clienteLoggato == null) {
            btnPrenota.setVisible(false);
        }

        btnAggiorna.addActionListener(e -> carica());
        btnPrenota.addActionListener(e -> prenota());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnPrenota);
        bottom.add(btnAggiorna);

        add(bottom, BorderLayout.SOUTH);

        carica();
    }

    private void carica() {
        try {
            model.setRowCount(0);
            List<Auto> lista = autoService.getAutoDisponibili();

            for (Auto a : lista) {
                model.addRow(new Object[]{
                        a.getIdAuto(),
                        a.getTarga(),
                        a.getModello(),
                        a.getCostoDaily(),
                        a.getStato()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore caricamento auto: " + e.getMessage());
        }
    }

    private void prenota() {
        if (clienteLoggato == null) {
            JOptionPane.showMessageDialog(this, "Solo i clienti possono prenotare.");
            return;
        }

        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un'auto dalla tabella");
            return;
        }

        // 1. CHIEDIAMO LA DATA DI FINE ALL'UTENTE
        String inputData = JOptionPane.showInputDialog(this,
                "Inserisci la data di fine noleggio (AAAA-MM-GG):\n(Lascia vuoto se non sei ancora sicuro)",
                "Data Fine Noleggio",
                JOptionPane.QUESTION_MESSAGE);

        try {
            int idAuto = (int) model.getValueAt(row, 0);

            // Gestione della data scelta
            Date dataFineScelta = null;
            if (inputData != null && !inputData.trim().isEmpty()) {
                try {
                    dataFineScelta = Date.valueOf(inputData.trim());

                    // Controllo logico: la data di fine non può essere nel passato
                    if (dataFineScelta.before(new java.util.Date())) {
                        JOptionPane.showMessageDialog(this, "La data di fine non può essere precedente a oggi.");
                        return;
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, "Formato data non valido! Usa AAAA-MM-GG (es. 2026-12-31)");
                    return;
                }
            }

            // Creiamo l'oggetto Prenotazione da passare al service
            // NOTA: Non settiamo l'ID perché lo genera il DB
            Prenotazione nuovaPrenotazione = new Prenotazione();

            // Creiamo un oggetto Auto fittizio con solo l'ID (sufficiente per il DAO)
            Auto autoSelezionata = new Auto();
            autoSelezionata.setIdAuto(idAuto);

            nuovaPrenotazione.setAuto(autoSelezionata);
            nuovaPrenotazione.setCliente(clienteLoggato);
            nuovaPrenotazione.setDataInizio(new java.util.Date()); // Oggi
            nuovaPrenotazione.setDataFine(dataFineScelta);
            nuovaPrenotazione.setStato(Prenotazione.StatoPren.IN_ATTESA);

            // Chiamata al metodo del service che usa il DAO aggiornato
            prenotazioneService.effettuaPrenotazione(nuovaPrenotazione);

            JOptionPane.showMessageDialog(this, "Prenotazione effettuata con successo!");
            carica();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore prenotazione: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}