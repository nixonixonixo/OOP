package gui;

import model.Auto;
import model.Cliente;
import service.AutoService;
import service.PrenotazioneService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AutoPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private final AutoService autoService;
    private final PrenotazioneService prenotazioneService;
    private final Cliente clienteLoggato;

    public AutoPanel(AutoService autoService,
                     PrenotazioneService prenotazioneService,
                     Cliente cliente) {

        this.autoService = autoService;
        this.prenotazioneService = prenotazioneService;
        this.clienteLoggato = cliente;

        setLayout(new BorderLayout(10, 10));

        model = new DefaultTableModel(
                new Object[]{"ID", "Targa", "Modello", "Costo Giornaliero", "Stato"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnAggiorna = new JButton("Aggiorna");
        JButton btnPrenota = new JButton("Prenota");

        btnAggiorna.addActionListener(e -> carica());
        btnPrenota.addActionListener(e -> prenota());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnPrenota);
        bottom.add(btnAggiorna);

        add(bottom, BorderLayout.SOUTH);

        carica();
    }

    private void carica() {
        try {
            model.setRowCount(0);

            List<Auto> lista = autoService.getAutoDisponibili();

            for (Auto a : lista) {
                model.addRow(new Object[]{
                        a.getIdAuto(),
                        a.getTarga(),
                        a.getModello(),
                        a.getCostoDaily(),
                        a.getStato()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void prenota() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un'auto");
            return;
        }

        try {
            int idAuto = (int) model.getValueAt(row, 0);

            prenotazioneService.creaPrenotazione(clienteLoggato.getIdUtente(), idAuto);

            JOptionPane.showMessageDialog(this, "Prenotazione inviata");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}