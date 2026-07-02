package controller;

import dao.*;
import model.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Controller principale dell'applicativo che funge da mediatore tra la GUI e il Data Access Layer.
 */
public class Controller {

    private final UtenteDAO utenteDAO;
    private final ClienteDAO clienteDAO;
    private final OperatoreDAO operatoreDAO;
    private final AutoDAO autoDAO;
    private final NoleggioDAO noleggioDAO;
    private final PagamentoDAO pagamentoDAO;
    private final PrenotazioneDAO prenotazioneDAO;

    private Utente utenteLoggato;

    /**
     * Inizializza il controller con le implementazioni DAO necessarie.
     *
     * @param utenteDAO       DAO per la gestione degli utenti
     * @param clienteDAO      DAO per la gestione dei clienti
     * @param operatoreDAO    DAO per la gestione degli operatori
     * @param autoDAO         DAO per la gestione delle auto
     * @param noleggioDAO     DAO per la gestione dei noleggi
     * @param pagamentoDAO    DAO per la gestione dei pagamenti
     * @param prenotazioneDAO DAO per la gestione delle prenotazioni
     */
    public Controller(UtenteDAO utenteDAO, ClienteDAO clienteDAO, OperatoreDAO operatoreDAO,
                      AutoDAO autoDAO, NoleggioDAO noleggioDAO, PagamentoDAO pagamentoDAO,
                      PrenotazioneDAO prenotazioneDAO) {
        this.utenteDAO = utenteDAO;
        this.clienteDAO = clienteDAO;
        this.operatoreDAO = operatoreDAO;
        this.autoDAO = autoDAO;
        this.noleggioDAO = noleggioDAO;
        this.pagamentoDAO = pagamentoDAO;
        this.prenotazioneDAO = prenotazioneDAO;
    }

    /**
     * Esegue l'autenticazione dell'utente nel sistema.
     *
     * @param username lo username inserito
     * @param password la password inserita
     * @return l'oggetto Utente autenticato
     * @throws SQLException             se si verifica un errore nel database
     * @throws IllegalArgumentException se le credenziali non sono valide
     */
    public Utente login(String username, String password) throws SQLException {
        Utente u = utenteDAO.trovaUtentePerUsername(username);
        if (u == null || !u.verificaPassword(password)) {
            throw new IllegalArgumentException("Credenziali non valide");
        }
        this.utenteLoggato = u;
        return u;
    }

    /**
     * Termina la sessione dell'utente corrente.
     */
    public void logout() { this.utenteLoggato = null; }

    /**
     * Restituisce l'utente attualmente loggato.
     *
     * @return l'utente loggato, o null se nessuno è autenticato
     */
    public Utente getUtenteLoggato() { return utenteLoggato; }

    /**
     * Verifica se l'utente autenticato è un operatore.
     *
     * @return true se l'utente è un operatore, false altrimenti
     */
    public boolean isOperatoreLoggato() { return utenteLoggato instanceof Operatore; }

    /**
     * Recupera un cliente specifico tramite il suo ID univoco.
     *
     * @param id l'ID del cliente
     * @return l'oggetto Cliente trovato
     * @throws SQLException se si verifica un errore nel database
     */
    public Cliente getClienteById(int id) throws SQLException {
        return clienteDAO.trovaClientePerId(id);
    }

    /**
     * Registra un nuovo cliente nel sistema.
     *
     * @param c il cliente da registrare
     * @throws SQLException se si verifica un errore nel database
     */
    public void registraCliente(Cliente c) throws SQLException {
        utenteDAO.salvaUtente(c);
        clienteDAO.salvaCliente(c);
    }

