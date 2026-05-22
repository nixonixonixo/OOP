package controller;

import dao.*;
import model.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class Controller {

    //Riferimenti a tutti i DAO
    private final UtenteDAO utenteDAO;
    private final ClienteDAO clienteDAO;
    private final OperatoreDAO operatoreDAO;
    private final AutoDAO autoDAO;
    private final NoleggioDAO noleggioDAO;
    private final PagamentoDAO pagamentoDAO;
    private final PrenotazioneDAO prenotazioneDAO;

    // Gestione della sessione utente attiva
    private Utente utenteLoggato;

    // Costruttore
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

    // Sessione e autenticazione
    public Utente login(String username, String password) throws SQLException {
        Utente u = utenteDAO.trovaUtentePerUsername(username);

        if (u == null || !u.verificaPassword(password)) {
            throw new IllegalArgumentException("Credenziali non valide");
        }

        this.utenteLoggato = u;
        return u;
    }

    public void logout() {
        this.utenteLoggato = null;
    }

    public void registraCliente(Cliente cliente) throws SQLException {
        utenteDAO.salvaUtente(cliente);
        clienteDAO.salvaCliente(cliente);
    }

    public Utente getUtenteLoggato() {
        return utenteLoggato;
    }

    public boolean isOperatoreLoggato() {
        return utenteLoggato instanceof Operatore;
    }

    // Gestione delle auto

    public List<Auto> getTutteAuto() throws SQLException {
        return autoDAO.trovaTutteAuto();
    }

    public List<Auto> getAutoDisponibili() throws SQLException {
        return autoDAO.trovaAutoDisponibili();
    }

    public void cambiaStatoAuto(int idAuto, Auto.StatoAuto stato) throws SQLException {
        autoDAO.aggiornaStatoAuto(idAuto, stato);
    }

    // Gestione Clienti

    public Cliente getClienteById(int id) throws SQLException {
        return clienteDAO.trovaClientePerId(id);
    }

    public void ricaricaCreditoCliente(int id, BigDecimal importo) throws SQLException {
        clienteDAO.aggiornaCredito(id, importo);
    }

    // Gestione Operatori

    public Operatore getOperatore(int id) throws SQLException {
        return operatoreDAO.trovaOperatorePerId(id);
    }

    public List<Operatore> getTuttiOperatori() throws SQLException {
        return operatoreDAO.trovaTuttiOperatori();
    }

    public void salvaOperatore(Operatore o) throws SQLException {
        operatoreDAO.salvaOperatore(o);
    }

    public void aggiornaOperatore(Operatore o) throws SQLException {
        operatoreDAO.aggiornaOperatore(o);
    }

    public void eliminaOperatore(int id) throws SQLException {
        operatoreDAO.eliminaOperatore(id);
    }

    // Gestione Prenotazioni

    public void effettuaPrenotazione(Prenotazione p) throws SQLException {
        prenotazioneDAO.salvaPrenotazione(p);
        autoDAO.aggiornaStatoAuto(p.getAuto().getIdAuto(), Auto.StatoAuto.NOLEGGIATA);
    }

    public List<Prenotazione> getTuttePrenotazioni() throws SQLException {
        return prenotazioneDAO.trovaTuttePrenotazioni();
    }

    public List<Prenotazione> getPrenotazioniCliente(int idCliente) throws SQLException {
        return prenotazioneDAO.trovaPrenotazioniCliente(idCliente);
    }

    public void confermaPrenotazione(int idPrenotazione) throws Exception {
        Prenotazione p = prenotazioneDAO.trovaPrenotazionePerId(idPrenotazione);
        if (p == null) throw new Exception("Prenotazione non trovata");

        prenotazioneDAO.aggiornaStatoPrenotazione(idPrenotazione, Prenotazione.StatoPren.CONFERMATA);

        Noleggio n = new Noleggio(0, new Date(), p);
        noleggioDAO.salvaNoleggio(n);
    }

    public void annullaPrenotazione(int idPrenotazione) throws Exception {
        Prenotazione p = prenotazioneDAO.trovaPrenotazionePerId(idPrenotazione);
        if (p == null) throw new Exception("Prenotazione non trovata");

        prenotazioneDAO.aggiornaStatoPrenotazione(idPrenotazione, Prenotazione.StatoPren.ANNULLATA);
        autoDAO.aggiornaStatoAuto(p.getAuto().getIdAuto(), Auto.StatoAuto.DISPONIBILE);
    }

    // Gestione Noleggi

    public List<Noleggio> getTuttiNoleggi() throws SQLException {
        return noleggioDAO.trovaTuttiNoleggi();
    }

    public void chiudiNoleggio(int idNoleggio) throws Exception {
        Noleggio n = noleggioDAO.trovaNoleggioPerId(idNoleggio);

        if (n == null) {
            throw new Exception("Errore: Noleggio non trovato.");
        }

        if (n.getDataRestituzione() != null) {
            throw new Exception("Errore: Questo noleggio è già stato chiuso.");
        }

        Date dataOggi = new Date();
        double costoDaily = n.getPrenotazione().getAuto().getCostoDaily().doubleValue();

        n.chiudiNoleggio(dataOggi, java.math.BigDecimal.valueOf(costoDaily));
        noleggioDAO.aggiornaNoleggio(n);

        Pagamento nuovoPagamento = new Pagamento(
                0,
                n.getCostoTot(),
                Pagamento.StatoPagamento.IN_ATTESA,
                n
        );
        pagamentoDAO.salvaPagamento(nuovoPagamento);

        int idAuto = n.getPrenotazione().getAuto().getIdAuto();
        autoDAO.aggiornaStatoAuto(idAuto, Auto.StatoAuto.DISPONIBILE);
    }

    // Gestione Pagamenti

    public List<Pagamento> getPagamentiByCliente(int idCliente) throws SQLException {
        return pagamentoDAO.trovaPagamentiCliente(idCliente);
    }

    public void ricaricaConto(int idCliente, BigDecimal importo) throws SQLException {
        if (importo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("L'importo della ricarica deve essere positivo");
        }
        pagamentoDAO.ricaricaSaldoCliente(idCliente, importo);
    }

    public void effettuaPagamento(int idPagamento, int idCliente) throws Exception {
        Pagamento p = pagamentoDAO.trovaPagamentoPerId(idPagamento);
        if (p == null) throw new Exception("Pagamento non trovato.");

        if (p.getStato() != Pagamento.StatoPagamento.IN_ATTESA) {
            throw new Exception("Pagamento già effettuato.");
        }

        Cliente c = clienteDAO.trovaClientePerId(idCliente);
        BigDecimal costo = p.getImporto();

        if (c.getCredito().compareTo(costo) < 0) {
            throw new Exception("Credito insufficiente!");
        }

        BigDecimal nuovoSaldoNegativo = costo.negate();
        pagamentoDAO.ricaricaSaldoCliente(idCliente, nuovoSaldoNegativo);

        p.setStato(Pagamento.StatoPagamento.COMPLETATO);
        pagamentoDAO.aggiornaPagamento(p);
    }

    public List<Pagamento> getTuttiPagamenti() throws SQLException {
        return pagamentoDAO.trovaTuttiPagamenti();
    }

    public BigDecimal getSaldoAggiornato(int idCliente) throws SQLException {
        Cliente c = clienteDAO.trovaClientePerId(idCliente);
        return (c != null) ? c.getCredito() : BigDecimal.ZERO;
    }
}