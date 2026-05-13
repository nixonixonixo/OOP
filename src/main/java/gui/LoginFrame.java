package gui;

import model.Utente;
import service.*;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final UtenteService utenteService;
    private final ClienteService clienteService;
    // Teniamo i riferimenti per passarli alla Dashboard dopo il login
    private final AutoService autoService;
    private final PrenotazioneService prenotazioneService;
    private final NoleggioService noleggioService;
    private final PagamentoService pagamentoService;

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame(UtenteService utenteService, ClienteService clienteService,
                      AutoService autoService, PrenotazioneService prenotazioneService,
                      NoleggioService noleggioService, PagamentoService pagamentoService) {

        this.utenteService = utenteService;
        this.clienteService = clienteService;
        this.autoService = autoService;
        this.prenotazioneService = prenotazioneService;
        this.noleggioService = noleggioService;
        this.pagamentoService = pagamentoService;

        setTitle("Login - Noleggio Auto");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Registrati");

        panel.add(new JLabel("Username")); panel.add(usernameField);
        panel.add(new JLabel("Password")); panel.add(passwordField);
        panel.add(loginButton); panel.add(registerButton);

        add(panel, BorderLayout.CENTER);

        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> openRegistration());
    }

    private void login() {
        try {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            Utente utente = utenteService.login(username, password);
            JOptionPane.showMessageDialog(this, "Benvenuto " + utente.getUsername());

            dispose();
            // Qui apriresti la Dashboard passandogli i service necessari
            // new DashboardFrame(utente, autoService, ...);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openRegistration() {
        // Passiamo i service necessari alla registrazione
        new RegistrazioneFrame(utenteService, clienteService);
    }
}