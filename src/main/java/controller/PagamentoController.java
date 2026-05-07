package controller;

import model.*;
import dao.*;
import java.math.BigDecimal;
import java.sql.SQLException;

public class PagamentoController {

    private PagamentoDAO pagamentoDAO;

    public PagamentoController(PagamentoDAO pagamentoDAO) {
        this.pagamentoDAO = pagamentoDAO;
    }

    public Pagamento effettuaPagamento(Noleggio n, Cliente c) throws SQLException {

        BigDecimal importo = n.getCostoTot();

        if (c.getCredito().compareTo(importo) < 0) {
            throw new IllegalArgumentException("Credito insufficiente");
        }

        c.scalaCredito(importo);

        Pagamento p = new Pagamento(0, importo, Pagamento.StatoPagamento.IN_ATTESA, n);

        p.completa();

        pagamentoDAO.salvaPagamento(p);
        return p;
    }
}