package gui;

import model.Cliente;
import model.Operatore;
import model.Utente;
import service.*;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private final Utente utente;

    private final AutoService autoService;
    private final PrenotazioneService prenotazioneService;
    private final NoleggioService noleggioService;
    private final PagamentoService pagamentoService;
    private final ClienteService clienteService;

    public DashboardFrame(
            Utente utente,
            AutoService autoService,
            PrenotazioneService prenotazioneService,
            NoleggioService noleggioService,
            PagamentoService pagamentoService,
            ClienteService clienteService
    ) {

        this.utente = utente;
        this.autoService = autoService;
        this.prenotazioneService = prenotazioneService;
        this.noleggioService = noleggioService;
        this.pagamentoService = pagamentoService;
        this.clienteService = clienteService;

        setTitle("Noleggio Auto - " + utente.getNome() + " " + utente.getCognome());
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

        JLabel info = new JLabel("Utente collegato: " + utente.getUsername() + " (" + utente.getClass().getSimpleName() + ")");
        info.setForeground(Color.WHITE);
        info.setFont(new Font("SansSerif", Font.BOLD, 14));

        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> {
            dispose();
            // Assicurati che LoginFrame accetti questi parametri nel costruttore
            new LoginFrame(
                    utenteServiceFake(),
                    clienteService,
                    autoService,
                    prenotazioneService,
                    noleggioService,
                    pagamentoService
            );
        });

        header.add(info, BorderLayout.WEST);
        header.add(logout, BorderLayout.EAST);

        // --- TABS ---
        JTabbedPane tabs = new JTabbedPane();

        // LOGICA PER OPERATORE
        if (utente instanceof Operatore) {

            // AutoPanel richiede: AutoService, PrenotazioneService, Cliente (null per operatore)
            tabs.addTab("Gestione Auto",
                    new AutoPanel(autoService, prenotazioneService, null));

            // PrenotazionePanel richiede: Cliente (null per operatore), PrenotazioneService
            tabs.addTab("Tutte le Prenotazioni",
                    new PrenotazionePanel(null, prenotazioneService));

            tabs.addTab("Gestione Noleggi",
                    new NoleggioPanel(noleggioService));

            // PagamentoPanel richiede: PagamentoService, Cliente (null per operatore)
            tabs.addTab("Tutti i Pagamenti",
                    new PagamentoPanel(pagamentoService, null));
        }

        // LOGICA PER CLIENTE
        else if (utente instanceof Cliente cliente) {

            // AutoPanel richiede: AutoService, PrenotazioneService, Cliente
            tabs.addTab("Catalogo Auto",
                    new AutoPanel(autoService, prenotazioneService, cliente));

            // PrenotazionePanel richiede: Cliente, PrenotazioneService
            tabs.addTab("Le mie Prenotazioni",
                    new PrenotazionePanel(cliente, prenotazioneService));

            // PagamentoPanel richiede: PagamentoService, Cliente
            tabs.addTab("I miei Pagamenti",
                    new PagamentoPanel(pagamentoService, cliente));

            tabs.addTab("Il mio Profilo",
                    new ClientePanel(cliente, clienteService));
        }

        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    // Metodo fittizio come da tuo codice originale
    private UtenteService utenteServiceFake() {
        return null;
    }
}