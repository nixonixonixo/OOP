package gui;

import controller.AuthController;
import dao.UtenteDAO;
import implementazionePostgresDAO.ImpUtenteDAO;
import model.Utente;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {

        setTitle("Login - Noleggio Auto");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton registratiButton =
                new JButton("Registrati");

        panel.add(new JLabel("Username"));
        panel.add(usernameField);

        panel.add(new JLabel("Password"));
        panel.add(passwordField);

        panel.add(loginButton);
        panel.add(registratiButton);

        add(panel);

        loginButton.addActionListener(
                e -> login()
        );

        registratiButton.addActionListener(
                e -> new RegistrazioneFrame()
        );

        setVisible(true);
    }

    private void login() {

        try {

            String username =
                    usernameField.getText();

            String password =
                    new String(
                            passwordField.getPassword()
                    );

            UtenteDAO dao =
                    new ImpUtenteDAO();

            AuthController auth =
                    new AuthController(dao);

            Utente utente =
                    auth.login(username, password);

            JOptionPane.showMessageDialog(
                    this,
                    "Login effettuato!"
            );

            dispose();

            new DashboardFrame(utente);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}