package gui;

import dao.PagamentoDAO;
import implementazionePostgresDAO.ImpPagamentoDAO;
import model.Pagamento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PagamentoPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private PagamentoDAO pagamentoDAO;

    public PagamentoPanel() {
        this.pagamentoDAO = new ImpPagamentoDAO();

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
        btnAggiorna.addActionListener(e -> caricaPagamenti());

        JPanel pnlSud = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSud.add(btnAggiorna);
        add(pnlSud, BorderLayout.SOUTH);

        caricaPagamenti();
    }

    private void caricaPagamenti() {
        try {
            model.setRowCount(0);

            List<Pagamento> pagamenti = pagamentoDAO.trovaTuttiPagamenti();

            if (pagamenti != null) {
                for (Pagamento p : pagamenti) {
                    model.addRow(new Object[]{
                            p.getIdPagamento(),
                            p.getImporto() + " €",
                            p.getStato()
                    });
                }
            }

            revalidate();
            repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Errore nel caricamento pagamenti: " + e.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}