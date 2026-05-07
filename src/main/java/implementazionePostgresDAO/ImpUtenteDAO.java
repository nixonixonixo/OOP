package implementazionePostgresDAO;

import dao.UtenteDAO;
import database.ConnessioneDatabase;
import model.Utente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImpUtenteDAO implements UtenteDAO {

    @Override
    public void salvaUtente(Utente utente) throws SQLException {

        String sql =
                "INSERT INTO UTENTE VALUES (?, ?, ?, ?, ?, ?)";

        try (
                Connection conn =
                        ConnessioneDatabase.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(1, utente.getIdUtente());
            ps.setString(2, utente.getUsername());
            ps.setString(3, utente.getPasswordHash());
            ps.setString(4, utente.getNome());
            ps.setString(5, utente.getCognome());
            ps.setString(6, utente.getEmail());

            ps.executeUpdate();
        }
    }

    @Override
    public Utente trovaUtentePerId(int idUtente)
            throws SQLException {

        String sql =
                "SELECT * FROM UTENTE WHERE idUtente = ?";

        try (
                Connection conn =
                        ConnessioneDatabase.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idUtente);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new Utente(
                        rs.getInt("idUtente"),
                        rs.getString("username"),
                        rs.getString("passwordHash"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email")
                );
            }
        }

        return null;
    }

    @Override
    public Utente trovaUtentePerUsername(String username)
            throws SQLException {

        String sql =
                "SELECT * FROM UTENTE WHERE username = ?";

        try (
                Connection conn =
                        ConnessioneDatabase.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new Utente(
                        rs.getInt("idUtente"),
                        rs.getString("username"),
                        rs.getString("passwordHash"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email")
                );
            }
        }

        return null;
    }

    @Override
    public List<Utente> trovaTuttiUtenti()
            throws SQLException {

        List<Utente> utenti = new ArrayList<>();

        String sql = "SELECT * FROM UTENTE";

        try (
                Connection conn =
                        ConnessioneDatabase.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                utenti.add(
                        new Utente(
                                rs.getInt("idUtente"),
                                rs.getString("username"),
                                rs.getString("passwordHash"),
                                rs.getString("nome"),
                                rs.getString("cognome"),
                                rs.getString("email")
                        )
                );
            }
        }

        return utenti;
    }

    @Override
    public void aggiornaUtente(Utente utente)
            throws SQLException {

        String sql = """
                UPDATE UTENTE
                SET username = ?,
                    passwordHash = ?,
                    nome = ?,
                    cognome = ?,
                    email = ?
                WHERE idUtente = ?
                """;

        try (
                Connection conn =
                        ConnessioneDatabase.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setString(1, utente.getUsername());
            ps.setString(2, utente.getPasswordHash());
            ps.setString(3, utente.getNome());
            ps.setString(4, utente.getCognome());
            ps.setString(5, utente.getEmail());
            ps.setInt(6, utente.getIdUtente());

            ps.executeUpdate();
        }
    }

    @Override
    public void eliminaUtente(int idUtente)
            throws SQLException {

        String sql =
                "DELETE FROM UTENTE WHERE idUtente = ?";

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