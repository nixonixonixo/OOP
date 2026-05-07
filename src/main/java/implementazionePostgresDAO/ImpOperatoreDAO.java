package implementazionePostgresDAO;

import dao.OperatoreDAO;
import database.ConnessioneDatabase;
import model.Operatore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImpOperatoreDAO implements OperatoreDAO {

    @Override
    public void salvaOperatore(Operatore operatore)
            throws SQLException {

        String sql =
                "INSERT INTO OPERATORE VALUES (?, ?)";

        try (
                Connection conn =
                        ConnessioneDatabase.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(1, operatore.getIdUtente());
            ps.setString(2, operatore.getRuolo().name());

            ps.executeUpdate();
        }
    }

    @Override
    public Operatore trovaOperatorePerId(int idUtente)
            throws SQLException {

        String sql = """
                SELECT *
                FROM UTENTE u
                JOIN OPERATORE o
                ON u.idUtente = o.idUtente
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

                return new Operatore(
                        rs.getInt("idUtente"),
                        rs.getString("username"),
                        rs.getString("passwordHash"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        Operatore.Ruolo.valueOf(
                                rs.getString("ruolo")
                        )
                );
            }
        }

        return null;
    }

    @Override
    public List<Operatore> trovaTuttiOperatori()
            throws SQLException {

        List<Operatore> operatori =
                new ArrayList<>();

        String sql = """
                SELECT *
                FROM UTENTE u
                JOIN OPERATORE o
                ON u.idUtente = o.idUtente
                """;

        try (
                Connection conn =
                        ConnessioneDatabase.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                operatori.add(
                        new Operatore(
                                rs.getInt("idUtente"),
                                rs.getString("username"),
                                rs.getString("passwordHash"),
                                rs.getString("nome"),
                                rs.getString("cognome"),
                                rs.getString("email"),
                                Operatore.Ruolo.valueOf(
                                        rs.getString("ruolo")
                                )
                        )
                );
            }
        }

        return operatori;
    }

    @Override
    public void aggiornaOperatore(Operatore operatore)
            throws SQLException {

        String sql = """
                UPDATE OPERATORE
                SET ruolo = ?
                WHERE idUtente = ?
                """;

        try (
                Connection conn =
                        ConnessioneDatabase.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setString(1,
                    operatore.getRuolo().name());

            ps.setInt(2,
                    operatore.getIdUtente());

            ps.executeUpdate();
        }
    }

    @Override
    public void eliminaOperatore(int idUtente)
            throws SQLException {

        String sql =
                "DELETE FROM OPERATORE WHERE idUtente = ?";

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
}