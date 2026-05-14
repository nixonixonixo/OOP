package gui;

import model.Cliente;
import model.Operatore;
import model.Utente;
import service.*;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private final Utente utente;

    private final UtenteService utenteService;
    private final AutoService autoService;
    private final PrenotazioneService prenotazioneService;
    private final NoleggioService noleggioService;
    private final PagamentoService pagamentoService;
    private final ClienteService clienteService;

    public DashboardFrame(
            Utente utente,
            UtenteService utenteService,
            AutoService autoService,
            PrenotazioneService prenotazioneService,
            NoleggioService noleggioService,
            PagamentoService pagamentoService,
            ClienteService clienteService
    ) {

        this.utente = utente;
        this.utenteService = utenteService;
        this.autoService = autoService;
        this.prenotazioneService = prenotazioneService;
        this.noleggioService = noleggioService;
        this.pagamentoService = pagamentoService;
        this.clienteService = clienteService;

        if (this.utente == null) {
            JOptionPane.showMessageDialog(null, "Sessione non valida.");
            return;
        }

        // Titolo dinamico con indicazione del ruolo
        String tipoUtente = utente instanceof Operatore ? "Operatore" : "Cliente";
        setTitle("Noleggio Auto - " + utente.getNome() + " " + utente.getCognome() + " (" + tipoUtente + ")");

        setSize(1100, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(45, 52, 54));
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel info = new JLabel("Utente: " + utente.getUsername() + " [" + utente.getClass().getSimpleName() + "]");
        info.setForeground(Color.WHITE);
        info.setFont(new Font("Arial", Font.BOLD, 14));

        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> {
            dispose();
            new LoginFrame(
                    this.utenteService,
                    this.clienteService,
                    this.autoService,
                    this.prenotazioneService,
                    this.noleggioService,
                    this.pagamentoService
            );
        });

        header.add(info, BorderLayout.WEST);
        header.add(logout, BorderLayout.EAST);

        // --- TABS ---
        JTabbedPane tabs = new JTabbedPane();

        // LOGICA PER OPERATORE
        if (utente instanceof Operatore op) {
            // Adesso passiamo 'op' invece di 'null'!
            tabs.addTab("Gestione Parco Auto", new AutoPanel(autoService, prenotazioneService, op));
            tabs.addTab("Tutte le Prenotazioni", new PrenotazionePanel(null, prenotazioneService));
            tabs.addTab("Gestione Noleggi", new NoleggioPanel(noleggioService));
        }
        // LOGICA PER CLIENTE
        else if (utente instanceof Cliente c) {
            tabs.addTab("Catalogo Auto", new AutoPanel(autoService, prenotazioneService, c));
            tabs.addTab("Le mie Prenotazioni", new PrenotazionePanel(c, prenotazioneService));
            tabs.addTab("Pagamenti e Credito", new PagamentoPanel(pagamentoService, c));
            tabs.addTab("Il mio Profilo", new ClientePanel(c, clienteService, pagamentoService));
        }

        // --- LAYOUT FINALE ---
        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }
}