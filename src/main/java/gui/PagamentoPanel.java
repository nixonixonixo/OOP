package gui;

import controller.Controller;
import model.Cliente;
import model.Pagamento;
import model.Utente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class PagamentoPanel extends JPanel {


    private JPanel mainPanel;
    private JPanel headerPanel;
    private JLabel lblSaldo;
    private JScrollPane scrollPane;
    private JTable table;
    private JPanel bottomPanel;
    private JButton btnPaga;
    private JButton btnAggiorna;

    private DefaultTableModel model;
    private final Controller controller;

    public PagamentoPanel(Controller controller) {
        this.controller = controller;


        add(mainPanel);


        model = new DefaultTableModel(
                new Object[]{"ID Pagamento", "Importo", "Stato"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table.setModel(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        btnPaga.setBackground(new Color(40, 167, 69));
        btnPaga.setForeground(Color.WHITE);


        btnAggiorna.addActionListener(e -> carica());
        btnPaga.addActionListener(e -> pagaSelezionato());


        carica();
    }

    private void carica() {
        try {
            model.setRowCount(0);


            Utente utenteLoggato = controller.getUtenteLoggato();
            if (utenteLoggato == null) {
                lblSaldo.setText("Nessun utente loggato");
                return;
            }


            int idUtente = utenteLoggato.getIdUtente();
            List<Pagamento> lista = controller.getPagamentiByCliente(idUtente);

            if (lista != null) {
                for (Pagamento p : lista) {
                    model.addRow(new Object[]{
                            p.getIdPagamento(),
                            p.getImporto() + " €",
                            p.getStato()
                    });
                }
            }


            if (utenteLoggato instanceof Cliente) {
                BigDecimal creditoAttuale = ((Cliente) utenteLoggato).getCredito();
                lblSaldo.setText("Il tuo saldo: " + creditoAttuale + " €");
            } else {
                lblSaldo.setText("Profilo Staff (Nessun saldo contabile)");
                btnPaga.setEnabled(false); // Un operatore/amministratore non effettua pagamenti personali
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errore caricamento pagamenti: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pagaSelezionato() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un pagamento dalla tabella", "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Utente utenteLoggato = controller.getUtenteLoggato();
        if (!(utenteLoggato instanceof Cliente)) {
            JOptionPane.showMessageDialog(this, "Azione consentita solo ai clienti", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cliente cliente = (Cliente) utenteLoggato;
        int idPagamento = (int) model.getValueAt(row, 0);

        try {

            String importoStr = model.getValueAt(row, 1).toString().replace(" €", "").trim();
            BigDecimal importoPagamento = new BigDecimal(importoStr);


            if (cliente.getCredito().compareTo(importoPagamento) < 0) {
                JOptionPane.showMessageDialog(this, "Credito insufficiente per completare il pagamento.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                return;
            }


            controller.effettuaPagamento(idPagamento, cliente.getIdUtente());


            cliente.setCredito(cliente.getCredito().subtract(importoPagamento));

            JOptionPane.showMessageDialog(this, "Pagamento completato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);


            carica();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore durante il pagamento: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}
