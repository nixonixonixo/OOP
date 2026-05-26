package implementazionePostgresDAO;

import dao.PagamentoDAO;
import database.ConnessioneDatabase;
import model.Pagamento;
import model.Noleggio;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Imp pagamento dao.
 */
public class ImpPagamentoDAO implements PagamentoDAO {

    @Override
    public void ricaricaSaldoCliente(int idCliente, java.math.BigDecimal importo) throws SQLException {
        String sql = "UPDATE CLIENTE SET credito = credito + ? WHERE idutente = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBigDecimal(1, importo);
            ps.setInt(2, idCliente);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Nessun cliente trovato con ID: " + idCliente);
            }
        }
    }

    @Override
    public void salvaPagamento(Pagamento p) throws SQLException {
        String sql = "INSERT INTO PAGAMENTO (importo, stato, idnoleggio) VALUES (?, ?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBigDecimal(1, p.getImporto());

            ps.setString(2, p.getStato().toString());

            ps.setInt(3, p.getNoleggio().getIdNoleggio());

            ps.executeUpdate();
        }
    }

    @Override
    public List<Pagamento> trovaPagamentiCliente(int idCliente) throws SQLException {
        List<Pagamento> lista = new ArrayList<>();

        String sql = """
            SELECT p.*, n.idnoleggio, n.dataritiro
            FROM PAGAMENTO p
            JOIN NOLEGGIO n ON p.idnoleggio = n.idnoleggio
            JOIN PRENOTAZIONE pr ON n.idprenotazione = pr.idprenotazione
            WHERE pr.idcliente = ?
        """;
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mappaResultSetInPagamento(rs));
            }
        }
        return lista;
    }

    @Override
    public Pagamento trovaPagamentoPerId(int idPagamento) throws SQLException {
        String sql = "SELECT p.*, n.idnoleggio, n.dataritiro FROM PAGAMENTO p JOIN NOLEGGIO n ON p.idnoleggio = n.idnoleggio WHERE p.idpagamento = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPagamento);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mappaResultSetInPagamento(rs);
        }
        return null;
    }

    @Override
    public List<Pagamento> trovaPagamentiNoleggio(int idNoleggio) throws SQLException {
        List<Pagamento> lista = new ArrayList<>();
        String sql = "SELECT p.*, n.idnoleggio, n.dataritiro FROM PAGAMENTO p JOIN NOLEGGIO n ON p.idnoleggio = n.idnoleggio WHERE p.idnoleggio = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idNoleggio);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mappaResultSetInPagamento(rs));
        }
        return lista;
    }

    @Override
    public List<Pagamento> trovaTuttiPagamenti() throws SQLException {
        List<Pagamento> lista = new ArrayList<>();
        String sql = "SELECT p.*, n.idnoleggio, n.dataritiro FROM PAGAMENTO p JOIN NOLEGGIO n ON p.idnoleggio = n.idnoleggio";
        try (Connection conn = ConnessioneDatabase.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mappaResultSetInPagamento(rs));
        }
        return lista;
    }

    @Override
    public void aggiornaPagamento(Pagamento p) throws SQLException {
        String sql = "UPDATE PAGAMENTO SET importo = ?, stato = ? WHERE idpagamento = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, p.getImporto());
            ps.setString(2, p.getStato().name());
            ps.setInt(3, p.getIdPagamento());
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminaPagamento(int idPagamento) throws SQLException {
        String sql = "DELETE FROM PAGAMENTO WHERE idpagamento = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPagamento);
            ps.executeUpdate();
        }
    }

    private Pagamento mappaResultSetInPagamento(ResultSet rs) throws SQLException {
        Noleggio noleggio = new Noleggio(rs.getInt("idnoleggio"), rs.getDate("dataritiro"), null);
        String statoStr = rs.getString("stato").toUpperCase();
        Pagamento.StatoPagamento statoEnum = Pagamento.StatoPagamento.valueOf(statoStr);
        return new Pagamento(rs.getInt("idpagamento"), rs.getBigDecimal("importo"), statoEnum, noleggio);
    }

    @Override
    public void aggiornaStatoPagamento(int idPagamento, Pagamento.StatoPagamento nuovoStato) throws SQLException {
        String sql = "UPDATE PAGAMENTO SET stato = ? WHERE idpagamento = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuovoStato.name());
            ps.setInt(2, idPagamento);
            ps.executeUpdate();
        }
    }
}