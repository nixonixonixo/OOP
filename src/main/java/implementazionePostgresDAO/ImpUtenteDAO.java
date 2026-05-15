package implementazionePostgresDAO;

import dao.UtenteDAO;
import database.ConnessioneDatabase;
import model.Utente;
import model.Cliente;
import model.Operatore;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImpUtenteDAO implements UtenteDAO {

    @Override
    public void salvaUtente(Utente utente) throws SQLException {
        String sql = "INSERT INTO utente (idutente, username, passwordhash, nome, cognome, email) VALUES (?, ?, ?, ?, ?, ?)";

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
        String sql = """
            SELECT u.*, c.patente, c.credito, o.ruolo
            FROM utente u
            LEFT JOIN cliente c ON u.idutente = c.idutente
            LEFT JOIN operatore o ON u.idutente = o.idutente
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
            SELECT u.*, c.patente, c.credito, o.ruolo
            FROM utente u
            LEFT JOIN cliente c ON u.idutente = c.idutente
            LEFT JOIN operatore o ON u.idutente = o.idutente
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

    @Override
    public List<Utente> trovaTuttiUtenti() throws SQLException {
        List<Utente> utenti = new ArrayList<>();

        String sql = """
            SELECT u.*, c.patente, c.credito, o.ruolo
            FROM utente u
            LEFT JOIN cliente c ON u.idutente = c.idutente
            LEFT JOIN operatore o ON u.idutente = o.idutente
            """;

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                utenti.add(mappaResultSetInUtente(rs));
            }
        }

        return utenti;
    }

    @Override
    public void aggiornaUtente(Utente utente) throws SQLException {
        String sql = """
            UPDATE utente
            SET username = ?, passwordhash = ?, nome = ?, cognome = ?, email = ?
            WHERE idutente = ?
            """;

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
        String sql = "DELETE FROM utente WHERE idutente = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtente);
            ps.executeUpdate();
        }
    }

    private Utente mappaResultSetInUtente(ResultSet rs) throws SQLException {
        // 1. Recupero dati comuni
        int id = rs.getInt("idutente");
        String username = rs.getString("username");
        String passwordHash = rs.getString("passwordhash"); // Questo è l'hash dal DB
        String nome = rs.getString("nome");
        String cognome = rs.getString("cognome");
        String email = rs.getString("email");

        // 2. Controllo se è un Operatore
        String ruoloStr = rs.getString("ruolo");
        if (ruoloStr != null) {
            Operatore.Ruolo ruolo;
            try {
                ruolo = Operatore.Ruolo.valueOf(ruoloStr.trim().toUpperCase());
            } catch (Exception e) {
                ruolo = Operatore.Ruolo.ADDETTO_NOLEGGIO;
            }
            // USIAMO IL COSTRUTTORE CON IL FLAG TRUE
            return new Operatore(id, username, passwordHash, nome, cognome, email, ruolo, true);
        }

        // 3. Controllo se è un Cliente
        String patente = rs.getString("patente");
        if (patente != null) {
            BigDecimal credito = rs.getBigDecimal("credito");
            if (credito == null) credito = BigDecimal.ZERO;
            // USIAMO IL COSTRUTTORE CON IL FLAG TRUE
            return new Cliente(id, username, passwordHash, nome, cognome, email, patente, credito, true);
        }

        // 4. Utente generico (fallback)
        return new Utente(id, username, passwordHash, nome, cognome, email, true);
    }
}