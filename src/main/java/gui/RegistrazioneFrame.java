package gui;

import controller.Controller;
import model.Cliente;

import javax.swing.*;
import java.math.BigDecimal;

public class RegistrazioneFrame extends JFrame {

    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField emailField;
    private JTextField patenteField;
    private JButton registratiButton;

    private final Controller controller;

    public RegistrazioneFrame(Controller controller) {
        this.controller = controller;

        setTitle("Registrazione Cliente");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        add(mainPanel);

        pack();
        setLocationRelativeTo(null);

        registratiButton.addActionListener(e -> registrazione());

        setVisible(true);
    }

    private void registrazione() {
        try {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String nome = nomeField.getText().trim();
            String cognome = cognomeField.getText().trim();
            String email = emailField.getText().trim();
            String patente = patenteField.getText().trim();


            if (username.isEmpty() || password.isEmpty() || patente.isEmpty() ||
                    nome.isEmpty() || cognome.isEmpty() || email.isEmpty()) {
                throw new Exception("Tutti i campi sono obbligatori per procedere con la registrazione.");
            }


            Cliente cliente = new Cliente(
                    0, // ID fittizio o gestito a monte
                    username,
                    password,
                    nome,
                    cognome,
                    email,
                    patente,
                    BigDecimal.ZERO // Credito iniziale azzerato
            );


            controller.registraCliente(cliente);

            JOptionPane.showMessageDialog(this, "Registrazione completata con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}