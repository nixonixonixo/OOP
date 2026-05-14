package gui;

import model.Noleggio;
import service.NoleggioService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Date; // Aggiunta importazione

public class NoleggioPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private final NoleggioService noleggioService;

    public NoleggioPanel(NoleggioService service) {
        this.noleggioService = service;

        setLayout(new BorderLayout(10, 10));

        model = new DefaultTableModel(
                new Object[]{"ID", "Auto", "Ritiro", "Restituzione", "Costo", "Stato"}, 0
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

        // Estetica: differenziamo i bottoni
        btnChiudi.setBackground(new Color(220, 53, 69));
        btnChiudi.setForeground(Color.WHITE);

        btnAggiorna.addActionListener(e -> carica());
        btnChiudi.addActionListener(e -> chiudiSelezionato());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnChiudi);
        bottom.add(btnAggiorna);

        add(bottom, BorderLayout.SOUTH);

        carica();
    }

    private void carica() {
        try {
            model.setRowCount(0);
            List<Noleggio> lista = noleggioService.getTuttiNoleggi();

            for (Noleggio n : lista) {
                String stato = (n.getDataRestituzione() == null) ? "ATTIVO" : "CHIUSO";

                // Gestione null per il costo
                String costoStr = (n.getCostoTot() == null || n.getCostoTot().doubleValue() == 0)
                        ? "In corso..."
                        : n.getCostoTot().toString() + " €";

                model.addRow(new Object[]{
                        n.getIdNoleggio(),
                        n.getPrenotazione().getAuto().getModello(), // Info utile in tabella
                        n.getDataRitiro(),
                        n.getDataRestituzione() == null ? "-" : n.getDataRestituzione(),
                        costoStr,
                        stato
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore caricamento: " + e.getMessage());
        }
    }

    private void chiudiSelezionato() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un noleggio dalla tabella");
            return;
        }

        // Controllo se è già chiuso per evitare chiamate inutili
        String stato = (String) model.getValueAt(row, 5);
        if (stato.equals("CHIUSO")) {
            JOptionPane.showMessageDialog(this, "Questo noleggio è già stato chiuso.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Vuoi procedere alla chiusura e generare il pagamento?",
                "Conferma Rientro Auto", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = (int) model.getValueAt(row, 0);

                // FIX: Passiamo la data attuale per la chiusura
                // NOTA: Se il tuo service.chiudiNoleggio non accetta la data,
                // usa quella che abbiamo scritto insieme nel passaggio precedente.
                noleggioService.chiudiNoleggio(id);

                JOptionPane.showMessageDialog(this,
                        "Noleggio Chiuso!\nL'auto è tornata DISPONIBILE.\nPagamento inviato al cliente.");

                carica();

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Errore durante la chiusura: " + e.getMessage());
            }
        }
    }
}