package gui;

import model.Cliente;
import model.Operatore;
import model.Utente;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame(Utente utente) {
        setTitle("Dashboard Noleggio - " + utente.getNome());
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel lblBenvenuto = new JLabel("Accesso effettuato come: " + utente.getUsername());
        lblBenvenuto.setHorizontalAlignment(SwingConstants.RIGHT);
        lblBenvenuto.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTabbedPane tabs = new JTabbedPane();

        if (utente instanceof Operatore operatore) {
            tabs.addTab("Gestione Parco Auto", new AutoPanel());
            tabs.addTab("Pannello Operativo", new OperatorePanel());
            tabs.addTab("Tutte le Prenotazioni", new PrenotazionePanel(null));
        }
        else if (utente instanceof Cliente cliente) {
            tabs.addTab("Catalogo Auto", new AutoPanel());
            tabs.addTab("Le Mie Prenotazioni", new PrenotazionePanel(cliente));
            tabs.addTab("Il Mio Profilo", new ClientePanel(cliente));
            tabs.addTab("I Miei Pagamenti", new PagamentoPanel());
        }

        setLayout(new BorderLayout());
        add(lblBenvenuto, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        setVisible(true);
    }
}