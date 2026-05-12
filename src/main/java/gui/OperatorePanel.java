package gui;

import dao.AutoDAO;
import dao.PrenotazioneDAO;
import implementazionePostgresDAO.ImpAutoDAO;
import implementazionePostgresDAO.ImpPrenotazioneDAO;
import model.Auto;
import model.Prenotazione;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class OperatorePanel extends JPanel {

    private JTable tabellaAuto;
    private DefaultTableModel tableModel;
    private AutoDAO autoDAO;
    private PrenotazioneDAO prenotazioneDAO;

    public OperatorePanel() {
        this.autoDAO = new ImpAutoDAO();
        this.prenotazioneDAO = new ImpPrenotazioneDAO();

        setLayout(new BorderLayout(10, 10));

        JLabel titolo = new JLabel("Pannello Gestione Operatore - Monitoraggio Parco Auto");
        titolo.setFont(new Font("Arial", Font.BOLD, 16));
        titolo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titolo, BorderLayout.NORTH);

        String[] colonne = {"ID", "Targa", "Modello", "Stato", "Costo Daily"};
        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabellaAuto = new JTable(tableModel);
        add(new JScrollPane(tabellaAuto), BorderLayout.CENTER);

        JPanel azioniPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnManutenzione = new JButton("Invia in Manutenzione");
        JButton btnRendiDisponibile = new JButton("Rendi Disponibile");
        JButton btnAggiorna = new JButton("Aggiorna Lista");

        azioniPanel.add(btnManutenzione);
        azioniPanel.add(btnRendiDisponibile);
        azioniPanel.add(btnAggiorna);
        add(azioniPanel, BorderLayout.SOUTH);

        btnAggiorna.addActionListener(e -> caricaDati());

        btnManutenzione.addActionListener(e -> cambiaStatoSelezionato(Auto.StatoAuto.IN_MANUTENZIONE));

        btnRendiDisponibile.addActionListener(e -> cambiaStatoSelezionato(Auto.StatoAuto.DISPONIBILE));

        caricaDati();
    }

    private void caricaDati() {
        try {
            tableModel.setRowCount(0); // Pulisce la tabella
            List<Auto> autoList = autoDAO.trovaTutteAuto();
            for (Auto a : autoList) {
                tableModel.addRow(new Object[]{
                        a.getIdAuto(),
                        a.getTarga(),
                        a.getModello(),
                        a.getStato(),
                        a.getCostoDaily()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errore caricamento dati: " + e.getMessage());
        }
    }

    private void cambiaStatoSelezionato(Auto.StatoAuto nuovoStato) {
        int riga = tabellaAuto.getSelectedRow();
        if (riga == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un'auto dalla tabella!");
            return;
        }

        int idAuto = (int) tableModel.getValueAt(riga, 0);
        try {
            autoDAO.aggiornaStatoAuto(idAuto, nuovoStato);
            JOptionPane.showMessageDialog(this, "Stato aggiornato con successo!");
            caricaDati(); // Refresh
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errore durante l'aggiornamento: " + e.getMessage());
        }
    }
}