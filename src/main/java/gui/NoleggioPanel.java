package gui;

import dao.NoleggioDAO;
import implementazionePostgresDAO.ImpNoleggioDAO;
import implementazionePostgresDAO.ImpAutoDAO;
import model.Noleggio;
import model.Auto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NoleggioPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private NoleggioDAO noleggioDAO;

    public NoleggioPanel() {
        this.noleggioDAO = new ImpNoleggioDAO();

        setLayout(new BorderLayout(10, 10));

        model = new DefaultTableModel(
                new Object[]{"ID", "Inizio (Ritiro)", "Fine (Prevista/Effettiva)", "Costo Totale", "Stato"}, 0
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

        // Pulsanti
        JButton btnAggiorna = new JButton("Aggiorna");
        JButton btnTermina = new JButton("Termina Noleggio (Restituzione)");

        btnTermina.setBackground(new Color(46, 204, 113)); // Un bel verde per la chiusura
        btnTermina.setForeground(Color.WHITE);

        btnAggiorna.addActionListener(e -> caricaNoleggi());
        btnTermina.addActionListener(e -> terminaNoleggioSelezionato());

        JPanel pnlSud = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSud.add(btnTermina);
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
                    // Se la data restituzione è null, il noleggio è ancora attivo
                    String stato = (n.getDataRestituzione() == null) ? "ATTIVO" : "CHIUSO";
                    String costo = (n.getCostoTot() == null) ? "Da calcolare" : n.getCostoTot() + " €";

                    model.addRow(new Object[]{
                            n.getIdNoleggio(),
                            n.getDataRitiro(),
                            (n.getDataRestituzione() == null) ? "In corso..." : n.getDataRestituzione(),
                            costo,
                            stato
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore caricamento: " + e.getMessage());
        }
    }

    private void terminaNoleggioSelezionato() {
        int riga = table.getSelectedRow();
        if (riga == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un noleggio attivo dalla tabella.");
            return;
        }

        int idNoleggio = (int) model.getValueAt(riga, 0);
        String stato = (String) model.getValueAt(riga, 4);

        if (stato.equals("CHIUSO")) {
            JOptionPane.showMessageDialog(this, "Questo noleggio è già stato terminato.");
            return;
        }

        try {
            Noleggio n = noleggioDAO.trovaNoleggioPerId(idNoleggio);
            if (n == null) return;

            Date oggi = new Date();
            long diffInMillies = Math.abs(oggi.getTime() - n.getDataRitiro().getTime());
            long giorni = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if (giorni <= 0) giorni = 1;

            BigDecimal tariffaGiorno = n.getPrenotazione().getAuto().getCostoDaily();
            BigDecimal costoFinale = tariffaGiorno.multiply(new BigDecimal(giorni));

            int opzione = JOptionPane.showConfirmDialog(this,
                    "Restituzione Auto: " + n.getPrenotazione().getAuto().getModello() + "\n" +
                            "Giorni trascorsi: " + giorni + "\n" +
                            "Costo Totale da addebitare: " + costoFinale + " €\n\n" +
                            "Confermare la restituzione?",
                    "Chiusura Noleggio", JOptionPane.YES_NO_OPTION);

            if (opzione == JOptionPane.YES_OPTION) {
                n.setDataRestituzione(oggi);
                n.setCostoTot(costoFinale);

                noleggioDAO.aggiornaNoleggio(n);

                new ImpAutoDAO().aggiornaStatoAuto(n.getPrenotazione().getAuto().getIdAuto(), Auto.StatoAuto.DISPONIBILE);

                JOptionPane.showMessageDialog(this, "Noleggio terminato con successo!");
                caricaNoleggi();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore durante la chiusura: " + e.getMessage());
            e.printStackTrace();
        }
    }
}