package gui;

import dao.NoleggioDAO;
import implementazionePostgresDAO.ImpNoleggioDAO;
import model.Noleggio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NoleggioPanel
        extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public NoleggioPanel() {

        setLayout(new BorderLayout());

        model =
                new DefaultTableModel();

        model.addColumn("ID");
        model.addColumn("Data Ritiro");
        model.addColumn(
                "Data Restituzione"
        );
        model.addColumn(
                "Costo Totale"
        );

        table =
                new JTable(model);

        add(
                new JScrollPane(table),
                BorderLayout.CENTER
        );

        caricaNoleggi();
    }

    private void caricaNoleggi() {

        try {

            NoleggioDAO dao =
                    new ImpNoleggioDAO();

            List<Noleggio> noleggi =
                    dao.trovaTuttiNoleggi();

            for (Noleggio n :
                    noleggi) {

                model.addRow(
                        new Object[]{
                                n.getIdNoleggio(),
                                n.getDataRitiro(),
                                n.getDataRestituzione(),
                                n.getCostoTot()
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