package gui;

import model.Auto;
import service.AutoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class OperatorePanel extends JPanel {

    private JTable tabellaAuto;
    private DefaultTableModel tableModel;
    private final AutoService autoService;

    public OperatorePanel(AutoService autoService) {

        this.autoService = autoService;

        setLayout(new BorderLayout(10, 10));

        JLabel titolo = new JLabel("Pannello Operatore - Gestione Auto");
        titolo.setFont(new Font("Arial", Font.BOLD, 16));
        titolo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titolo, BorderLayout.NORTH);

        String[] colonne = {"ID", "Targa", "Modello", "Stato", "Costo Giornaliero"};

        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabellaAuto = new JTable(tableModel);
        add(new JScrollPane(tabellaAuto), BorderLayout.CENTER);

        JPanel azioniPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnManutenzione = new JButton("Manutenzione");
        JButton btnDisponibile = new JButton("Disponibile");
        JButton btnAggiorna = new JButton("Aggiorna");

        azioniPanel.add(btnManutenzione);
        azioniPanel.add(btnDisponibile);
        azioniPanel.add(btnAggiorna);

        add(azioniPanel, BorderLayout.SOUTH);

        btnAggiorna.addActionListener(e -> caricaDati());
        btnManutenzione.addActionListener(e -> cambiaStato(Auto.StatoAuto.IN_MANUTENZIONE));
        btnDisponibile.addActionListener(e -> cambiaStato(Auto.StatoAuto.DISPONIBILE));

        caricaDati();
    }

    private void caricaDati() {
        try {
            tableModel.setRowCount(0);

            List<Auto> autoList = autoService.getTutte();

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
            JOptionPane.showMessageDialog(this,
                    "Errore caricamento auto: " + e.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiaStato(Auto.StatoAuto nuovoStato) {

        int riga = tabellaAuto.getSelectedRow();

        if (riga == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un'auto");
            return;
        }

        int idAuto = (int) tableModel.getValueAt(riga, 0);

        try {
            autoService.cambiaStato(idAuto, nuovoStato);
            JOptionPane.showMessageDialog(this, "Stato aggiornato");
            caricaDati();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Errore aggiornamento: " + e.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}