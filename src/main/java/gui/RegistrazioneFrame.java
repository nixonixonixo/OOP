package gui;

import model.Cliente;
import service.ClienteService;
import service.UtenteService;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class RegistrazioneFrame extends JFrame {
    private JTextField usernameField, nomeField, cognomeField, emailField, patenteField;
    private JPasswordField passwordField;

    private final ClienteService clienteService;
    private final UtenteService utenteService;

    public RegistrazioneFrame(UtenteService utenteService, ClienteService clienteService) {
        this.utenteService = utenteService;
        this.clienteService = clienteService;

        setTitle("Registrazione Cliente");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        nomeField = new JTextField();
        cognomeField = new JTextField();
        emailField = new JTextField();
        patenteField = new JTextField();
        JButton registratiButton = new JButton("Conferma Registrazione");

        panel.add(new JLabel("Username:")); panel.add(usernameField);
        panel.add(new JLabel("Password:")); panel.add(passwordField);
        panel.add(new JLabel("Nome:")); panel.add(nomeField);
        panel.add(new JLabel("Cognome:")); panel.add(cognomeField);
        panel.add(new JLabel("Email:")); panel.add(emailField);
        panel.add(new JLabel("Patente:")); panel.add(patenteField);
        panel.add(new JLabel()); panel.add(registratiButton);

        add(panel);
        registratiButton.addActionListener(e -> registrazione());
        setVisible(true);
    }

    private void registrazione() {
        try {
            // Validazione minima
            if (usernameField.getText().isEmpty() || patenteField.getText().isEmpty()) {
                throw new Exception("Compila tutti i campi obbligatori");
            }

            Cliente cliente = new Cliente(
                    generaId(),
                    usernameField.getText(),
                    new String(passwordField.getPassword()),
                    nomeField.getText(),
                    cognomeField.getText(),
                    emailField.getText(),
                    patenteField.getText(),
                    BigDecimal.ZERO
            );

            utenteService.registraCliente(cliente);
            JOptionPane.showMessageDialog(this, "Registrazione completata con successo!");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int generaId() {
        return (int) (Math.random() * 100000);
    }
}