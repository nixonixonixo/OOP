package gui;

import controller.Controller;
import model.Noleggio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;

public class NoleggioPanel extends JPanel {
    private JPanel mainPanel;
    private JTable tabellaNoleggi;
    private JButton btnChiudi;
    private DefaultTableModel model;
    private final Controller controller;

    public NoleggioPanel(Controller controller) throws SQLException {
        this.controller = controller;
        add(mainPanel);
        btnChiudi.addActionListener(e -> {
            try {
                chiudi();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        model = new DefaultTableModel(new String[]{"ID", "Cliente", "Auto", "Stato"}, 0);
        tabellaNoleggi.setModel(model);
        caricaNoleggi();
    }

    private void chiudi() throws Exception {
        int row = tabellaNoleggi.getSelectedRow();
        if (row != -1) {
            controller.terminaNoleggio((int) model.getValueAt(row, 0));
            caricaNoleggi();
        }
    }

    private void caricaNoleggi() throws SQLException {
        model.setRowCount(0);
        for (Noleggio n : controller.getNoleggiAttivi()) {
            model.addRow(new Object[]{
                    n.getIdNoleggio(),
                    n.getCliente().getNome() + " " + n.getCliente().getCognome(),
                    n.getAuto().getModello(),
                    "ATTIVO"
            });
        }
    }
}