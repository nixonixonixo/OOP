package gui;

import controller.Controller;
import model.Auto;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

/**
 * Pannello dell'interfaccia grafica dedicato agli operatori.
 * <p>
 * Offre strumenti per monitorare il parco auto e modificarne lo stato operativo
 * (es. invio in manutenzione o ripristino della disponibilità).
 */
public class OperatorePanel extends JPanel {

    private JPanel mainPanel;
    private JLabel lblTitolo;
    private JTable tabellaAuto;
    private JScrollPane scrollPane;
    private JPanel azioniPanel;
    private JButton btnManutenzione;
    private JButton btnDisponibile;
    private JButton btnAggiorna;

    private DefaultTableModel tableModel;
    private final Controller controller;

    /**
     * Inizializza il pannello e configura la tabella di visualizzazione auto.
     *
     * @param controller il controller di sistema per gestire le operazioni sulle auto
     */
    public OperatorePanel(Controller controller) {
        this.controller = controller;

        add(mainPanel);

        String[] colonne = {"ID", "Targa", "Modello", "Stato", "Costo Giornaliero"};
        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabellaAuto.setModel(tableModel);
        tabellaAuto.getTableHeader().setReorderingAllowed(false);

        btnAggiorna.addActionListener(e -> caricaDati());
        btnManutenzione.addActionListener(e -> cambiaStato(Auto.StatoAuto.IN_MANUTENZIONE));
        btnDisponibile.addActionListener(e -> cambiaStato(Auto.StatoAuto.DISPONIBILE));

        caricaDati();
    }

    /**
     * Aggiorna la tabella recuperando l'elenco completo delle auto dal controller.
     */
    private void caricaDati() {
        try {
            tableModel.setRowCount(0);
            List<Auto> autoList = controller.getTutteAuto();

            for (Auto a : autoList) {
                tableModel.addRow(new Object[]{
                        a.getIdAuto(),
                        a.getTarga(),
                        a.getModello(),
                        a.getStato(),
                        a.getCostoDaily()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Errore caricamento auto: " + e.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Modifica lo stato dell'auto selezionata nella tabella.
     *
     * @param nuovoStato lo stato {@link Auto.StatoAuto} da applicare
     */
    private void cambiaStato(Auto.StatoAuto nuovoStato) {
        int riga = tabellaAuto.getSelectedRow();

        if (riga == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un'auto dalla tabella", "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idAuto = (int) tableModel.getValueAt(riga, 0);

        try {
            controller.cambiaStatoAuto(idAuto, nuovoStato);
            JOptionPane.showMessageDialog(this, "Stato aggiornato con successo!", "Operazione Completata", JOptionPane.INFORMATION_MESSAGE);
            caricaDati();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Errore aggiornamento: " + e.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}