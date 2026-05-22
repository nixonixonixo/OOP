package gui;

import controller.Controller;
import model.Cliente;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;

public class ClientePanel extends JPanel {

    // Componenti bound tramite l'interfaccia grafica di IntelliJ
    private JPanel mainPanel;
    private JLabel lblNome;
    private JLabel lblEmail;
    private JLabel lblCredito;
    private JTextField txtRicarica;
    private JButton btnRicarica;
    private JPanel infoPanel;
    private JPanel ricaricaPanel;
    private JLabel lblImportoDesc;

    private final Controller controller;

    public ClientePanel(Controller controller) {
        this.controller = controller;

        // Collega il pannello principale generato dal Form a questo oggetto JPanel
        add(mainPanel);

        // Configurazione font per il credito (personalizzazione via codice)
        lblCredito.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Assegnazione del listener al bottone di ricarica
        btnRicarica.addActionListener(e -> ricaricaCredito());

        // Caricamento iniziale dei dati sul form
        aggiornaDati();
    }

    private void aggiornaDati() {
        try {
            // Verifichiamo prima che l'utente loggato sia effettivamente un cliente
            if (controller.getUtenteLoggato() instanceof Cliente) {
                Cliente clienteSessione = (Cliente) controller.getUtenteLoggato();

                // Recuperiamo i dati aggiornati direttamente dal database
                Cliente aggiornato = controller.getClienteById(clienteSessione.getIdUtente());

                lblNome.setText("Nome: " + aggiornato.getNome() + " " + aggiornato.getCognome());
                lblEmail.setText("Email: " + aggiornato.getEmail());
                lblCredito.setText("Credito Attuale: " + aggiornato.getCredito() + " €");
            } else {
                JOptionPane.showMessageDialog(this, "Errore: L'utente loggato non è un Cliente.", "Errore Ruolo", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errore caricamento dati: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ricaricaCredito() {
        try {
            String testo = txtRicarica.getText().replace(",", ".");
            if (testo.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Inserire un importo da ricaricare.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                return;
            }

            BigDecimal importo = new BigDecimal(testo);

            if (importo.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("L'importo deve essere maggiore di zero");
            }

            Cliente clienteSessione = (Cliente) controller.getUtenteLoggato();

            // Esegue l'operazione di ricarica tramite il controller unico
            controller.ricaricaConto(clienteSessione.getIdUtente(), importo);

            JOptionPane.showMessageDialog(this, "Ricarica di " + importo + " € effettuata con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);

            txtRicarica.setText("");
            aggiornaDati();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Inserire un numero valido (es: 50.00)", "Formato Errato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Valore Non Valido", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore durante la ricarica: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}