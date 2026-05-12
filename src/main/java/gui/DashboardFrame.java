package gui;

import model.Cliente;
import model.Operatore;
import model.Utente;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame(Utente utente) {

        setTitle("Dashboard - " + (utente instanceof Operatore ? "Operatore" : "Cliente"));
        setSize(1000, 700); // Leggermente più grande per contenere le tabelle
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel benvenuto = new JLabel("Benvenuto, " + utente.getNome() + " " + utente.getCognome());
        benvenuto.setFont(new Font("Arial", Font.BOLD, 14));
        benvenuto.setHorizontalAlignment(SwingConstants.CENTER);
        benvenuto.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JTabbedPane tabs = new JTabbedPane();

        if (utente instanceof Cliente cliente) {
            tabs.addTab("Catalogo Auto", new AutoPanel());

            tabs.addTab("Le Mie Prenotazioni", new PrenotazionePanel(cliente));

            tabs.addTab("I Miei Noleggi", new NoleggioPanel());
            tabs.addTab("Pagamenti", new PagamentoPanel());
        }

        if (utente instanceof Operatore operatore) {
            tabs.addTab("Gestione Parco Auto", new AutoPanel());

            tabs.addTab("Gestione Prenotazioni", new PrenotazionePanel(null));

            tabs.addTab("Monitoraggio Operatore", new OperatorePanel());
        }

        setLayout(new BorderLayout());
        add(benvenuto, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        setVisible(true);
    }
}