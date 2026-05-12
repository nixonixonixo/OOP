package gui;

import dao.ClienteDAO;
import dao.UtenteDAO;
import implementazionePostgresDAO.ImpClienteDAO;
import implementazionePostgresDAO.ImpUtenteDAO;
import model.Cliente;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class RegistrazioneFrame
        extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField emailField;
    private JTextField patenteField;

    public RegistrazioneFrame() {

        setTitle("Registrazione");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(
                DISPOSE_ON_CLOSE
        );

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                7,
                                2,
                                10,
                                10
                        )
                );

        usernameField = new JTextField();
        passwordField =
                new JPasswordField();

        nomeField = new JTextField();
        cognomeField = new JTextField();
        emailField = new JTextField();
        patenteField = new JTextField();

        JButton registratiButton =
                new JButton("Registrati");

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

        registratiButton.addActionListener(
                e -> registrazione()
        );

        setVisible(true);
    }

    private void registrazione() {

        try {

            Cliente cliente =
                    new Cliente(
                            generaId(),
                            usernameField.getText(),
                            new String(
                                    passwordField
                                            .getPassword()
                            ),
                            nomeField.getText(),
                            cognomeField.getText(),
                            emailField.getText(),
                            patenteField.getText(),
                            BigDecimal.ZERO
                    );

            UtenteDAO utenteDAO =
                    new ImpUtenteDAO();

            ClienteDAO clienteDAO =
                    new ImpClienteDAO();

            utenteDAO.salvaUtente(cliente);
            clienteDAO.salvaCliente(cliente);

            JOptionPane.showMessageDialog(
                    this,
                    "Registrazione completata!"
            );

            dispose();

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

        return (int)
                (Math.random() * 100000);
    }
}