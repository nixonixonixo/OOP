package gui;

import model.Cliente;
import model.Operatore;
import model.Utente;
import service.*;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame(
            Utente utente,
            AutoService autoService,
            PrenotazioneService prenotazioneService,
            NoleggioService noleggioService,
            PagamentoService pagamentoService,
            ClienteService clienteService
    ) {

        setTitle("Sistema Noleggio Auto - " + utente.getNome() + " " + utente.getCognome());
        setSize(1100, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(52, 73, 94));
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lbl = new JLabel("Benvenuto, " + utente.getUsername() + " (" + utente.getNome() + ")");
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));

        JButton logout = new JButton("Logout");

        logout.addActionListener(e -> {
            int scelta = JOptionPane.showConfirmDialog(this,
                    "Sei sicuro di voler uscire?", "Logout",
                    JOptionPane.YES_NO_OPTION);

            if (scelta == JOptionPane.YES_OPTION) {
                dispose();

                new LoginFrame(
                        utenteServiceDallaDashboard(utente, clienteService),
                        clienteService,
                        autoService,
                        prenotazioneService,
                        noleggioService,
                        pagamentoService
                );
            }
        });

        header.add(lbl, BorderLayout.WEST);
        header.add(logout, BorderLayout.EAST);

        JTabbedPane tabs = new JTabbedPane();

        if (utente instanceof Operatore) {
            tabs.addTab("Auto", new AutoPanel(autoService));
            tabs.addTab("Prenotazioni", new PrenotazionePanel(null, prenotazioneService));
            tabs.addTab("Noleggi", new NoleggioPanel(noleggioService));
        } else if (utente instanceof Cliente cliente) {
            tabs.addTab("Auto disponibili", new AutoPanel(autoService));
            tabs.addTab("Prenotazioni", new PrenotazionePanel(cliente, prenotazioneService));
            tabs.addTab("Cliente", new ClientePanel(cliente, clienteService));
            tabs.addTab("Pagamenti", new PagamentoPanel(pagamentoService));
            tabs.addTab("Noleggi", new NoleggioPanel(noleggioService));
        }

        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        setVisible(true);
    }

    private UtenteService utenteServiceDallaDashboard(Utente u, ClienteService cs) {
        return null;
    }
}