package gui;

import model.Pagamento;
import service.PagamentoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PagamentoPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private final PagamentoService pagamentoService;

    public PagamentoPanel(PagamentoService pagamentoService) {

        this.pagamentoService = pagamentoService;

        setLayout(new BorderLayout(10, 10));

        model = new DefaultTableModel(
                new Object[]{"ID", "Importo", "Stato"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnAggiorna = new JButton("Aggiorna Pagamenti");
        btnAggiorna.addActionListener(e -> carica());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnAggiorna);

        add(bottom, BorderLayout.SOUTH);

        carica();
    }

    private void carica() {
        try {
            model.setRowCount(0);

            List<Pagamento> lista = pagamentoService.getTuttiPagamenti();

            for (Pagamento p : lista) {
                model.addRow(new Object[]{
                        p.getIdPagamento(),
                        p.getImporto() + " €",
                        p.getStato()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Errore caricamento pagamenti: " + e.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}