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
 * The type Controller.
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
     * Instantiates a new Controller.
     *
     * @param utenteDAO       the utente dao
     * @param clienteDAO      the cliente dao
     * @param operatoreDAO    the operatore dao
     * @param autoDAO         the auto dao
     * @param noleggioDAO     the noleggio dao
     * @param pagamentoDAO    the pagamento dao
     * @param prenotazioneDAO the prenotazione dao
     */
    //Costruttore Controller
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
     * Login utente.
     *
     * @param username the username
     * @param password the password
     * @return the utente
     * @throws SQLException the sql exception
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
     * Logout.
     */
    //Metodo per logout
    public void logout() { this.utenteLoggato = null; }

    /**
     * Gets utente loggato.
     *
     * @return the utente loggato
     */
    //Getter per l'utente loggato
    public Utente getUtenteLoggato() { return utenteLoggato; }

    /**
     * Is operatore loggato boolean.
     *
     * @return the boolean
     */
    //Metodo per capire se l'utente loggato è un operatore
    public boolean isOperatoreLoggato() { return utenteLoggato instanceof Operatore; }

    /**
     * Gets cliente by id.
     *
     * @param id the id
     * @return the cliente by id
     * @throws SQLException the sql exception
     */
    //Getter del cliente per ID
    public Cliente getClienteById(int id) throws SQLException {
        return clienteDAO.trovaClientePerId(id);
    }

    /**
     * Registra cliente.
     *
     * @param c the c
     * @throws SQLException the sql exception
     */
    //Metodo per la registrazione
    public void registraCliente(Cliente c) throws SQLException {
        utenteDAO.salvaUtente(c);
        clienteDAO.salvaCliente(c);
    }

    /**
     * Ricarica conto.
     *
     * @param idCliente the id cliente
     * @param importo   the importo
     * @throws SQLException the sql exception
     */
    //Metodo per ricarica il conto di un cliente
    public void ricaricaConto(int idCliente, BigDecimal importo) throws SQLException {
        if (importo.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Importo non valido");
        pagamentoDAO.ricaricaSaldoCliente(idCliente, importo);
    }

    /**
     * Gets tutte auto.
     *
     * @return the tutte auto
     * @throws SQLException the sql exception
     */
    //Getter di tutte le auto
    public List<Auto> getTutteAuto() throws SQLException {
        return autoDAO.trovaTutteAuto();
    }

    /**
     * Gets auto disponibili.
     *
     * @return the auto disponibili
     * @throws SQLException the sql exception
     */
    //Getter per le auto disponibili
    public List<Auto> getAutoDisponibili() throws SQLException {
        return autoDAO.trovaAutoDisponibili();
    }

    /**
     * Cambia stato auto.
     *
     * @param idAuto the id auto
     * @param stato  the stato
     * @throws SQLException the sql exception
     */
    //Metodo per cambiare lo stato dell'auto
    public void cambiaStatoAuto(int idAuto, Auto.StatoAuto stato) throws SQLException {
        autoDAO.aggiornaStatoAuto(idAuto, stato);
    }

    /**
     * Effettua prenotazione.
     *
     * @param p the p
     * @throws Exception the exception
     */
    //Metodo che permette al cliente di effettuare una prenotazione
    public void effettuaPrenotazione(Prenotazione p) throws Exception {
        prenotazioneDAO.salvaPrenotazione(p);

        autoDAO.aggiornaStatoAuto(p.getAuto().getIdAuto(), Auto.StatoAuto.NOLEGGIATA);
    }

    /**
     * Gets tutte prenotazioni.
     *
     * @return the tutte prenotazioni
     * @throws SQLException the sql exception
     */
    //Getter di tutte le prenotazioni
    public List<Prenotazione> getTuttePrenotazioni() throws SQLException { return prenotazioneDAO.trovaTuttePrenotazioni(); }

    /**
     * Gets prenotazioni cliente.
     *
     * @param idCliente the id cliente
     * @return the prenotazioni cliente
     * @throws SQLException the sql exception
     */
    //Getter di prenotazioni di un cliente
    public List<Prenotazione> getPrenotazioniCliente(int idCliente) throws SQLException { return prenotazioneDAO.trovaPrenotazioniCliente(idCliente); }

    /**
     * Conferma prenotazione.
     *
     * @param idPrenotazione the id prenotazione
     * @throws Exception the exception
     */
    //Metodo per confermare la prenotazione
    public void confermaPrenotazione(int idPrenotazione) throws Exception {
        Prenotazione p = prenotazioneDAO.trovaPrenotazionePerId(idPrenotazione);
        if (p == null) throw new Exception("Prenotazione non trovata");
        prenotazioneDAO.aggiornaStatoPrenotazione(idPrenotazione, Prenotazione.StatoPren.CONFERMATA);
        Noleggio n = new Noleggio(0, new Date(), p);
        noleggioDAO.salvaNoleggio(n);
    }

    /**
     * Gets noleggi attivi.
     *
     * @return the noleggi attivi
     * @throws SQLException the sql exception
     */
    //Metodo per mostrare i noleggi attivi
    public List<Noleggio> getNoleggiAttivi() throws SQLException {
        return noleggioDAO.trovaTuttiNoleggi().stream()
                .filter(n -> n.getDataRestituzione() == null)
                .collect(Collectors.toList());
    }

    /**
     * Termina noleggio.
     *
     * @param idNoleggio the id noleggio
     * @throws Exception the exception
     */
    //Metodo per terminare il noleggio
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
     * Gets pagamenti by cliente.
     *
     * @param idCliente the id cliente
     * @return the pagamenti by cliente
     * @throws SQLException the sql exception
     */
    public List<Pagamento> getPagamentiByCliente(int idCliente) throws SQLException { return pagamentoDAO.trovaPagamentiCliente(idCliente); }

    /**
     * Conferma pagamento.
     *
     * @param idPagamento the id pagamento
     * @throws Exception the exception
     */
    //Metodo per permettere all'utente di pagare
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

    //Metodo di supporto per il metodo confermaPagamento
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