package service;

import dao.PagamentoDAO;
import model.*;

import java.math.BigDecimal;
import java.sql.SQLException;

public class PagamentoService {

    private final PagamentoDAO pagamentoDAO;

    public PagamentoService(PagamentoDAO pagamentoDAO) {
        this.pagamentoDAO = pagamentoDAO;
    }

    public Pagamento effettuaPagamento(Noleggio n, Cliente c) throws SQLException {

        BigDecimal importo = n.getCostoTot();

        if (importo == null || importo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Importo non valido");
        }

        if (c.getCredito().compareTo(importo) < 0) {
            throw new IllegalArgumentException("Credito insufficiente");
        }

        c.scalaCredito(importo);

        Pagamento p = new Pagamento(
                0,
                importo,
                Pagamento.StatoPagamento.COMPLETATO,
                n
        );

        pagamentoDAO.salvaPagamento(p);

        return p;
    }
}