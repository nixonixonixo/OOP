package gui;

import dao.ClienteDAO;
import implementazionePostgresDAO.ImpClienteDAO;
import model.Cliente;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;

public class ClientePanel extends JPanel {

    private Cliente cliente;
    private ClienteDAO clienteDAO;
    private JLabel lblCredito;
    private JLabel lblPatente;

    public ClientePanel(Cliente cliente) {
        this.cliente = cliente;
        this.clienteDAO = new ImpClienteDAO();

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        JLabel lblNome = new JLabel(cliente.getNome() + " " + cliente.getCognome());
        lblNome.setFont(new Font("Arial", Font.BOLD, 24));
        JLabel lblUser = new JLabel("Username: " + cliente.getUsername());
        headerPanel.add(lblNome);
        headerPanel.add(lblUser);

        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Dettagli Account"));

        lblPatente = new JLabel("Patente: " + cliente.getPatente());
        lblCredito = new JLabel("Credito Residuo: " + cliente.getCredito() + " €");
        lblCredito.setFont(new Font("Arial", Font.BOLD, 18));
        lblCredito.setForeground(new Color(0, 128, 0)); // Verde scuro

        infoPanel.add(lblPatente);
        infoPanel.add(lblCredito);

        JPanel ricaricaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ricaricaPanel.setBorder(BorderFactory.createTitledBorder("Ricarica Credito"));

        JTextField txtRicarica = new JTextField(10);
        JButton btnRicarica = new JButton("Ricarica Ora");

        ricaricaPanel.add(new JLabel("Importo (€):"));
        ricaricaPanel.add(txtRicarica);
        ricaricaPanel.add(btnRicarica);

        btnRicarica.addActionListener(e -> {
            try {
                BigDecimal importo = new BigDecimal(txtRicarica.getText());
                if (importo.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new NumberFormatException();
                }

                BigDecimal nuovoCredito = cliente.getCredito().add(importo);

                clienteDAO.aggiornaCredito(cliente.getIdUtente(), nuovoCredito);

                cliente.setCredito(nuovoCredito);
                lblCredito.setText("Credito Residuo: " + cliente.getCredito() + " €");
                txtRicarica.setText("");

                JOptionPane.showMessageDialog(this, "Ricarica effettuata con successo!");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Inserire un importo valido (es: 50.00)", "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Errore DB: " + ex.getMessage());
            }
        });

        add(headerPanel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);
        add(ricaricaPanel, BorderLayout.SOUTH);
    }
}