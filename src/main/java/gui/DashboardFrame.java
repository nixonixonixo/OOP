package gui;

import model.Cliente;
import model.Operatore;
import model.Utente;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame(Utente utente) {
        setTitle("Sistema Noleggio Auto - " + utente.getNome() + " " + utente.getCognome());
        setSize(1100, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(52, 73, 94)); // Blu scuro elegante
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        String infoUtente = utente.getUsername() + " [" + utente.getClass().getSimpleName().toUpperCase() + "]";
        JLabel lblBenvenuto = new JLabel("Benvenuto, " + infoUtente);
        lblBenvenuto.setForeground(Color.WHITE);
        lblBenvenuto.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFocusable(false);
        btnLogout.setBackground(new Color(192, 57, 43)); // Rosso scuro
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Vuoi davvero uscire dal sistema?", "Conferma Logout",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginFrame();
            }
        });

        pnlHeader.add(lblBenvenuto, BorderLayout.WEST);
        pnlHeader.add(btnLogout, BorderLayout.EAST);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        if (utente instanceof Operatore operatore) {
            tabs.addTab("Gestione Auto", new AutoPanel());
            tabs.addTab("Prenotazioni Clienti", new PrenotazionePanel(null)); // null = tutte
            tabs.addTab("Gestione Noleggi Attivi", new NoleggioPanel()); // <--- AGGIUNTO QUI
            tabs.addTab("Pannello Operativo", new OperatorePanel());
        }
        else if (utente instanceof Cliente cliente) {
            tabs.addTab("Prenota Auto", new AutoPanel());
            tabs.addTab("Le Mie Prenotazioni", new PrenotazionePanel(cliente));
            tabs.addTab("I Miei Pagamenti", new PagamentoPanel());
            tabs.addTab("Profilo e Saldo", new ClientePanel(cliente));
        }

        setLayout(new BorderLayout());
        add(pnlHeader, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        setVisible(true);
    }
}