package gui;

import controller.Controller;
import model.Noleggio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class NoleggioPanel extends JPanel {

    private JPanel mainPanel;
    private JTable table;
    private JButton btnAggiorna;
    private JButton btnChiudi;
    private JScrollPane scrollPane;
    private JPanel bottomPanel;

    private DefaultTableModel model;
    private final Controller controller;

    public NoleggioPanel(Controller controller) {
        this.controller = controller;

        add(mainPanel);

        model = new DefaultTableModel(
                new Object[]{"ID", "Auto", "Ritiro", "Restituzione", "Costo", "Stato"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(model);
        table.getTableHeader().setReorderingAllowed(false);

        btnChiudi.setBackground(new Color(220, 53, 69));
        btnChiudi.setForeground(Color.WHITE);

        btnAggiorna.addActionListener(e -> carica());
        btnChiudi.addActionListener(e -> chiudiSelezionato());

        carica();
    }

    private void carica() {
        try {
            model.setRowCount(0);
            List<Noleggio> lista = controller.getTuttiNoleggi();

            for (Noleggio n : lista) {
                String stato = (n.getDataRestituzione() == null) ? "ATTIVO" : "CHIUSO";

                String costoStr = (n.getCostoTot() == null || n.getCostoTot().doubleValue() == 0)
                        ? "In corso..."
                        : n.getCostoTot().toString() + " €";

                model.addRow(new Object[]{
                        n.getIdNoleggio(),
                        n.getPrenotazione().getAuto().getModello(),
                        n.getDataRitiro(),
                        n.getDataRestituzione() == null ? "-" : n.getDataRestituzione(),
                        costoStr,
                        stato
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errore caricamento noleggi: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void chiudiSelezionato() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un noleggio dalla tabella", "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String stato = (String) model.getValueAt(row, 5);
        if (stato.equals("CHIUSO")) {
            JOptionPane.showMessageDialog(this, "Questo noleggio è già stato chiuso.", "Azione non consentita", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Vuoi procedere alla chiusura e generare il pagamento?",
                "Conferma Rientro Auto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = (int) model.getValueAt(row, 0);

                controller.chiudiNoleggio(id);

                JOptionPane.showMessageDialog(this,
                        "Noleggio Chiuso!\nL'auto è tornata DISPONIBILE.\nPagamento inviato al cliente.",
                        "Successo", JOptionPane.INFORMATION_MESSAGE);

                carica();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Errore durante la chiusura: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}