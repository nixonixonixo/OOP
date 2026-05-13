package implementazionePostgresDAO;

import dao.UtenteDAO;
import database.ConnessioneDatabase;
import model.Utente;
import model.Cliente;
import model.Operatore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImpUtenteDAO implements UtenteDAO {

    @Override
    public void salvaUtente(Utente utente) throws SQLException {
        String sql = "INSERT INTO UTENTE (idutente, username, passwordhash, nome, cognome, email) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

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
    public Utente trovaUtentePerId(int idUtente) throws SQLException {
        // Uso la stessa logica di trovaUtentePerUsername per caricare Cliente/Operatore correttamente
        String sql = """
            SELECT u.*, c.patente, c.credito, o.ruolo as ruolo_op, o.idutente as id_op
            FROM UTENTE u
            LEFT JOIN CLIENTE c ON u.idutente = c.idutente
            LEFT JOIN OPERATORE o ON u.idutente = o.idutente
            WHERE u.idutente = ?
            """;

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtente);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mappaResultSetInUtente(rs);
            }
        }
        return null;
    }

    @Override
    public Utente trovaUtentePerUsername(String username) throws SQLException {
        String sql = """
            SELECT u.idutente, u.username, u.passwordhash, u.nome, u.cognome, u.email, 
                   c.patente, c.credito, 
                   o.ruolo as ruolo_op, o.idutente as id_op
            FROM UTENTE u
            LEFT JOIN CLIENTE c ON u.idutente = c.idutente
            LEFT JOIN OPERATORE o ON u.idutente = o.idutente
            WHERE u.username = ?
            """;

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mappaResultSetInUtente(rs);
            }
        }
        return null;
    }

    private Utente mappaResultSetInUtente(ResultSet rs) throws SQLException {
        int id = rs.getInt("idutente");
        String user = rs.getString("username");
        String pass = rs.getString("passwordhash");
        String nome = rs.getString("nome");
        String cognome = rs.getString("cognome");
        String email = rs.getString("email");

        rs.getInt("id_op");
        if (!rs.wasNull()) {
            String ruoloString = rs.getString("ruolo_op");
            Operatore.Ruolo ruoloEnum = Operatore.Ruolo.valueOf(ruoloString.toUpperCase());
            return new Operatore(id, user, pass, nome, cognome, email, ruoloEnum);
        }

        String patente = rs.getString("patente");
        if (patente != null || rs.getBigDecimal("credito") != null) {
            return new Cliente(id, user, pass, nome, cognome, email, patente, rs.getBigDecimal("credito"));
        }

        return new Utente(id, user, pass, nome, cognome, email);
    }

    @Override
    public List<Utente> trovaTuttiUtenti() throws SQLException {
        List<Utente> utenti = new ArrayList<>();
        String sql = "SELECT * FROM UTENTE";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                utenti.add(new Utente(
                        rs.getInt("idutente"),
                        rs.getString("username"),
                        rs.getString("passwordhash"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email")
                ));
            }
        }
        return utenti;
    }

    @Override
    public void aggiornaUtente(Utente utente) throws SQLException {
        String sql = "UPDATE UTENTE SET username = ?, passwordhash = ?, nome = ?, cognome = ?, email = ? WHERE idutente = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

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
    public void eliminaUtente(int idUtente) throws SQLException {
        String sql = "DELETE FROM UTENTE WHERE idutente = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtente);
            ps.executeUpdate();
        }
    }
}