package gui;

import controller.Controller;
import model.Utente;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {


    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel lblUsername;
    private JLabel lblPassword;

    private final Controller controller;

    public LoginFrame(Controller controller) {
        this.controller = controller;

        setTitle("Login - Noleggio Auto");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(mainPanel); // Carica il layout strutturato nel Form Designer
        setSize(400, 250);
        setLocationRelativeTo(null);


        loginButton.addActionListener(e -> login());


        registerButton.addActionListener(e -> openRegistration());
    }

    private void login() {
        try {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                throw new IllegalArgumentException("Inserisci credenziali valide");
            }


            Utente utente = controller.login(username, password);

            JOptionPane.showMessageDialog(this, "Benvenuto " + utente.getNome(), "Accesso Eseguito", JOptionPane.INFORMATION_MESSAGE);


            this.dispose();


            DashboardFrame dashboardFrame = new DashboardFrame(controller);
            dashboardFrame.setVisible(true);

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Attenzione", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore di sistema: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openRegistration() {


        JOptionPane.showMessageDialog(this, "Finestra di registrazione in fase di collegamento.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}