package gui;

import dao.NoleggioDAO;
import implementazionePostgresDAO.ImpNoleggioDAO;
import model.Noleggio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NoleggioPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private NoleggioDAO noleggioDAO;

    public NoleggioPanel() {
        this.noleggioDAO = new ImpNoleggioDAO();

        setLayout(new BorderLayout(10, 10));

        model = new DefaultTableModel(
                new Object[]{"ID", "Data Ritiro", "Data Restituzione", "Costo Totale"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton btnAggiorna = new JButton("Aggiorna Noleggi");
        btnAggiorna.addActionListener(e -> caricaNoleggi());

        JPanel pnlSud = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSud.add(btnAggiorna);
        add(pnlSud, BorderLayout.SOUTH);

        caricaNoleggi();
    }

    private void caricaNoleggi() {
        try {
            model.setRowCount(0);
            List<Noleggio> noleggi = noleggioDAO.trovaTuttiNoleggi();
            if (noleggi != null) {
                for (Noleggio n : noleggi) {
                    model.addRow(new Object[]{
                            n.getIdNoleggio(),
                            n.getDataRitiro(),
                            n.getDataRestituzione(),
                            n.getCostoTot() + " €"
                    });
                }
            }

            revalidate();
            repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Errore caricamento noleggi: " + e.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}