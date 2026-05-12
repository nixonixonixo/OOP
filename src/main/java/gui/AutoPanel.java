package gui;

import dao.AutoDAO;
import implementazionePostgresDAO.ImpAutoDAO;
import model.Auto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AutoPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public AutoPanel() {

        setLayout(new BorderLayout());

        model = new DefaultTableModel();

        model.addColumn("ID");
        model.addColumn("Targa");
        model.addColumn("Modello");
        model.addColumn("Costo Giornaliero");
        model.addColumn("Stato");

        table = new JTable(model);

        JScrollPane scrollPane =
                new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        JButton aggiornaButton =
                new JButton("Aggiorna");

        aggiornaButton.addActionListener(
                e -> caricaAuto()
        );

        add(
                aggiornaButton,
                BorderLayout.SOUTH
        );

        caricaAuto();
    }

    private void caricaAuto() {

        try {

            model.setRowCount(0);

            AutoDAO dao =
                    new ImpAutoDAO();

            List<Auto> auto =
                    dao.trovaTutteAuto();

            for (Auto a : auto) {

                model.addRow(
                        new Object[]{
                                a.getIdAuto(),
                                a.getTarga(),
                                a.getModello(),
                                a.getCostoDaily(),
                                a.getStato()
                        }
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}