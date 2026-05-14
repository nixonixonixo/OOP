package gui;

import model.Auto;
import model.Cliente;
import model.Operatore;
import model.Utente;
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
    private final Utente utenteLoggato; // Usiamo Utente per gestire sia Cliente che Operatore

    public AutoPanel(AutoService autoService,
                     PrenotazioneService prenotazioneService,
                     Utente utente) {

        this.autoService = autoService;
        this.prenotazioneService = prenotazioneService;
        this.utenteLoggato = utente;

        setLayout(new BorderLayout(10, 10));

        // Modello tabella
        model = new DefaultTableModel(
                new Object[]{"ID", "Targa", "Modello", "Costo Giornaliero", "Stato"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottoni
        JButton btnAggiorna = new JButton("Aggiorna");
        JButton btnPrenota = new JButton("Prenota Auto");
        JButton btnManutenzione = new JButton("Manda in Manutenzione");
        JButton btnRendiDisp = new JButton("Rendi Disponibile");

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnAggiorna);

        // LOGICA DEI RUOLI
        if (utenteLoggato instanceof Cliente) {
            bottom.add(btnPrenota);
            btnManutenzione.setVisible(false);
            btnRendiDisp.setVisible(false);
        } else if (utenteLoggato instanceof Operatore) {
            bottom.add(btnManutenzione);
            bottom.add(btnRendiDisp);
            btnPrenota.setVisible(false);
        }

        add(bottom, BorderLayout.SOUTH);

        // Action Listeners
        btnAggiorna.addActionListener(e -> carica());
        btnPrenota.addActionListener(e -> prenota());
        btnManutenzione.addActionListener(e -> cambiaStato(Auto.StatoAuto.IN_MANUTENZIONE));
        btnRendiDisp.addActionListener(e -> cambiaStato(Auto.StatoAuto.DISPONIBILE));

        carica();
    }

    private void carica() {
        try {
            model.setRowCount(0);
            List<Auto> lista;

            // Se è Operatore vede TUTTE, se è Cliente vede solo DISPONIBILI
            if (utenteLoggato instanceof Operatore) {
                lista = autoService.getTutte();
            } else {
                lista = autoService.getAutoDisponibili();
            }

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

    private void cambiaStato(Auto.StatoAuto nuovoStato) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un'auto!");
            return;
        }

        int idAuto = (int) model.getValueAt(row, 0);
        try {
            autoService.cambiaStato(idAuto, nuovoStato);
            JOptionPane.showMessageDialog(this, "Stato auto aggiornato in: " + nuovoStato);
            carica();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore cambio stato: " + e.getMessage());
        }
    }

    private void prenota() {
        if (!(utenteLoggato instanceof Cliente)) {
            JOptionPane.showMessageDialog(this, "Solo i clienti possono prenotare.");
            return;
        }

        Cliente c = (Cliente) utenteLoggato;
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un'auto dalla tabella");
            return;
        }

        String inputData = JOptionPane.showInputDialog(this,
                "Inserisci la data di fine noleggio (AAAA-MM-GG):",
                "Data Fine Noleggio",
                JOptionPane.QUESTION_MESSAGE);

        try {
            int idAuto = (int) model.getValueAt(row, 0);
            Date dataFineScelta = null;

            if (inputData != null && !inputData.trim().isEmpty()) {
                dataFineScelta = Date.valueOf(inputData.trim());
            }

            Prenotazione nuovaPrenotazione = new Prenotazione();
            Auto autoSelezionata = new Auto();
            autoSelezionata.setIdAuto(idAuto);

            nuovaPrenotazione.setAuto(autoSelezionata);
            nuovaPrenotazione.setCliente(c);
            nuovaPrenotazione.setDataInizio(new java.util.Date());
            nuovaPrenotazione.setDataFine(dataFineScelta);
            nuovaPrenotazione.setStato(Prenotazione.StatoPren.IN_ATTESA);

            prenotazioneService.effettuaPrenotazione(nuovaPrenotazione);

            JOptionPane.showMessageDialog(this, "Prenotazione effettuata con successo!");
            carica();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore: " + e.getMessage());
        }
    }
}