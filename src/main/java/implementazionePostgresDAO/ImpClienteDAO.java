package implementazionePostgresDAO;

import dao.ClienteDAO;
import database.ConnessioneDatabase;
import model.Cliente;

import java.sql.*;
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
                JOIN CLIENTE c ON u.idutente = c.idutente
                WHERE u.idutente = ?
                """;

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtente);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mappaResultSetInCliente(rs);
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
                JOIN CLIENTE c ON u.idutente = c.idutente
                """;

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                clienti.add(mappaResultSetInCliente(rs));
            }
        }
        return clienti;
    }

    @Override
    public void aggiornaCliente(Cliente cliente) throws SQLException {
        String sql = """
                UPDATE CLIENTE
                SET patente = ?,
                    credito = ?
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
    public void aggiornaCredito(int idUtente, java.math.BigDecimal nuovoCredito) throws SQLException {
        String sql = "UPDATE CLIENTE SET credito = ? WHERE idutente = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBigDecimal(1, nuovoCredito);
            ps.setInt(2, idUtente);

            if (ps.executeUpdate() == 0) {
                throw new SQLException("Aggiornamento fallito: Cliente non trovato.");
            }
        }
    }

    private Cliente mappaResultSetInCliente(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getInt("idutente"),
                rs.getString("username"),
                rs.getString("passwordhash"),
                rs.getString("nome"),
                rs.getString("cognome"),
                rs.getString("email"),
                rs.getString("patente"),
                rs.getBigDecimal("credito")
        );
    }
}