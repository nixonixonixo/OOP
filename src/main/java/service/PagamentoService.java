package service;

import dao.PagamentoDAO;
import dao.NoleggioDAO;
import dao.ClienteDAO;
import model.*;

import java.math.BigDecimal;
import java.sql.SQLException;

public class PagamentoService {

    private final PagamentoDAO pagamentoDAO;
    private final ClienteDAO clienteDAO;
    private final NoleggioDAO noleggioDAO;

    public PagamentoService(
            PagamentoDAO pagamentoDAO,
            ClienteDAO clienteDAO,
            NoleggioDAO noleggioDAO
    ) {
        this.pagamentoDAO = pagamentoDAO;
        this.clienteDAO = clienteDAO;
        this.noleggioDAO = noleggioDAO;
    }

    /**
     * Effettua pagamento di un noleggio
     */
    public Pagamento effettuaPagamento(int idNoleggio) throws SQLException {

        Noleggio n = noleggioDAO.trovaNoleggioPerId(idNoleggio);

        if (n == null) {
            throw new IllegalArgumentException("Noleggio non trovato");
        }

        if (n.getCostoTot() == null) {
            throw new IllegalStateException("Costo non calcolato");
        }

        Cliente cliente = n.getPrenotazione().getCliente();

        BigDecimal importo = n.getCostoTot();

        // controllo credito
        if (cliente.getCredito().compareTo(importo) < 0) {
            throw new IllegalStateException("Credito insufficiente");
        }

        // scala credito
        BigDecimal nuovoCredito =
                cliente.getCredito().subtract(importo);

        cliente.setCredito(nuovoCredito);
        clienteDAO.aggiornaCredito(cliente.getIdUtente(), nuovoCredito);

        // crea pagamento
        Pagamento pagamento = new Pagamento(
                0,
                importo,
                Pagamento.StatoPagamento.COMPLETATO,
                n
        );

        pagamentoDAO.salvaPagamento(pagamento);

        return pagamento;
    }
}