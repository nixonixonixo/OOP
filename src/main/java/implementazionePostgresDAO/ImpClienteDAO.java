package implementazionePostgresDAO;

import dao.ClienteDAO;
import database.ConnessioneDatabase;
import model.Cliente;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ImpClienteDAO implements ClienteDAO {

    @Override
    public void salvaCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO CLIENTE (idutente, patente, credito) VALUES (?, ?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cliente.getIdUtente());
            ps.setString(2, cliente.getPatente());
            ps.setBigDecimal(3, cliente.getCredito());

            ps.executeUpdate();
        }
    }

    @Override
    public Cliente trovaClientePerId(int idUtente) throws SQLException {
        String sql = """
                SELECT u.*, c.patente, c.credito
                FROM UTENTE u
                LEFT JOIN CLIENTE c ON u.idutente = c.idutente
                WHERE u.idutente = ?
                """;

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtente);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mappaResultSetInCliente(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Cliente> trovaTuttiClienti() throws SQLException {
        List<Cliente> clienti = new ArrayList<>();

        String sql = """
                SELECT u.*, c.patente, c.credito
                FROM UTENTE u
                LEFT JOIN CLIENTE c ON u.idutente = c.idutente
                """;

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente c = mappaResultSetInCliente(rs);
                if (c.getPatente() != null) {
                    clienti.add(c);
                }
            }
        }

        return clienti;
    }

    @Override
    public void aggiornaCliente(Cliente cliente) throws SQLException {
        String sql = """
                UPDATE CLIENTE
                SET patente = ?, credito = ?
                WHERE idutente = ?
                """;

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cliente.getPatente());
            ps.setBigDecimal(2, cliente.getCredito());
            ps.setInt(3, cliente.getIdUtente());

            ps.executeUpdate();
        }
    }

    @Override
    public void eliminaCliente(int idUtente) throws SQLException {
        String sql = "DELETE FROM CLIENTE WHERE idutente = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtente);
            ps.executeUpdate();
        }
    }

    @Override
    public void aggiornaCredito(int idUtente, BigDecimal nuovoCredito) throws SQLException {
        String sql = "UPDATE CLIENTE SET credito = ? WHERE idutente = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBigDecimal(1, nuovoCredito);
            ps.setInt(2, idUtente);

            if (ps.executeUpdate() == 0) {
                throw new SQLException("Cliente non trovato");
            }
        }
    }

    private Cliente mappaResultSetInCliente(ResultSet rs) throws SQLException {

        BigDecimal credito = rs.getBigDecimal("credito");
        if (credito == null) {
            credito = BigDecimal.ZERO;
        }

        return new Cliente(
                rs.getInt("idutente"),
                rs.getString("username"),
                rs.getString("passwordhash"),
                rs.getString("nome"),
                rs.getString("cognome"),
                rs.getString("email"),
                rs.getString("patente"),
                credito
        );
    }

    @Override
    public void prelevaSaldo(int idCliente, java.math.BigDecimal importo) throws SQLException {
        String sql = "UPDATE CLIENTE SET credito = credito - ? WHERE idutente = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, importo);
            ps.setInt(2, idCliente);
            ps.executeUpdate();
        }
    }
}