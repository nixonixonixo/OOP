package gui;

import dao.AutoDAO;
import dao.PrenotazioneDAO;
import implementazionePostgresDAO.ImpAutoDAO;
import implementazionePostgresDAO.ImpPrenotazioneDAO;
import model.Auto;
import model.Cliente;
import model.Prenotazione;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class PrenotazionePanel extends JPanel {

    private JComboBox<Auto> autoComboBox;
    private JTable tabella;
    private DefaultTableModel model;
    private Cliente cliente;
    private PrenotazioneDAO prenotazioneDAO;
    private AutoDAO autoDAO;

    public PrenotazionePanel(Cliente cliente) {
        this.cliente = cliente;
        this.prenotazioneDAO = new ImpPrenotazioneDAO();
        this.autoDAO = new ImpAutoDAO();

        setLayout(new BorderLayout(10, 10));

        if (cliente != null) {
            JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
            formPanel.setBorder(BorderFactory.createTitledBorder("Nuova Prenotazione"));

            autoComboBox = new JComboBox<>();
            JButton prenotaButton = new JButton("Prenota");

            formPanel.add(new JLabel("Seleziona Auto:"));
            formPanel.add(autoComboBox);
            formPanel.add(new JLabel()); // Placeholder
            formPanel.add(prenotaButton);

            add(formPanel, BorderLayout.NORTH);

            prenotaButton.addActionListener(e -> prenotaAuto());
            caricaAutoDisponibili();
        }

        String[] colonne = {"ID", "Data Inizio", "Data Fine", "Stato", "Auto"};
        model = new DefaultTableModel(colonne, 0);
        tabella = new JTable(model);
        add(new JScrollPane(tabella), BorderLayout.CENTER);

        caricaPrenotazioni();
    }

    private void caricaPrenotazioni() {
        try {
            model.setRowCount(0);
            List<Prenotazione> lista;

            if (cliente != null) {
                lista = prenotazioneDAO.trovaPrenotazioniCliente(cliente.getIdUtente());
            } else {
                lista = prenotazioneDAO.trovaTuttePrenotazioni();
            }

            for (Prenotazione p : lista) {
                model.addRow(new Object[]{
                        p.getIdPrenotazione(),
                        p.getDataInizio(),
                        p.getDataFine(),
                        p.getStato(),
                        p.getAuto().getIdAuto()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errore caricamento: " + e.getMessage());
        }
    }

    private void caricaAutoDisponibili() {
        try {
            autoComboBox.removeAllItems();
            for (Auto auto : autoDAO.trovaAutoDisponibili()) {
                autoComboBox.addItem(auto);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void prenotaAuto() {
        try {
            Auto auto = (Auto) autoComboBox.getSelectedItem();
            if (auto == null) throw new IllegalArgumentException("Seleziona un'auto");

            Prenotazione p = new Prenotazione(
                    generaId(),
                    new Date(),
                    new Date(System.currentTimeMillis() + 86400000),
                    Prenotazione.StatoPren.IN_ATTESA,
                    cliente,
                    auto
            );

            prenotazioneDAO.salvaPrenotazione(p);
            JOptionPane.showMessageDialog(this, "Prenotazione effettuata!");

            caricaPrenotazioni(); // Aggiorna la tabella dopo la prenotazione
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private int generaId() {
        return (int) (Math.random() * 100000);
    }
}