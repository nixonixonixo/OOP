package implementazionePostgresDAO;

import dao.PagamentoDAO;
import database.ConnessioneDatabase;
import model.Pagamento;
import model.Noleggio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImpPagamentoDAO implements PagamentoDAO {

    @Override
    public void salvaPagamento(Pagamento p) throws SQLException {
        String sql = "INSERT INTO PAGAMENTO (idpagamento, importo, stato, idnoleggio) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, p.getIdPagamento());
            ps.setBigDecimal(2, p.getImporto());
            ps.setString(3, p.getStato().toString());
            ps.setInt(4, p.getNoleggio().getIdNoleggio());

            ps.executeUpdate();
        }
    }

    @Override
    public Pagamento trovaPagamentoPerId(int idPagamento) throws SQLException {
        String sql = "SELECT * FROM PAGAMENTO WHERE idpagamento = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPagamento);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mappaResultSetInPagamento(rs);
            }
        }
        return null;
    }

    @Override
    public List<Pagamento> trovaPagamentiNoleggio(int idNoleggio) throws SQLException {
        List<Pagamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM PAGAMENTO WHERE idnoleggio = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idNoleggio);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mappaResultSetInPagamento(rs));
            }
        }
        return lista;
    }

    @Override
    public List<Pagamento> trovaTuttiPagamenti() throws SQLException {
        List<Pagamento> lista = new ArrayList<>();
        String sql = """
        SELECT p.*, n.dataritiro, n.idprenotazione 
        FROM PAGAMENTO p
        JOIN NOLEGGIO n ON p.idnoleggio = n.idnoleggio
        """;

        try (Connection conn = ConnessioneDatabase.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mappaResultSetInPagamento(rs));
            }
        }
        return lista;
    }

    @Override
    public void aggiornaPagamento(Pagamento p) throws SQLException {
        String sql = "UPDATE PAGAMENTO SET importo = ?, stato = ? WHERE idpagamento = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBigDecimal(1, p.getImporto());
            ps.setString(2, p.getStato().toString());
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
        Date dataR = rs.getDate("dataritiro");

        Noleggio noleggio = new Noleggio(
                rs.getInt("idnoleggio"),
                dataR,
                null
        );

        String statoStr = rs.getString("stato");
        Pagamento.StatoPagamento statoEnum = Pagamento.StatoPagamento.valueOf(statoStr.toUpperCase());

        return new Pagamento(
                rs.getInt("idpagamento"),
                rs.getBigDecimal("importo"),
                statoEnum,
                noleggio
        );
    }
}