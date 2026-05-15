package gui;

import implementazionePostgresDAO.*;
import service.*;
import javax.swing.SwingUtilities;

public class MainGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // Inizializzazione DAO

            var utenteDAO = new ImpUtenteDAO();
            var clienteDAO = new ImpClienteDAO();
            var operatoreDAO = new ImpOperatoreDAO();
            var autoDAO = new ImpAutoDAO();
            var prenotazioneDAO = new ImpPrenotazioneDAO();
            var noleggioDAO = new ImpNoleggioDAO();
            var pagamentoDAO = new ImpPagamentoDAO();

            // Inizializzazione Service

            UtenteService utenteService = new UtenteService(utenteDAO, clienteDAO, operatoreDAO);

            ClienteService clienteService = new ClienteService(clienteDAO);

            AutoService autoService = new AutoService(autoDAO);

            PrenotazioneService prenotazioneService = new PrenotazioneService(prenotazioneDAO, noleggioDAO, autoDAO);

            NoleggioService noleggioService = new NoleggioService(noleggioDAO, autoDAO, pagamentoDAO);

            PagamentoService pagamentoService = new PagamentoService(pagamentoDAO, clienteDAO);

            // Avvio del LOGIN

            new LoginFrame(
                    utenteService,
                    clienteService,
                    autoService,
                    prenotazioneService,
                    noleggioService,
                    pagamentoService
            );
        });
    }
}