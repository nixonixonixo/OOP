package dao;

import model.Pagamento;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface PagamentoDAO {

    void salvaPagamento(Pagamento pagamento)
            throws SQLException;

    Pagamento trovaPagamentoPerId(int idPagamento)
            throws SQLException;

    List<Pagamento> trovaPagamentiNoleggio(int idNoleggio)
            throws SQLException;

    List<Pagamento> trovaTuttiPagamenti()
            throws SQLException;

    void aggiornaPagamento(Pagamento pagamento)
            throws SQLException;

    void eliminaPagamento(int idPagamento)
            throws SQLException;

    List<Pagamento> trovaPagamentiCliente(int idCliente)
            throws SQLException;

    void ricaricaSaldoCliente(int idCliente, BigDecimal importo)
            throws SQLException;

    void aggiornaStatoPagamento(int idPagamento, Pagamento.StatoPagamento nuovoStato)
            throws SQLException;
}