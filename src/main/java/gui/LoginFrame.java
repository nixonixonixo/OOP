package gui;

import controller.Controller;
import model.Utente;

import javax.swing.*;
import java.awt.*;

/**
 * Finestra di autenticazione dell'applicazione.
 * Fornisce i campi di input per le credenziali utente e gestisce il processo di
 * login.
 */
public class LoginFrame extends JFrame {

    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel lblUsername;
    private JLabel lblPassword;

    private final Controller controller;

    /**
     * Inizializza la finestra di login.
     *
     * @param controller il controller di sistema per gestire la logica di autenticazione
     */
    public LoginFrame(Controller controller) {
        this.controller = controller;

        setTitle("Login - Noleggio Auto");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        setSize(400, 250);
        setLocationRelativeTo(null);

        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> openRegistration());
    }

    /**
     * Esegue la logica di autenticazione.
     * In caso di successo, chiude la finestra di login e apre la dashboard.
     */
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

    /**
     * Apre la finestra di registrazione utente.
     */
    private void openRegistration() {
        RegistrazioneFrame regFrame = new RegistrazioneFrame(controller);
        regFrame.setVisible(true);
    }


}