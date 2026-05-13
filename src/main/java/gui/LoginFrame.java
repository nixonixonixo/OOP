package gui;

import model.Utente;
import service.UtenteService;
import service.AutoService;
import service.PrenotazioneService;
import service.NoleggioService;
import service.PagamentoService;
import service.ClienteService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    // SERVICE UNICO DI AUTENTICAZIONE
    private final UtenteService utenteService;

    public LoginFrame(UtenteService utenteService) {

        this.utenteService = utenteService;

        setTitle("Login - Noleggio Auto");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton registratiButton = new JButton("Registrati");

        panel.add(new JLabel("Username"));
        panel.add(usernameField);
        panel.add(new JLabel("Password"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registratiButton);

        add(panel);

        loginButton.addActionListener(e -> login());
        registratiButton.addActionListener(e -> new RegistrazioneFrame());

        setVisible(true);
    }

    private void login() {
        try {

            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            Utente utente = utenteService.login(username, password);

            JOptionPane.showMessageDialog(this, "Login effettuato");

            dispose();

            // =========================
            // QUI COSTRUISCI I SERVICE
            // =========================
            AutoService autoService = ServiceFactory.createAutoService();
            PrenotazioneService prenotazioneService = ServiceFactory.createPrenotazioneService();
            NoleggioService noleggioService = ServiceFactory.createNoleggioService();
            PagamentoService pagamentoService = ServiceFactory.createPagamentoService();
            ClienteService clienteService = ServiceFactory.createClienteService();

            new DashboardFrame(
                    utente,
                    autoService,
                    prenotazioneService,
                    noleggioService,
                    pagamentoService,
                    clienteService
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}