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

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(240, 240, 240));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel lblBenvenuto = new JLabel("Accesso effettuato come: " + utente.getUsername() + " (" + utente.getClass().getSimpleName() + ")");

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFocusable(false);
        btnLogout.setBackground(new Color(220, 53, 69));
        btnLogout.setForeground(Color.WHITE); // Testo bianco

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Vuoi davvero uscire?", "Conferma Logout",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginFrame();
            }
        });

        pnlHeader.add(lblBenvenuto, BorderLayout.WEST);
        pnlHeader.add(btnLogout, BorderLayout.EAST);

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
        add(pnlHeader, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        setVisible(true);
    }
}