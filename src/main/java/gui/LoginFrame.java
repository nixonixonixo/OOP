package gui;

import model.Utente;
import service.*;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final UtenteService utenteService;
    private final ClienteService clienteService;
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
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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

            if (username.isEmpty() || password.isEmpty()) {
                throw new IllegalArgumentException("Inserisci credenziali valide");
            }

            Utente utente = utenteService.login(username, password);

            if (utente == null) {
                JOptionPane.showMessageDialog(this,
                        "Username o Password non corretti",
                        "Accesso Negato",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "Benvenuto " + utente.getNome());

            this.dispose();

            // Cambia la chiamata così:
            new DashboardFrame(
                    utente,
                    this.utenteService, // Passa il servizio così non sarà null al prossimo logout!
                    autoService,
                    prenotazioneService,
                    noleggioService,
                    pagamentoService,
                    clienteService
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Errore di sistema", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openRegistration() {
        new RegistrazioneFrame(utenteService, clienteService);
    }
}