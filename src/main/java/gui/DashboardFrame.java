package gui;

import model.Utente;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame
        extends JFrame {

    public DashboardFrame(
            Utente utente
    ) {

        setTitle("Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(
                EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        JLabel benvenuto =
                new JLabel(
                        "Benvenuto "
                                + utente.getNome()
                );

        benvenuto.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        JTabbedPane tabs =
                new JTabbedPane();

        tabs.addTab(
                "Auto",
                new AutoPanel()
        );

        tabs.addTab(
                "Prenotazioni",
                new PrenotazionePanel()
        );

        tabs.addTab(
                "Noleggi",
                new NoleggioPanel()
        );

        tabs.addTab(
                "Pagamenti",
                new PagamentoPanel()
        );

        setLayout(new BorderLayout());

        add(
                benvenuto,
                BorderLayout.NORTH
        );

        add(
                tabs,
                BorderLayout.CENTER
        );

        setVisible(true);
    }
}