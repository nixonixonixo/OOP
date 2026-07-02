package implementazionePostgresDAO;

import dao.OperatoreDAO;
import database.ConnessioneDatabase;
import model.Operatore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione DAO per la persistenza dei dati degli Operatori su database PostgreSQL.
 */
public class ImpOperatoreDAO implements OperatoreDAO {

    /**
     * Salva i dati specifici dell'operatore nel database.
     *
     * @param operatore l'oggetto Operatore da persistere
     * @throws SQLException se si verifica un errore durante l'esecuzione della query
     */
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

    /**
     * Recupera un operatore dal database tramite il suo ID, unendo i dati delle tabelle UTENTE e OPERATORE.
     *
     * @param idUtente l'ID univoco dell'utente
     * @return l'oggetto Operatore popolato, o null se non trovato
     * @throws SQLException se si verifica un errore durante l'esecuzione della query
     */
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

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mappaResultSetInOperatore(rs);
                }
            }
        }

        return null;
    }

    /**
     * Recupera l'elenco di tutti gli operatori registrati nel sistema.
     *
     * @return una lista di oggetti Operatore
     * @throws SQLException se si verifica un errore durante l'esecuzione della query
     */
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

    /**
     * Aggiorna il ruolo di un operatore esistente.
     *
     * @param operatore l'oggetto Operatore contenente il nuovo ruolo
     * @throws SQLException se si verifica un errore durante l'esecuzione della query
     */
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

    /**
     * Elimina l'associazione dell'operatore dal database.
     *
     * @param idUtente l'ID dell'utente da rimuovere dal ruolo di operatore
     * @throws SQLException se si verifica un errore durante l'esecuzione della query
     */
    @Override
    public void eliminaOperatore(int idUtente) throws SQLException {
        String sql = "DELETE FROM OPERATORE WHERE idutente = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtente);
            ps.executeUpdate();
        }
    }

    /**
     * Metodo di supporto per mappare una riga del ResultSet in un oggetto Operatore.
     *
     * @param rs il ResultSet posizionato sulla riga corrente
     * @return un'istanza di Operatore popolata con i dati del database
     * @throws SQLException se si verifica un errore nella lettura delle colonne
     */
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