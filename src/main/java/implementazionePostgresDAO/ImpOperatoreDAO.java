package implementazionePostgresDAO;

import dao.OperatoreDAO;
import database.ConnessioneDatabase;
import model.Operatore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Imp operatore dao.
 */
public class ImpOperatoreDAO implements OperatoreDAO {

    @Override
    public void salvaOperatore(Operatore operatore) throws SQLException {

        String sql = """
            INSERT INTO OPERATORE (idutente, ruolo)
            VALUES (?, ?)
        """;

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, operatore.getIdUtente());
            ps.setString(2, operatore.getRuolo().name());

            ps.executeUpdate();
        }
    }

    @Override
    public Operatore trovaOperatorePerId(int idUtente) throws SQLException {

        String sql = """
            SELECT u.idutente, u.username, u.passwordhash, u.nome, u.cognome, u.email,
                   o.ruolo
            FROM UTENTE u
            JOIN OPERATORE o ON u.idutente = o.idutente
            WHERE u.idutente = ?
        """;

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtente);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mappaResultSetInOperatore(rs);
            }
        }

        return null;
    }

    @Override
    public List<Operatore> trovaTuttiOperatori() throws SQLException {

        List<Operatore> operatori = new ArrayList<>();

        String sql = """
            SELECT u.idutente, u.username, u.passwordhash, u.nome, u.cognome, u.email,
                   o.ruolo
            FROM UTENTE u
            JOIN OPERATORE o ON u.idutente = o.idutente
        """;

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                operatori.add(mappaResultSetInOperatore(rs));
            }
        }

        return operatori;
    }

    @Override
    public void aggiornaOperatore(Operatore operatore) throws SQLException {

        String sql = """
            UPDATE OPERATORE
            SET ruolo = ?
            WHERE idutente = ?
        """;

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, operatore.getRuolo().name());
            ps.setInt(2, operatore.getIdUtente());

            ps.executeUpdate();
        }
    }

    @Override
    public void eliminaOperatore(int idUtente) throws SQLException {

        String sql = "DELETE FROM OPERATORE WHERE idutente = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtente);
            ps.executeUpdate();
        }
    }

    private Operatore mappaResultSetInOperatore(ResultSet rs) throws SQLException {

        Operatore.Ruolo ruoloEnum = Operatore.Ruolo.valueOf(
                rs.getString("ruolo").toUpperCase()
        );

        return new Operatore(
                rs.getInt("idutente"),
                rs.getString("username"),
                rs.getString("passwordhash"),
                rs.getString("nome"),
                rs.getString("cognome"),
                rs.getString("email"),
                ruoloEnum
        );
    }
}