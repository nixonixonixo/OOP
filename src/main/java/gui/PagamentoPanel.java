package gui;

import dao.PagamentoDAO;
import implementazionePostgresDAO.ImpPagamentoDAO;
import model.Pagamento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PagamentoPanel
        extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public PagamentoPanel() {

        setLayout(new BorderLayout());

        model =
                new DefaultTableModel();

        model.addColumn("ID");
        model.addColumn("Importo");
        model.addColumn("Stato");

        table =
                new JTable(model);

        add(
                new JScrollPane(table),
                BorderLayout.CENTER
        );

        caricaPagamenti();
    }

    private void caricaPagamenti() {

        try {

            PagamentoDAO dao =
                    new ImpPagamentoDAO();

            List<Pagamento> pagamenti =
                    dao.trovaTuttiPagamenti();

            for (Pagamento p :
                    pagamenti) {

                model.addRow(
                        new Object[]{
                                p.getIdPagamento(),
                                p.getImporto(),
                                p.getStato()
                        }
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );
        }
    }
}