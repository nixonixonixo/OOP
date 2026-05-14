package service;

import dao.PagamentoDAO;
import model.Pagamento;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class PagamentoService {

    private final PagamentoDAO pagamentoDAO;

    public PagamentoService(PagamentoDAO pagamentoDAO) {
        this.pagamentoDAO = pagamentoDAO;
    }


    public void ricaricaConto(int idCliente, BigDecimal importo) throws SQLException {
        if (importo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("L'importo deve essere positivo");
        }
        pagamentoDAO.ricaricaSaldoCliente(idCliente, importo);
    }

    public void salvaPagamento(Pagamento p) throws SQLException {
        pagamentoDAO.salvaPagamento(p);
    }

    public List<Pagamento> getPagamentiByCliente(int idCliente) throws Exception {
        return pagamentoDAO.trovaPagamentiCliente(idCliente);
    }

    public List<Pagamento> getTuttiPagamenti() throws SQLException {
        return pagamentoDAO.trovaTuttiPagamenti();
    }


}