package service;

import dao.PagamentoDAO;
import dao.ClienteDAO;
import model.Pagamento;
import model.Cliente;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class PagamentoService {

    private final PagamentoDAO pagamentoDAO;
    private final ClienteDAO clienteDAO;

    public PagamentoService(PagamentoDAO pagamentoDAO, ClienteDAO clienteDAO) {
        this.pagamentoDAO = pagamentoDAO;
        this.clienteDAO = clienteDAO;
    }

    /**
     * Recupera i pagamenti di un cliente specifico (per la sua Area Personale)
     */
    public List<Pagamento> getPagamentiByCliente(int idCliente) throws SQLException {
        return pagamentoDAO.trovaPagamentiCliente(idCliente);
    }

    /**
     * Ricarica il credito del cliente
     */
    public void ricaricaConto(int idCliente, BigDecimal importo) throws SQLException {
        if (importo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("L'importo della ricarica deve essere positivo");
        }
        pagamentoDAO.ricaricaSaldoCliente(idCliente, importo);
    }

    /**
     * LOGICA DI PAGAMENTO: Il cliente paga un noleggio specifico usando il suo credito
     */
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

        // AGGIORNA IL DB: Sottrae il costo dal saldo nel database
        BigDecimal nuovoSaldoNegativo = costo.negate();
        pagamentoDAO.ricaricaSaldoCliente(idCliente, nuovoSaldoNegativo);

        // AGGIORNA IL DB: Cambia lo stato del pagamento
        p.setStato(Pagamento.StatoPagamento.COMPLETATO);
        pagamentoDAO.aggiornaPagamento(p);
    }

    /**
     * Recupera tutti i pagamenti (per il pannello di controllo dell'operatore)
     */
    public List<Pagamento> getTuttiPagamenti() throws SQLException {
        return pagamentoDAO.trovaTuttiPagamenti();
    }

    // Dentro PagamentoService.java
    public BigDecimal getSaldoAggiornato(int idCliente) throws SQLException {
        Cliente c = clienteDAO.trovaClientePerId(idCliente);
        return (c != null) ? c.getCredito() : BigDecimal.ZERO;
    }
}