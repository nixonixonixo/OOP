package gui;

import model.Noleggio;
import service.NoleggioService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NoleggioPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private final NoleggioService noleggioService;

    public NoleggioPanel(NoleggioService service) {
        this.noleggioService = service;

        setLayout(new BorderLayout(10, 10));

        model = new DefaultTableModel(
                new Object[]{"ID", "Ritiro", "Restituzione", "Costo", "Stato"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnAggiorna = new JButton("Aggiorna");
        JButton btnChiudi = new JButton("Chiudi Noleggio");

        btnAggiorna.addActionListener(e -> carica());
        btnChiudi.addActionListener(e -> chiudiSelezionato());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnChiudi);
        bottom.add(btnAggiorna);

        add(bottom, BorderLayout.SOUTH);

        carica();
    }

    // =========================
    // CARICAMENTO DATI
    // =========================
    private void carica() {
        try {
            model.setRowCount(0);

            List<Noleggio> lista = noleggioService.getTuttiNoleggi();

            for (Noleggio n : lista) {

                String stato = (n.getDataRestituzione() == null)
                        ? "ATTIVO"
                        : "CHIUSO";

                String costo = (n.getCostoTot() == null)
                        ? "Da calcolare"
                        : n.getCostoTot().toString();

                model.addRow(new Object[]{
                        n.getIdNoleggio(),
                        n.getDataRitiro(),
                        n.getDataRestituzione() == null ? "-" : n.getDataRestituzione(),
                        costo,
                        stato
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Errore caricamento: " + e.getMessage());
        }
    }

    // =========================
    // CHIUSURA NOLEGGIO
    // =========================
    private void chiudiSelezionato() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un noleggio");
            return;
        }

        try {
            int id = (int) model.getValueAt(row, 0);

            noleggioService.chiudiNoleggio(id, null);

            JOptionPane.showMessageDialog(this, "Noleggio chiuso con successo");

            carica();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Errore: " + e.getMessage());
        }
    }
}