package gui;

import model.Cliente;
import model.Operatore;
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

        // visibile a tutti
        tabs.addTab(
                "Auto",
                new AutoPanel()
        );

        // solo cliente
        if (utente instanceof Cliente cliente) {

            tabs.addTab(
                    "Prenotazioni",
                    new PrenotazionePanel(
                            cliente
                    )
            );

            tabs.addTab(
                    "Noleggi",
                    new NoleggioPanel()
            );

            tabs.addTab(
                    "Pagamenti",
                    new PagamentoPanel()
            );
        }

        if (utente instanceof Operatore) {

            tabs.addTab(
                    "Gestione Auto",
                    new AutoPanel()
            );

            tabs.addTab(
                    "Prenotazioni",
                    new PrenotazionePanel()
            );
        }

        setLayout(
                new BorderLayout()
        );

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