package gui;

import implementazionePostgresDAO.*;
import service.*;
import javax.swing.SwingUtilities;

public class MainGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. Inizializzazione DAO (Tutti i componenti per l'accesso ai dati)
            var utenteDAO = new ImpUtenteDAO();
            var clienteDAO = new ImpClienteDAO();
            var operatoreDAO = new ImpOperatoreDAO();
            var autoDAO = new ImpAutoDAO();
            var prenotazioneDAO = new ImpPrenotazioneDAO();
            var noleggioDAO = new ImpNoleggioDAO();
            var pagamentoDAO = new ImpPagamentoDAO();

            // 2. Inizializzazione SERVICE (Iniezione delle dipendenze corretta)

            UtenteService utenteService = new UtenteService(utenteDAO, clienteDAO, operatoreDAO);

            ClienteService clienteService = new ClienteService(clienteDAO);

            AutoService autoService = new AutoService(autoDAO);

            // FIX: PrenotazioneService ora ha bisogno anche di noleggioDAO per poterlo creare alla conferma
            // E di autoDAO per liberare l'auto in caso di annullamento
            PrenotazioneService prenotazioneService = new PrenotazioneService(prenotazioneDAO, noleggioDAO, autoDAO);

            // FIX: NoleggioService ha bisogno di pagamentoDAO per generare il debito alla chiusura
            NoleggioService noleggioService = new NoleggioService(noleggioDAO, autoDAO, pagamentoDAO);

            // FIX: PagamentoService ha bisogno di clienteDAO per scalare il credito durante il pagamento
            PagamentoService pagamentoService = new PagamentoService(pagamentoDAO, clienteDAO);

            // 3. Avvio del LOGIN
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