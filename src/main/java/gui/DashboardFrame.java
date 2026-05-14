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

        setTitle("Noleggio Auto - " + utente.getNome() + " " + utente.getCognome());
        setSize(1100, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(45, 52, 54));
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel info = new JLabel("Utente: " + utente.getUsername() + " [" + utente.getClass().getSimpleName() + "]");
        info.setForeground(Color.WHITE);

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

        JTabbedPane tabs = new JTabbedPane();

        if (utente instanceof Operatore) {
            tabs.addTab("Auto", new AutoPanel(autoService, prenotazioneService, null));
            tabs.addTab("Prenotazioni", new PrenotazionePanel(null, prenotazioneService));
            tabs.addTab("Noleggi", new NoleggioPanel(noleggioService));

        }
        else if (utente instanceof Cliente c) {
            tabs.addTab("Auto disponibili", new AutoPanel(autoService, prenotazioneService, c));
            tabs.addTab("Le mie prenotazioni", new PrenotazionePanel(c, prenotazioneService));
            tabs.addTab("Pagamenti", new PagamentoPanel(pagamentoService, c));
            tabs.addTab("Profilo", new ClientePanel(c, clienteService, pagamentoService));
        }

        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }
}