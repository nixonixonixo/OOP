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

/**
 * Implementazione DAO per la gestione degli Utenti su database PostgreSQL.
 * Gestisce l'identificazione e il recupero degli utenti,
 * distinguendo tra base Utente, Cliente o Operatore in base alle tabelle correlate.
 */
public class ImpUtenteDAO implements UtenteDAO {

    /**
     * Salva i dati anagrafici base dell'utente nel database.
     *
     * @param utente l'oggetto Utente da persistere
     * @throws SQLException se si verifica un errore durante l'esecuzione dell'istruzione SQL
     */
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

    /**
     * Recupera un utente dal database tramite il suo ID.
     * Effettua join con le tabelle Cliente e Operatore per ricostruire l'oggetto corretto.
     *
     * @param idUtente l'ID dell'utente
     * @return l'oggetto Utente (o la sottoclasse Cliente/Operatore) trovato, o null
     * @throws SQLException se si verifica un errore durante la query
     */
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

    /**
     * Cerca un utente per il suo username (utile per la logica di login).
     *
     * @param username lo username da cercare
     * @return l'oggetto Utente trovato, o null
     * @throws SQLException se si verifica un errore durante la query
     */
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

    /**
     * Recupera l'elenco completo di tutti gli utenti registrati.
     *
     * @return lista di oggetti Utente (polimorfici)
     * @throws SQLException se si verifica un errore durante la query
     */
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

    /**
     * Aggiorna le informazioni anagrafiche base dell'utente.
     *
     * @param utente l'oggetto Utente aggiornato
     * @throws SQLException se l'aggiornamento fallisce
     */
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

    /**
     * Elimina un utente dal sistema.
     *
     * @param idUtente l'ID dell'utente da eliminare
     * @throws SQLException se l'operazione fallisce
     */
    @Override
    public void eliminaUtente(int idUtente) throws SQLException {
        String sql = "DELETE FROM utente WHERE idutente = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtente);
            ps.executeUpdate();
        }
    }

    /**
     * Metodo privato di mapping che istanzia l'oggetto corretto (Utente, Cliente o Operatore)
     * analizzando la presenza di dati nelle tabelle esterne.
     *
     * @param rs il ResultSet posizionato sulla riga corrente
     * @return un'istanza dell'oggetto specifico (Cliente/Operatore/Utente)
     * @throws SQLException se la lettura fallisce
     */
    private Utente mappaResultSetInUtente(ResultSet rs) throws SQLException {
        int id = rs.getInt("idutente");
        String username = rs.getString("username");
        String passwordHash = rs.getString("passwordhash");
        String nome = rs.getString("nome");
        String cognome = rs.getString("cognome");
        String email = rs.getString("email");

        // Verifica se l'utente è un Operatore
        String ruoloStr = rs.getString("ruolo");
        if (ruoloStr != null) {
            Operatore.Ruolo ruolo;
            try {
                ruolo = Operatore.Ruolo.valueOf(ruoloStr.trim().toUpperCase());
            } catch (Exception e) {
                ruolo = Operatore.Ruolo.ADDETTO_NOLEGGIO;
            }
            return new Operatore(id, username, passwordHash, nome, cognome, email, ruolo, true);
        }

        // Verifica se l'utente è un Cliente
        String patente = rs.getString("patente");
        if (patente != null) {
            BigDecimal credito = rs.getBigDecimal("credito");
            if (credito == null) credito = BigDecimal.ZERO;
            return new Cliente(id, username, passwordHash, nome, cognome, email, patente, credito, true);
        }

        // Caso base: Utente generico
        return new Utente(id, username, passwordHash, nome, cognome, email, true);
    }
}