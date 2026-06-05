package gui;

import controller.Controller;
import model.Auto;
import model.Cliente;
import model.Prenotazione;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * Pannello dell'interfaccia grafica dedicato alla gestione e visualizzazione delle auto.
 * <p>
 * Questa classe gestisce la visualizzazione del parco veicoli in una {@link JTable},
 * differenziando le funzionalità disponibili (prenotazione vs manutenzione)
 * in base al ruolo dell'utente loggato.
 */
public class AutoPanel extends JPanel {

    private JPanel mainPanel;
    private JTable table;
    private JButton btnAggiorna;
    private JButton btnPrenota;
    private JButton btnManutenzione;
    private JButton btnRendiDisp;
    private JScrollPane scrollPane;
    private JPanel bottomPanel;

    private DefaultTableModel model;
    private final Controller controller;

    /**
     * Inizializza il pannello e configura i listener per gli eventi utente.
     *
     * @param controller il controller di sistema per la logica di business
     */
    public AutoPanel(Controller controller) {
        this.controller = controller;

        add(mainPanel);

        model = new DefaultTableModel(
                new Object[]{"ID", "Targa", "Modello", "Costo Giornaliero", "Stato"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table.setModel(model);

        configuraInterfacciaPerRuolo();

        // Configurazione dei listener per i pulsanti
        btnAggiorna.addActionListener(e -> carica());
        btnPrenota.addActionListener(e -> prenota());
        btnManutenzione.addActionListener(e -> cambiaStato(Auto.StatoAuto.IN_MANUTENZIONE));
        btnRendiDisp.addActionListener(e -> cambiaStato(Auto.StatoAuto.DISPONIBILE));
    }

    /**
     * Carica i dati iniziali nella tabella.
     */
    public void caricaDatiIniziali() {
        carica();
    }

    /**
     * Adatta la visibilità dei pulsanti di controllo basandosi sul ruolo dell'utente (Cliente o Operatore).
     */
    private void configuraInterfacciaPerRuolo() {
        if (controller.isOperatoreLoggato()) {
            btnPrenota.setVisible(false);
            btnManutenzione.setVisible(true);
            btnRendiDisp.setVisible(true);
        } else {
            btnPrenota.setVisible(true);
            btnManutenzione.setVisible(false);
            btnRendiDisp.setVisible(false);
        }
    }

    /**
     * Interroga il controller per aggiornare la tabella con i dati correnti del database.
     */
    private void carica() {
        try {
            model.setRowCount(0);
            List<Auto> lista;

            if (controller.isOperatoreLoggato()) {
                lista = controller.getTutteAuto();
            } else {
                lista = controller.getAutoDisponibili();
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
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errore caricamento auto: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Modifica lo stato di un'auto selezionata in tabella.
     *
     * @param nuovoStato lo stato {@link Auto.StatoAuto} da impostare
     */
    private void cambiaStato(Auto.StatoAuto nuovoStato) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un'auto dalla tabella!", "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idAuto = (int) model.getValueAt(row, 0);
        try {
            controller.cambiaStatoAuto(idAuto, nuovoStato);
            JOptionPane.showMessageDialog(this, "Stato auto aggiornato in: " + nuovoStato, "Successo", JOptionPane.INFORMATION_MESSAGE);
            carica();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errore cambio stato: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Gestisce la logica di creazione di una nuova prenotazione da parte di un cliente.
     * Richiede l'inserimento della data di fine via input dialog.
     */
    private void prenota() {
        if (controller.isOperatoreLoggato() || !(controller.getUtenteLoggato() instanceof Cliente)) {
            JOptionPane.showMessageDialog(this, "Solo i clienti possono effettuare prenotazioni.", "Azione Negata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cliente clienteAttuale = (Cliente) controller.getUtenteLoggato();
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona l'auto che desideri prenotare.", "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String inputData = JOptionPane.showInputDialog(this,
                "Inserisci la data di fine noleggio (AAAA-MM-GG):",
                "Data Fine Noleggio",
                JOptionPane.QUESTION_MESSAGE);

        if (inputData == null) return;

        try {
            int idAuto = (int) model.getValueAt(row, 0);
            Date dataFineScelta = null;

            if (!inputData.trim().isEmpty()) {
                dataFineScelta = Date.valueOf(inputData.trim());
            }

            Prenotazione nuovaPrenotazione = new Prenotazione();
            Auto autoSelezionata = new Auto();
            autoSelezionata.setIdAuto(idAuto);

            nuovaPrenotazione.setAuto(autoSelezionata);
            nuovaPrenotazione.setCliente(clienteAttuale);
            nuovaPrenotazione.setDataInizio(new java.util.Date());
            nuovaPrenotazione.setDataFine(dataFineScelta);
            nuovaPrenotazione.setStato(Prenotazione.StatoPren.IN_ATTESA);

            controller.effettuaPrenotazione(nuovaPrenotazione);

            JOptionPane.showMessageDialog(this, "Prenotazione effettuata con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            carica();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Formato data non valido. Usa il formato AAAA-MM-GG.", "Errore Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore durante la prenotazione: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

}