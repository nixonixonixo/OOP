package gui;

import implementazionePostgresDAO.*;
import service.*;

import javax.swing.SwingUtilities;

public class MainGUI {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            // DAO
            var utenteDAO = new ImpUtenteDAO();
            var autoDAO = new ImpAutoDAO();
            var prenotazioneDAO = new ImpPrenotazioneDAO();
            var noleggioDAO = new ImpNoleggioDAO();
            var pagamentoDAO = new ImpPagamentoDAO();
            var clienteDAO = new ImpClienteDAO();

            // SERVICE
            UtenteService utenteService = new UtenteService(utenteDAO);
            AutoService autoService = new AutoService(autoDAO);
            ClienteService clienteService = new ClienteService(clienteDAO);

            PrenotazioneService prenotazioneService =
                    new PrenotazioneService(prenotazioneDAO, autoDAO);

            NoleggioService noleggioService =
                    new NoleggioService(noleggioDAO, autoDAO);

            PagamentoService pagamentoService =
                    new PagamentoService(pagamentoDAO);

            // GUI START
            new LoginFrame(utenteService);
        });
    }
}