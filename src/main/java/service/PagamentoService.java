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