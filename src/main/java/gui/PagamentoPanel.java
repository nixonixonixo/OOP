package gui;

import model.Cliente;
import model.Pagamento;
import service.PagamentoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class PagamentoPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JLabel lblSaldo; // Per mostrare il credito attuale

    private final PagamentoService pagamentoService;
    private final Cliente cliente;

    public PagamentoPanel(PagamentoService pagamentoService, Cliente cliente) {
        this.pagamentoService = pagamentoService;
        this.cliente = cliente;

        setLayout(new BorderLayout(10, 10));

        // Header con Saldo Attuale
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblSaldo = new JLabel("Il tuo saldo: " + cliente.getCredito() + " €");
        lblSaldo.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.add(lblSaldo);
        add(header, BorderLayout.NORTH);

        // Tabella
        model = new DefaultTableModel(
                new Object[]{"ID Pagamento", "Importo", "Stato"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottoni
        JButton btnAggiorna = new JButton("Aggiorna");
        JButton btnPaga = new JButton("Paga Selezionato");

        btnPaga.setBackground(new Color(40, 167, 69)); // Verde
        btnPaga.setForeground(Color.WHITE);

        btnAggiorna.addActionListener(e -> carica());
        btnPaga.addActionListener(e -> pagaSelezionato());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnPaga);
        bottom.add(btnAggiorna);

        add(bottom, BorderLayout.SOUTH);

        carica();
    }

    private void carica() {
        try {
            model.setRowCount(0);

            // Ricarichiamo i pagamenti dal service
            List<Pagamento> lista = pagamentoService.getPagamentiByCliente(cliente.getIdUtente());

            for (Pagamento p : lista) {
                model.addRow(new Object[]{
                        p.getIdPagamento(),
                        p.getImporto() + " €",
                        p.getStato()
                });
            }

            // Aggiorniamo anche la label del saldo (utile se ha appena pagato)
            lblSaldo.setText("Il tuo saldo: " + cliente.getCredito() + " €");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore caricamento: " + e.getMessage());
        }
    }

    private void pagaSelezionato() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int idPagamento = (int) model.getValueAt(row, 0);

        try {
            // 1. Esegue il pagamento sul DB
            pagamentoService.effettuaPagamento(idPagamento, cliente.getIdUtente());

            // 2. RECUPERA IL SALDO AGGIORNATO DAL SERVICE
            // Non serve il clienteDAO qui, chiediamo al service!
            BigDecimal nuovoSaldo = pagamentoService.getSaldoAggiornato(cliente.getIdUtente());

            // 3. AGGIORNA L'OGGETTO CLIENTE LOCALE
            cliente.setCredito(nuovoSaldo);

            JOptionPane.showMessageDialog(this, "Pagamento completato!");

            // 4. RICARICA LA TABELLA E LA LABEL
            carica();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore: " + e.getMessage());
        }
    }
}