package gui;

import dao.AutoDAO;
import dao.PrenotazioneDAO;
import implementazionePostgresDAO.ImpAutoDAO;
import implementazionePostgresDAO.ImpPrenotazioneDAO;
import model.Auto;
import model.Cliente;
import model.Prenotazione;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class PrenotazionePanel
        extends JPanel {

    private JComboBox<Auto> autoComboBox;

    private Cliente cliente;

    public PrenotazionePanel(
            Cliente cliente
    ) {

        this.cliente = cliente;

        setLayout(
                new GridLayout(
                        5,
                        2,
                        10,
                        10
                )
        );

        JLabel autoLabel =
                new JLabel("Auto");

        autoComboBox =
                new JComboBox<>();

        JButton prenotaButton =
                new JButton("Prenota");

        add(autoLabel);
        add(autoComboBox);

        add(new JLabel());
        add(prenotaButton);

        caricaAutoDisponibili();

        prenotaButton.addActionListener(
                e -> prenotaAuto()
        );
    }

    private void caricaAutoDisponibili() {

        try {

            AutoDAO dao =
                    new ImpAutoDAO();

            for (
                    Auto auto :
                    dao.trovaAutoDisponibili()
            ) {

                autoComboBox
                        .addItem(auto);
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );
        }
    }

    private void prenotaAuto() {

        try {

            Auto auto =
                    (Auto)
                            autoComboBox
                                    .getSelectedItem();

            if (auto == null) {

                throw new IllegalArgumentException(
                        "Seleziona un'auto"
                );
            }

            Prenotazione prenotazione =
                    new Prenotazione(
                            generaId(),
                            new Date(),
                            new Date(
                                    System.currentTimeMillis()
                                            + 86400000
                            ),
                            Prenotazione
                                    .StatoPren
                                    .IN_ATTESA,
                            cliente,
                            auto
                    );

            PrenotazioneDAO dao =
                    new ImpPrenotazioneDAO();

            dao.salvaPrenotazione(
                    prenotazione
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Prenotazione effettuata!"
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );
        }
    }

    private int generaId() {

        return (int)
                (Math.random() * 100000);
    }
}