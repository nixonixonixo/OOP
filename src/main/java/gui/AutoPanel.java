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
    private AutoDAO autoDAO;

    public AutoPanel() {
        // Inizializzazione DAO
        this.autoDAO = new ImpAutoDAO();

        setLayout(new BorderLayout(10, 10));

        model = new DefaultTableModel(
                new Object[]{"ID", "Targa", "Modello", "Costo Giornaliero", "Stato"}, 0
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

        JButton aggiornaButton = new JButton("Aggiorna Catalogo");
        aggiornaButton.setFont(new Font("Arial", Font.BOLD, 12));

        aggiornaButton.addActionListener(e -> caricaAuto());

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southPanel.add(aggiornaButton);
        add(southPanel, BorderLayout.SOUTH);

        caricaAuto();
    }

    public void caricaAuto() {
        try {
            model.setRowCount(0);
            List<Auto> listaAuto = autoDAO.trovaTutteAuto();

            if (listaAuto.isEmpty()) {
                System.out.println("Nessuna auto trovata nel database.");
            }

            for (Auto a : listaAuto) {
                model.addRow(new Object[]{
                        a.getIdAuto(),
                        a.getTarga(),
                        a.getModello(),
                        a.getCostoDaily() + " €",
                        a.getStato()
                });
            }

            revalidate();
            repaint();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Errore durante il caricamento delle auto: " + e.getMessage(),
                    "Errore Database",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}