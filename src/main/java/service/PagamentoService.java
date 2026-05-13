package service;

import dao.PagamentoDAO;
import model.Pagamento;

import java.sql.SQLException;
import java.util.List;

public class PagamentoService {

    private final PagamentoDAO pagamentoDAO;

    public PagamentoService(PagamentoDAO pagamentoDAO) {
        this.pagamentoDAO = pagamentoDAO;
    }

    public void salvaPagamento(Pagamento p) throws SQLException {
        pagamentoDAO.salvaPagamento(p);
    }

    public Pagamento getPagamentoById(int id) throws SQLException {
        return pagamentoDAO.trovaPagamentoPerId(id);
    }

    public List<Pagamento> getPagamentiByNoleggio(int idNoleggio) throws SQLException {
        return pagamentoDAO.trovaPagamentiNoleggio(idNoleggio);
    }

    public List<Pagamento> getTuttiPagamenti() throws SQLException {
        return pagamentoDAO.trovaTuttiPagamenti();
    }

    public void aggiornaPagamento(Pagamento p) throws SQLException {
        pagamentoDAO.aggiornaPagamento(p);
    }

    public void eliminaPagamento(int id) throws SQLException {
        pagamentoDAO.eliminaPagamento(id);
    }
}