    /**
     * Effettua una ricarica sul saldo disponibile di un cliente.
     *
     * @param idCliente l'ID del cliente
     * @param importo   l'importo da accreditare
     * @throws SQLException se si verifica un errore nel database
     */
    public void ricaricaConto(int idCliente, BigDecimal importo) throws SQLException {
        if (importo.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Importo non valido");
        pagamentoDAO.ricaricaSaldoCliente(idCliente, importo);
    }

    /**
     * Restituisce l'elenco completo delle auto nel sistema.
     *
     * @return una lista di auto
     * @throws SQLException se si verifica un errore nel database
     */
    public List<Auto> getTutteAuto() throws SQLException {
        return autoDAO.trovaTutteAuto();
    }

    /**
     * Restituisce solo le auto attualmente disponibili per il noleggio.
     *
     * @return una lista di auto disponibili
     * @throws SQLException se si verifica un errore nel database
     */
    public List<Auto> getAutoDisponibili() throws SQLException {
        return autoDAO.trovaAutoDisponibili();
    }

    /**
     * Aggiorna lo stato di un'auto nel database.
     *
     * @param idAuto l'ID dell'auto
     * @param stato  il nuovo stato da impostare
     * @throws SQLException se si verifica un errore nel database
     */
    public void cambiaStatoAuto(int idAuto, Auto.StatoAuto stato) throws SQLException {
        autoDAO.aggiornaStatoAuto(idAuto, stato);
    }

    /**
     * Finalizza una prenotazione e imposta l'auto come noleggiata.
     *
     * @param p la prenotazione da effettuare
     * @throws Exception in caso di errore di persistenza
     */
    public void effettuaPrenotazione(Prenotazione p) throws Exception {
        prenotazioneDAO.salvaPrenotazione(p);
        autoDAO.aggiornaStatoAuto(p.getAuto().getIdAuto(), Auto.StatoAuto.NOLEGGIATA);
    }

    /**
     * Restituisce tutte le prenotazioni presenti.
     *
     * @return lista di prenotazioni
     * @throws SQLException se si verifica un errore nel database
     */
    public List<Prenotazione> getTuttePrenotazioni() throws SQLException { return prenotazioneDAO.trovaTuttePrenotazioni(); }

    /**
     * Restituisce le prenotazioni effettuate da un cliente specifico.
     *
     * @param idCliente l'ID del cliente
     * @return lista di prenotazioni del cliente
     * @throws SQLException se si verifica un errore nel database
     */
    public List<Prenotazione> getPrenotazioniCliente(int idCliente) throws SQLException { return prenotazioneDAO.trovaPrenotazioniCliente(idCliente); }

    /**
     * Conferma una prenotazione esistente e crea un nuovo noleggio associato.
     *
     * @param idPrenotazione l'ID della prenotazione da confermare
     * @throws Exception se la prenotazione non esiste
     */
    public void confermaPrenotazione(int idPrenotazione) throws Exception {
        Prenotazione p = prenotazioneDAO.trovaPrenotazionePerId(idPrenotazione);
        if (p == null) throw new Exception("Prenotazione non trovata");
        prenotazioneDAO.aggiornaStatoPrenotazione(idPrenotazione, Prenotazione.StatoPren.CONFERMATA);
        Noleggio n = new Noleggio(0, new Date(), p);
        noleggioDAO.salvaNoleggio(n);
    }

    /**
     * Restituisce la lista dei noleggi in corso (non ancora restituiti).
     *
     * @return lista di noleggi attivi
     * @throws SQLException se si verifica un errore nel database
     */
    public List<Noleggio> getNoleggiAttivi() throws SQLException {
        return noleggioDAO.trovaTuttiNoleggi().stream()
                .filter(n -> n.getDataRestituzione() == null)
                .collect(Collectors.toList());
    }

    /**
     * Calcola il costo totale, chiude il noleggio e crea la richiesta di pagamento.
     *
     * @param idNoleggio l'ID del noleggio da terminare
     * @throws Exception in caso di errori durante il calcolo o la persistenza
     */
    public void terminaNoleggio(int idNoleggio) throws Exception {
        Noleggio n = noleggioDAO.trovaNoleggioPerId(idNoleggio);
        if (n == null) throw new Exception("Noleggio non trovato.");

        long diffInMillis = new Date().getTime() - n.getDataRitiro().getTime();
        long giorni = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        if (giorni == 0) giorni = 1;

        BigDecimal totale = n.getPrenotazione().getAuto().getCostoDaily().multiply(BigDecimal.valueOf(giorni));

        n.chiudiNoleggio(new Date(), totale);
        noleggioDAO.aggiornaNoleggio(n);
        pagamentoDAO.salvaPagamento(new Pagamento(0, totale, Pagamento.StatoPagamento.IN_ATTESA, n));
        autoDAO.aggiornaStatoAuto(n.getPrenotazione().getAuto().getIdAuto(), Auto.StatoAuto.DISPONIBILE);
    }

    /**
     * Restituisce i pagamenti effettuati o in attesa di un cliente.
     *
     * @param idCliente l'ID del cliente
     * @return lista di pagamenti
     * @throws SQLException se si verifica un errore nel database
     */
    public List<Pagamento> getPagamentiByCliente(int idCliente) throws SQLException { return pagamentoDAO.trovaPagamentiCliente(idCliente); }

    /**
     * Effettua il pagamento di una fattura utilizzando il credito disponibile del cliente.
     *
     * @param idPagamento l'ID del pagamento da saldare
     * @throws Exception se il credito è insufficiente o il pagamento non esiste
     */
    public void confermaPagamento(int idPagamento) throws Exception {
        Pagamento p = pagamentoDAO.trovaPagamentoPerId(idPagamento);
        if (p == null) throw new Exception("Pagamento non trovato.");
        if (p.getStato() == Pagamento.StatoPagamento.COMPLETATO) throw new Exception("Pagamento già effettuato.");

        int idCliente = recuperaIdClienteDaPagamento(idPagamento);

        Cliente c = clienteDAO.trovaClientePerId(idCliente);
        if (c == null) throw new Exception("Cliente non trovato.");

        if (c.getCredito().compareTo(p.getImporto()) < 0) {
            throw new Exception("Credito insufficiente. Ricarica il conto!");
        }

        clienteDAO.prelevaSaldo(idCliente, p.getImporto());
        pagamentoDAO.aggiornaStatoPagamento(idPagamento, Pagamento.StatoPagamento.COMPLETATO);
    }

    /**
     * Metodo privato di supporto per risalire al cliente partendo da un ID pagamento.
     *
     * @param idPagamento l'ID del pagamento
     * @return l'ID del cliente associato
     * @throws SQLException se non viene trovato il cliente o c'è un errore SQL
     */
    private int recuperaIdClienteDaPagamento(int idPagamento) throws SQLException {
        String sql = "SELECT pr.idcliente FROM PRENOTAZIONE pr " +
                "JOIN NOLEGGIO n ON n.idprenotazione = pr.idprenotazione " +
                "JOIN PAGAMENTO p ON p.idnoleggio = n.idnoleggio " +
                "WHERE p.idpagamento = ?";

        try (java.sql.Connection conn = database.ConnessioneDatabase.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPagamento);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idcliente");
                } else {
                    throw new SQLException("Impossibile trovare il cliente associato al pagamento.");
                }
            }
        }
    }
}