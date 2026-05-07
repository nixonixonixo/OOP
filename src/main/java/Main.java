import implementazionePostgresDAO.ImpUtenteDAO;
import implementazionePostgresDAO.ImpClienteDAO;

import model.Cliente;
import model.Utente;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {

        try {

            // =========================
            // CREAZIONE CLIENTE
            // =========================

            Cliente cliente = new Cliente(
                    1000,
                    "loffrypasq",
                    "lollo",
                    "Passquale",
                    "Russo",
                    "pasq_l_23@gmail.com",
                    "PAT104",
                    new BigDecimal("500")
            );

            // =========================
            // DAO
            // =========================

            ImpUtenteDAO utenteDAO = new ImpUtenteDAO();

            ImpClienteDAO clienteDAO = new ImpClienteDAO();

            // =========================
            // SALVATAGGIO
            // =========================

            utenteDAO.salvaUtente(cliente);

            clienteDAO.salvaCliente(cliente);

            System.out.println("Cliente salvato nel database!");

            // =========================
            // LETTURA
            // =========================

            Cliente clienteLetto =
                    clienteDAO.trovaClientePerId(100);

            if (clienteLetto != null) {

                System.out.println("Cliente trovato:");

                System.out.println(clienteLetto);

            } else {

                System.out.println("Cliente non trovato");
            }

        } catch (Exception e) {

            System.out.println("Errore:");

            e.printStackTrace();
        }
    }
}