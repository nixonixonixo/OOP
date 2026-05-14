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
        // 1. Recuperiamo il pagamento dal DB
        Pagamento p = pagamentoDAO.trovaPagamentoPerId(idPagamento);
        if (p == null) throw new Exception("Pagamento non trovato.");

        if (p.getStato() != Pagamento.StatoPagamento.IN_ATTESA) {
            throw new Exception("Questo pagamento è già stato elaborato (Stato: " + p.getStato() + ")");
        }

        // 2. Recuperiamo il cliente per controllare il saldo attuale
        // Nota: Assumiamo che clienteDAO.trovaPerId restituisca un oggetto Cliente con il credito aggiornato
        Cliente c = clienteDAO.trovaClientePerId(idCliente);
        BigDecimal costo = p.getImporto();

        if (c.getCredito().compareTo(costo) < 0) {
            throw new Exception("Credito insufficiente. Carica il tuo conto per procedere.");
        }

        // 3. Sottraiamo il costo dal credito del cliente
        // Usiamo il metodo ricaricaSaldoCliente con segno negativo
        BigDecimal importoDaSottrarre = costo.negate();
        pagamentoDAO.ricaricaSaldoCliente(idCliente, importoDaSottrarre);

        // 4. Aggiorniamo lo stato del pagamento in COMPLETATO
        p.setStato(Pagamento.StatoPagamento.COMPLETATO);
        pagamentoDAO.aggiornaPagamento(p);
    }

    /**
     * Recupera tutti i pagamenti (per il pannello di controllo dell'operatore)
     */
    public List<Pagamento> getTuttiPagamenti() throws SQLException {
        return pagamentoDAO.trovaTuttiPagamenti();
    }
}