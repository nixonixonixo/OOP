package gui;

import controller.Controller;
import model.Cliente;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

/**
 * Finestra di registrazione per i nuovi clienti.
 */
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

    /**
     * Inizializza la finestra di registrazione.
     *
     * @param controller il controller di sistema per gestire la persistenza del nuovo cliente
     */
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

    /**
     * Raccoglie i dati dai campi di input, crea un oggetto {@link Cliente}
     * e invoca la logica di registrazione nel controller.
     */
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

            // Si utilizza 0 come ID provvisorio poiché assegnato dal Database
            Cliente cliente = new Cliente(
                    0,
                    username,
                    password,
                    nome,
                    cognome,
                    email,
                    patente,
                    BigDecimal.ZERO
            );

            controller.registraCliente(cliente);

            JOptionPane.showMessageDialog(this, "Registrazione completata con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}