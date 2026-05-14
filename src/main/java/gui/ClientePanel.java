package gui;

import model.Cliente;
import service.ClienteService;
import service.PagamentoService; // Aggiunto import

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class ClientePanel extends JPanel {

    private final ClienteService clienteService;
    private final PagamentoService pagamentoService; // Aggiunto servizio pagamenti
    private final Cliente cliente;

    private JLabel lblNome;
    private JLabel lblEmail;
    private JLabel lblCredito;

    private JTextField txtRicarica;
    private JButton btnRicarica;

    // Modificato il costruttore per ricevere PagamentoService
    public ClientePanel(Cliente cliente, ClienteService service, PagamentoService pagamentoService) {

        this.cliente = cliente;
        this.clienteService = service;
        this.pagamentoService = pagamentoService; // Inizializzato

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        lblNome = new JLabel();
        lblEmail = new JLabel();
        lblCredito = new JLabel();

        // Font più leggibile per il credito
        lblCredito.setFont(new Font("SansSerif", Font.BOLD, 14));

        infoPanel.add(lblNome);
        infoPanel.add(lblEmail);
        infoPanel.add(lblCredito);

        add(infoPanel, BorderLayout.NORTH);

        JPanel ricaricaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ricaricaPanel.setBorder(BorderFactory.createTitledBorder("Ricarica Credito"));

        txtRicarica = new JTextField(10);
        btnRicarica = new JButton("Ricarica ora");

        btnRicarica.addActionListener(e -> ricaricaCredito());

        ricaricaPanel.add(new JLabel("Importo (€):"));
        ricaricaPanel.add(txtRicarica);
        ricaricaPanel.add(btnRicarica);

        add(ricaricaPanel, BorderLayout.CENTER);

        aggiornaDati();
    }

    private void aggiornaDati() {
        try {
            // Recuperiamo i dati freschi dal DB
            Cliente aggiornato = clienteService.getClienteById(cliente.getIdUtente());

            lblNome.setText("Nome: " + aggiornato.getNome() + " " + aggiornato.getCognome());
            lblEmail.setText("Email: " + aggiornato.getEmail());

            // Usiamo getCredito o getSaldo in base a come lo hai chiamato nel model
            lblCredito.setText("Credito Attuale: " + aggiornato.getCredito() + " €");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Errore caricamento dati: " + e.getMessage());
        }
    }

    private void ricaricaCredito() {
        try {
            String testo = txtRicarica.getText().replace(",", "."); // Gestisce la virgola
            BigDecimal importo = new BigDecimal(testo);

            if (importo.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("L'importo deve essere maggiore di zero");
            }

            // USIAMO IL NUOVO METODO CHE ABBIAMO CREATO PRIMA
            // Questo eseguirà: UPDATE CLIENTE SET credito = credito + ?
            pagamentoService.ricaricaConto(cliente.getIdUtente(), importo);

            JOptionPane.showMessageDialog(this, "Ricarica di " + importo + " € effettuata con successo!");

            // PULIZIA E REFRESH
            txtRicarica.setText("");
            aggiornaDati();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Inserire un numero valido (es: 50.00)");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Errore durante la ricarica: " + e.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}