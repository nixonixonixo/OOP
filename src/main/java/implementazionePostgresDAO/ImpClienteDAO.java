package implementazionePostgresDAO;

import dao.ClienteDAO;
import database.ConnessioneDatabase;
import model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImpClienteDAO implements ClienteDAO {

    @Override
    public void salvaCliente(Cliente cliente)
            throws SQLException {

        String sql =
                "INSERT INTO CLIENTE VALUES (?, ?, ?)";

        try (
                Connection conn =
                        ConnessioneDatabase.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(1, cliente.getIdUtente());
            ps.setString(2, cliente.getPatente());
            ps.setBigDecimal(3, cliente.getCredito());

            ps.executeUpdate();
        }
    }

    @Override
    public Cliente trovaClientePerId(int idUtente)
            throws SQLException {

        String sql = """
                SELECT *
                FROM UTENTE u
                JOIN CLIENTE c
                ON u.idUtente = c.idUtente
                WHERE u.idUtente = ?
                """;

        try (
                Connection conn =
                        ConnessioneDatabase.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idUtente);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new Cliente(
                        rs.getInt("idUtente"),
                        rs.getString("username"),
                        rs.getString("passwordHash"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("patente"),
                        rs.getBigDecimal("credito")
                );
            }
        }

        return null;
    }

    @Override
    public List<Cliente> trovaTuttiClienti()
            throws SQLException {

        List<Cliente> clienti = new ArrayList<>();

        String sql = """
                SELECT *
                FROM UTENTE u
                JOIN CLIENTE c
                ON u.idUtente = c.idUtente
                """;

        try (
                Connection conn =
                        ConnessioneDatabase.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                clienti.add(
                        new Cliente(
                                rs.getInt("idUtente"),
                                rs.getString("username"),
                                rs.getString("passwordHash"),
                                rs.getString("nome"),
                                rs.getString("cognome"),
                                rs.getString("email"),
                                rs.getString("patente"),
                                rs.getBigDecimal("credito")
                        )
                );
            }
        }

        return clienti;
    }

    @Override
    public void aggiornaCliente(Cliente cliente)
            throws SQLException {

        String sql = """
                UPDATE CLIENTE
                SET patente = ?,
                    credito = ?
                WHERE idUtente = ?
                """;

        try (
                Connection conn =
                        ConnessioneDatabase.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setString(1, cliente.getPatente());
            ps.setBigDecimal(2, cliente.getCredito());
            ps.setInt(3, cliente.getIdUtente());

            ps.executeUpdate();
        }
    }

    @Override
    public void eliminaCliente(int idUtente)
            throws SQLException {

        String sql =
                "DELETE FROM CLIENTE WHERE idUtente = ?";

        try (
                Connection conn =
                        ConnessioneDatabase.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idUtente);

            ps.executeUpdate();
        }
    }

    @Override
    public void aggiornaCredito(int idUtente, java.math.BigDecimal nuovoCredito) throws SQLException {

        String sql = """
                UPDATE CLIENTE
                SET credito = ?
                WHERE idUtente = ?
                """;

        try (
                Connection conn = ConnessioneDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setBigDecimal(1, nuovoCredito);
            ps.setInt(2, idUtente);

            int rowsAffected = ps.executeUpdate();

            // Opzionale: un piccolo controllo di sicurezza
            if (rowsAffected == 0) {
                throw new SQLException("Aggiornamento fallito: Cliente non trovato.");
            }
        }
    }
}