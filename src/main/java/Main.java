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
                    100,
                    "mario123",
                    "hashPassword",
                    "Mario",
                    "Rossi",
                    "mario.rossi@email.com",
                    "PAT99999",
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