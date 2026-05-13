package gui;

import model.Cliente;
import service.ClienteService;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class ClientePanel extends JPanel {

    private final ClienteService clienteService;
    private final Cliente cliente;

    private JLabel lblNome;
    private JLabel lblEmail;
    private JLabel lblCredito;

    private JTextField txtRicarica;
    private JButton btnRicarica;

    public ClientePanel(Cliente cliente, ClienteService service) {

        this.cliente = cliente;
        this.clienteService = service;

        setLayout(new BorderLayout(10, 10));

        // =========================
        // INFO CLIENTE
        // =========================
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));

        lblNome = new JLabel();
        lblEmail = new JLabel();
        lblCredito = new JLabel();

        infoPanel.add(lblNome);
        infoPanel.add(lblEmail);
        infoPanel.add(lblCredito);

        add(infoPanel, BorderLayout.NORTH);

        // =========================
        // RICARICA CREDITO
        // =========================
        JPanel ricaricaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        txtRicarica = new JTextField(10);
        btnRicarica = new JButton("Ricarica credito");

        btnRicarica.addActionListener(e -> ricaricaCredito());

        ricaricaPanel.add(new JLabel("Importo:"));
        ricaricaPanel.add(txtRicarica);
        ricaricaPanel.add(btnRicarica);

        add(ricaricaPanel, BorderLayout.CENTER);

        aggiornaDati();
    }

    // =========================
    // CARICAMENTO DATI CLIENTE
    // =========================
    private void aggiornaDati() {
        try {
            Cliente aggiornato = clienteService.getClienteById(cliente.getIdUtente());

            lblNome.setText("Nome: " + aggiornato.getNome() + " " + aggiornato.getCognome());
            lblEmail.setText("Email: " + aggiornato.getEmail());
            lblCredito.setText("Credito: " + aggiornato.getCredito() + " €");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Errore caricamento cliente: " + e.getMessage());
        }
    }

    // =========================
    // RICARICA CREDITO
    // =========================
    private void ricaricaCredito() {
        try {
            BigDecimal importo = new BigDecimal(txtRicarica.getText());

            if (importo.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Importo non valido");
            }

            clienteService.ricaricaCredito(cliente.getIdUtente(), importo);

            JOptionPane.showMessageDialog(this, "Ricarica effettuata");

            aggiornaDati();
            txtRicarica.setText("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Errore: " + e.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}