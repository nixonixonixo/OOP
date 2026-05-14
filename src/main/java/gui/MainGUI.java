package gui;

import implementazionePostgresDAO.*;
import service.*;
import javax.swing.SwingUtilities;

public class MainGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // DAO
            var utenteDAO = new ImpUtenteDAO();
            var clienteDAO = new ImpClienteDAO();
            var operatoreDAO = new ImpOperatoreDAO();
            var autoDAO = new ImpAutoDAO();
            var prenotazioneDAO = new ImpPrenotazioneDAO();
            var noleggioDAO = new ImpNoleggioDAO();
            var pagamentoDAO = new ImpPagamentoDAO();

            // SERVICE
            UtenteService utenteService = new UtenteService(utenteDAO, clienteDAO, operatoreDAO);
            ClienteService clienteService = new ClienteService(clienteDAO);
            AutoService autoService = new AutoService(autoDAO);
            PrenotazioneService prenotazioneService = new PrenotazioneService(prenotazioneDAO, autoDAO);
            NoleggioService noleggioService = new NoleggioService(noleggioDAO, autoDAO);
            PagamentoService pagamentoService = new PagamentoService(pagamentoDAO);

            // LOGIN
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