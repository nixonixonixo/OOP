package gui;

import model.Cliente;
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
    private final Cliente cliente;

    public PagamentoPanel(PagamentoService pagamentoService, Cliente cliente) {

        this.pagamentoService = pagamentoService;
        this.cliente = cliente;

        setLayout(new BorderLayout(10, 10));

        model = new DefaultTableModel(
                new Object[]{"ID", "Importo", "Stato"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnAggiorna = new JButton("Aggiorna");
        btnAggiorna.addActionListener(e -> carica());

        add(btnAggiorna, BorderLayout.SOUTH);

        carica();
    }

    private void carica() {
        try {
            model.setRowCount(0);

            List<Pagamento> lista =
                    pagamentoService.getPagamentiByCliente(cliente.getIdUtente());

            for (Pagamento p : lista) {
                model.addRow(new Object[]{
                        p.getIdPagamento(),
                        p.getImporto(),
                        p.getStato()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}