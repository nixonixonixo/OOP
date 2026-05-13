package gui;

import model.Cliente;
import service.UtenteService;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;

public class RegistrazioneFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField emailField;
    private JTextField patenteField;

    private final UtenteService utenteService;

    public RegistrazioneFrame(UtenteService utenteService) {

        this.utenteService = utenteService;

        setTitle("Registrazione");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        nomeField = new JTextField();
        cognomeField = new JTextField();
        emailField = new JTextField();
        patenteField = new JTextField();

        JButton registratiButton = new JButton("Registrati");

        panel.add(new JLabel("Username"));
        panel.add(usernameField);

        panel.add(new JLabel("Password"));
        panel.add(passwordField);

        panel.add(new JLabel("Nome"));
        panel.add(nomeField);

        panel.add(new JLabel("Cognome"));
        panel.add(cognomeField);

        panel.add(new JLabel("Email"));
        panel.add(emailField);

        panel.add(new JLabel("Patente"));
        panel.add(patenteField);

        panel.add(new JLabel());
        panel.add(registratiButton);

        add(panel);

        registratiButton.addActionListener(e -> registrazione());

        setVisible(true);
    }

    private void registrazione() {

        try {
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

            JOptionPane.showMessageDialog(
                    this,
                    "Registrazione completata"
            );

            dispose();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Errore database: " + e.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private int generaId() {
        return (int) (Math.random() * 100000);
    }
}