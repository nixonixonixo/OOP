package gui;

import controller.Controller;
import model.Pagamento;
import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Pannello dell'interfaccia grafica per la gestione dei pagamenti del cliente.
 * <p>
 * Consente al cliente di visualizzare la lista dei propri pagamenti (in attesa o completati)
 * e di effettuare il saldo di quelli ancora in sospeso.
 */
public class PagamentoPanel extends JPanel {
    private JPanel mainPanel;
    private JTable tablePagamenti;
    private JButton btnPaga;
    private DefaultTableModel model;
    private final Controller controller;

    /**
     * Inizializza il pannello e configura la tabella di visualizzazione pagamenti.
     *
     * @param controller il controller di sistema
     */
    public PagamentoPanel(Controller controller) {
        this.controller = controller;
        add(mainPanel);

        String[] colonne = {"ID", "Importo", "Stato"};
        model = new DefaultTableModel(colonne, 0);
        tablePagamenti.setModel(model);

        btnPaga.addActionListener(e -> pagaSelezionato());

        // Listener per ricaricare i pagamenti quando il tab diventa visibile
        this.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                caricaPagamenti();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
            }
        });
    }

    /**
     * Gestisce la conferma del pagamento per la transazione selezionata nella tabella.
     */
    private void pagaSelezionato() {
        int row = tablePagamenti.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un pagamento in sospeso.");
            return;
        }

        int idPagamento = (int) model.getValueAt(row, 0);
        try {
            controller.confermaPagamento(idPagamento);
            JOptionPane.showMessageDialog(this, "Pagamento effettuato con successo!");
            caricaPagamenti();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
        }
    }

    /**
     * Recupera dal controller l'elenco dei pagamenti associati al cliente loggato
     * e aggiorna la tabella.
     */
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