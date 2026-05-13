package gui;

import model.Auto;
import service.AutoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AutoPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private final AutoService autoService;

    public AutoPanel(AutoService autoService) {

        this.autoService = autoService;

        setLayout(new BorderLayout(10, 10));

        model = new DefaultTableModel(
                new Object[]{"ID", "Targa", "Modello", "Costo Giornaliero", "Stato"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnAggiorna = new JButton("Aggiorna Catalogo");
        btnAggiorna.addActionListener(e -> carica());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnAggiorna);

        add(bottom, BorderLayout.SOUTH);

        carica();
    }

    // =========================
    // CARICAMENTO AUTO
    // =========================
    private void carica() {
        try {
            model.setRowCount(0);

            List<Auto> lista = autoService.getAutoDisponibili();

            for (Auto a : lista) {
                model.addRow(new Object[]{
                        a.getIdAuto(),
                        a.getTarga(),
                        a.getModello(),
                        a.getCostoDaily() + " €",
                        a.getStato()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Errore caricamento auto: " + e.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}