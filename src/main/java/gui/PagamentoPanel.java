package gui;

import controller.Controller;
import model.Pagamento;
import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class PagamentoPanel extends JPanel {
    private JPanel mainPanel;
    private JTable tablePagamenti;
    private DefaultTableModel model;
    private final Controller controller;

    public PagamentoPanel(Controller controller) {
        this.controller = controller;
        add(mainPanel);

        String[] colonne = {"ID", "Importo", "Stato"};
        model = new DefaultTableModel(colonne, 0);
        tablePagamenti.setModel(model);

        this.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) { caricaPagamenti(); }
            @Override
            public void ancestorRemoved(AncestorEvent event) {}
            @Override
            public void ancestorMoved(AncestorEvent event) {}
        });
    }

    private void caricaPagamenti() {
        try {
            model.setRowCount(0);
            int idCliente = controller.getUtenteLoggato().getIdUtente();
            List<Pagamento> pagamenti = controller.getPagamentiByCliente(idCliente);

            for (Pagamento p : pagamenti) {
                model.addRow(new Object[]{
                        p.getIdPagamento(),
                        p.getImporto() + " €",
                        p.getStato()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore caricamento pagamenti: " + e.getMessage());
        }
    }
}