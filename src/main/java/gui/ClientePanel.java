package gui;

import controller.Controller;
import model.Cliente;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;

public class ClientePanel extends JPanel {

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
        add(mainPanel);

        lblCredito.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnRicarica.addActionListener(e -> ricaricaCredito());

        this.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                aggiornaDati();
            }
            @Override
            public void ancestorRemoved(AncestorEvent event) {}
            @Override
            public void ancestorMoved(AncestorEvent event) {}
        });
    }

    private void aggiornaDati() {
        try {
            if (controller.getUtenteLoggato() instanceof Cliente) {
                Cliente clienteSessione = (Cliente) controller.getUtenteLoggato();
                Cliente aggiornato = controller.getClienteById(clienteSessione.getIdUtente());

                lblNome.setText("Nome: " + aggiornato.getNome() + " " + aggiornato.getCognome());
                lblEmail.setText("Email: " + aggiornato.getEmail());
                lblCredito.setText("Credito Attuale: " + aggiornato.getCredito() + " €");
            }
        } catch (SQLException e) {
            // Log dell'errore senza bloccare l'interfaccia
            System.err.println("Errore caricamento dati: " + e.getMessage());
        }
    }

    private void ricaricaCredito() {
        try {
            String testo = txtRicarica.getText().replace(",", ".");
            if (testo.trim().isEmpty()) return;

            BigDecimal importo = new BigDecimal(testo);
            if (importo.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("L'importo deve essere maggiore di zero");
            }

            Cliente clienteSessione = (Cliente) controller.getUtenteLoggato();
            controller.ricaricaConto(clienteSessione.getIdUtente(), importo);
            JOptionPane.showMessageDialog(this, "Ricarica effettuata!");
            txtRicarica.setText("");
            aggiornaDati();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore: " + e.getMessage());
        }
    }
}