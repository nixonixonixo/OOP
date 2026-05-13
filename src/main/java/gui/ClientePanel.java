package gui;

import dao.AutoDAO;
import dao.ClienteDAO;
import dao.PrenotazioneDAO;
import implementazionePostgresDAO.ImpAutoDAO;
import implementazionePostgresDAO.ImpClienteDAO;
import implementazionePostgresDAO.ImpPrenotazioneDAO;
import model.Auto;
import model.Cliente;
import model.Prenotazione;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

public class ClientePanel extends JPanel {

    private Cliente cliente

    private ClienteDAO clienteDAO;
    private PrenotazioneDAO prenotazioneDAO;
    private AutoDAO autoDAO;

    private JLabel lblCredito;
    private JLabel lblPatente;

    private JComboBox<Auto> cmbAuto;

    public ClientePanel(
            Cliente cliente
    ) {

        this.cliente = cliente;

        this.clienteDAO =
                new ImpClienteDAO();

        this.prenotazioneDAO =
                new ImpPrenotazioneDAO();

        this.autoDAO =
                new ImpAutoDAO();

        setLayout(
                new BorderLayout(
                        20,
                        20
                )
        );

        setBorder(
                BorderFactory
                        .createEmptyBorder(
                                30,
                                30,
                                30,
                                30
                        )
        );

        // HEADER

        JPanel headerPanel =
                new JPanel(
                        new GridLayout(
                                2,
                                1
                        )
                );

        JLabel lblNome =
                new JLabel(
                        cliente.getNome()
                                + " "
                                + cliente.getCognome()
                );

        lblNome.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        JLabel lblUser =
                new JLabel(
                        "Username: "
                                + cliente.getUsername()
                );

        headerPanel.add(lblNome);
        headerPanel.add(lblUser);

        // INFO ACCOUNT

        JPanel infoPanel =
                new JPanel(
                        new GridLayout(
                                2,
                                1,
                                10,
                                10
                        )
                );

        infoPanel.setBorder(
                BorderFactory
                        .createTitledBorder(
                                "Dettagli Account"
                        )
        );

        lblPatente =
                new JLabel(
                        "Patente: "
                                + cliente.getPatente()
                );

        lblCredito =
                new JLabel(
                        "Credito Residuo: "
                                + cliente.getCredito()
                                + " €"
                );

        lblCredito.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        lblCredito.setForeground(
                new Color(
                        0,
                        128,
                        0
                )
        );

        infoPanel.add(lblPatente);
        infoPanel.add(lblCredito);

        // RICARICA

        JPanel ricaricaPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        ricaricaPanel.setBorder(
                BorderFactory
                        .createTitledBorder(
                                "Ricarica Credito"
                        )
        );

        JTextField txtRicarica =
                new JTextField(10);

        JButton btnRicarica =
                new JButton(
                        "Ricarica Ora"
                );

        ricaricaPanel.add(
                new JLabel(
                        "Importo (€):"
                )
        );

        ricaricaPanel.add(
                txtRicarica
        );

        ricaricaPanel.add(
                btnRicarica
        );

        btnRicarica.addActionListener(
                e -> {

                    try {

                        BigDecimal importo =
                                new BigDecimal(
                                        txtRicarica
                                                .getText()
                                );

                        if (
                                importo.compareTo(
                                        BigDecimal.ZERO
                                ) <= 0
                        ) {

                            throw new NumberFormatException();
                        }

                        BigDecimal nuovoCredito =
                                cliente
                                        .getCredito()
                                        .add(importo);

                        clienteDAO
                                .aggiornaCredito(
                                        cliente.getIdUtente(),
                                        nuovoCredito
                                );

                        cliente.setCredito(
                                nuovoCredito
                        );

                        lblCredito.setText(
                                "Credito Residuo: "
                                        + cliente.getCredito()
                                        + " €"
                        );

                        txtRicarica.setText("");

                        JOptionPane
                                .showMessageDialog(
                                        this,
                                        "Ricarica effettuata!"
                                );

                    } catch (
                            Exception ex
                    ) {

                        JOptionPane
                                .showMessageDialog(
                                        this,
                                        "Importo non valido"
                                );
                    }
                }
        );

        // PRENOTAZIONE

        JPanel prenotazionePanel =
                new JPanel(
                        new GridLayout(
                                3,
                                2,
                                10,
                                10
                        )
                );

        prenotazionePanel.setBorder(
                BorderFactory
                        .createTitledBorder(
                                "Prenota Auto"
                        )
        );

        cmbAuto =
                new JComboBox<>();

        caricaAutoDisponibili();

        JButton btnPrenota =
                new JButton(
                        "Prenota"
                );

        prenotazionePanel.add(
                new JLabel(
                        "Auto disponibile:"
                )
        );

        prenotazionePanel.add(
                cmbAuto
        );

        prenotazionePanel.add(
                new JLabel()
        );

        prenotazionePanel.add(
                btnPrenota
        );

        btnPrenota
                .addActionListener(
                        e -> prenotaAuto()
                );

        JPanel centerPanel =
                new JPanel(
                        new BorderLayout()
                );

        centerPanel.add(
                infoPanel,
                BorderLayout.NORTH
        );

        centerPanel.add(
                prenotazionePanel,
                BorderLayout.CENTER
        );

        add(
                headerPanel,
                BorderLayout.NORTH
        );

        add(
                centerPanel,
                BorderLayout.CENTER
        );

        add(
                ricaricaPanel,
                BorderLayout.SOUTH
        );
    }

    private void caricaAutoDisponibili() {

        try {

            cmbAuto.removeAllItems();

            for (
                    Auto auto :
                    autoDAO
                            .trovaAutoDisponibili()
            ) {

                cmbAuto.addItem(auto);
            }

        } catch (Exception e) {

            JOptionPane
                    .showMessageDialog(
                            this,
                            e.getMessage()
                    );
        }
    }

    private void prenotaAuto() {

        try {

            Auto auto =
                    (Auto)
                            cmbAuto
                                    .getSelectedItem();

            if (auto == null) {

                JOptionPane
                        .showMessageDialog(
                                this,
                                "Nessuna auto disponibile"
                        );

                return;
            }

            Date oggi =
                    new Date();

            Calendar calendar =
                    Calendar.getInstance();

            calendar.add(
                    Calendar.DAY_OF_MONTH,
                    1
            );

            Date domani =
                    calendar.getTime();

            Prenotazione prenotazione =
                    new Prenotazione(
                            generaId(),
                            oggi,
                            domani,
                            Prenotazione
                                    .StatoPren
                                    .IN_ATTESA,
                            cliente,
                            auto
                    );

            prenotazioneDAO
                    .salvaPrenotazione(
                            prenotazione
                    );

            JOptionPane
                    .showMessageDialog(
                            this,
                            "Prenotazione effettuata!"
                    );

        } catch (Exception e) {

            JOptionPane
                    .showMessageDialog(
                            this,
                            e.getMessage()
                    );
        }
    }

    private int generaId() {

        return (int)
                (Math.random()
                        * 100000);
    }
}