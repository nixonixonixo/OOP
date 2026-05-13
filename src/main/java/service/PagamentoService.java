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

    public void salva(Pagamento p) throws SQLException {
        pagamentoDAO.salvaPagamento(p);
    }

    public List<Pagamento> getTuttiPagamenti() throws SQLException {
        return pagamentoDAO.trovaTuttiPagamenti();
    }

    public void aggiorna(Pagamento p) throws SQLException {
        pagamentoDAO.aggiornaPagamento(p);
    }
}