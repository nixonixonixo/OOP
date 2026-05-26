package implementazionePostgresDAO;

import dao.PrenotazioneDAO;
import database.ConnessioneDatabase;
import model.Auto;
import model.Cliente;
import model.Prenotazione;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione DAO per la gestione delle Prenotazioni su database PostgreSQL.
 * Gestisce l'integrazione tra le entità Prenotazione, Auto e Cliente.
 */
public class ImpPrenotazioneDAO implements PrenotazioneDAO {

    /**
     * Salva una nuova prenotazione nel database.
     *
     * @param p l'oggetto Prenotazione da persistere
     * @throws SQLException se si verifica un errore durante l'esecuzione dell'istruzione SQL
     */
    @Override
    public void salvaPrenotazione(Prenotazione p) throws SQLException {
        String sql = """
            INSERT INTO PRENOTAZIONE (datainizio, datafine, stato, idcliente, idauto)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(p.getDataInizio().getTime()));

            if (p.getDataFine() != null) {
                ps.setDate(2, new java.sql.Date(p.getDataFine().getTime()));
            } else {
                ps.setNull(2, Types.DATE);
            }

            ps.setString(3, p.getStato().name());
            ps.setInt(4, p.getCliente().getIdUtente());
            ps.setInt(5, p.getAuto().getIdAuto());

            ps.executeUpdate();
        }
    }

    /**
     * Recupera una prenotazione dal database tramite il suo ID univoco.
     *
     * @param idPrenotazione l'ID della prenotazione
     * @return l'oggetto Prenotazione popolato, o null se non trovato
     * @throws SQLException se si verifica un errore durante l'esecuzione della query
     */
    @Override
    public Prenotazione trovaPrenotazionePerId(int idPrenotazione) throws SQLException {
        String sql = """
            SELECT p.idprenotazione, p.datainizio, p.datafine, p.stato,
                   a.idauto, a.targa, a.modello, a.stato AS stato_auto, a.costogiornaliero,
                   u.idutente, u.username, u.passwordhash, u.nome, u.cognome, u.email,
                   c.patente, c.credito
            FROM PRENOTAZIONE p
            JOIN AUTO a ON p.idauto = a.idauto
            JOIN CLIENTE c ON p.idcliente = c.idutente
            JOIN UTENTE u ON c.idutente = u.idutente
            WHERE p.idprenotazione = ?
        """;
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPrenotazione);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mappaResultSetCompleto(rs);
        }
        return null;
    }

    /**
     * Recupera tutte le prenotazioni effettuate da un cliente specifico.
     *
     * @param idCliente l'ID del cliente
     * @return lista di prenotazioni del cliente
     * @throws SQLException se si verifica un errore durante l'esecuzione della query
     */
    @Override
    public List<Prenotazione> trovaPrenotazioniCliente(int idCliente) throws SQLException {
        List<Prenotazione> lista = new ArrayList<>();
        String sql = """
            SELECT p.idprenotazione, p.datainizio, p.datafine, p.stato,
                   a.idauto, a.targa, a.modello, a.stato AS stato_auto, a.costogiornaliero,
                   u.idutente, u.username, u.passwordhash, u.nome, u.cognome, u.email,
                   c.patente, c.credito
            FROM PRENOTAZIONE p
            JOIN AUTO a ON p.idauto = a.idauto
            JOIN CLIENTE c ON p.idcliente = c.idutente
            JOIN UTENTE u ON c.idutente = u.idutente
            WHERE p.idcliente = ?
        """;
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mappaResultSetCompleto(rs));
        }
        return lista;
    }

    /**
     * Recupera l'elenco completo di tutte le prenotazioni registrate.
     *
     * @return lista di tutte le prenotazioni
     * @throws SQLException se si verifica un errore durante l'esecuzione della query
     */
    @Override
    public List<Prenotazione> trovaTuttePrenotazioni() throws SQLException {
        List<Prenotazione> lista = new ArrayList<>();
        String sql = """
            SELECT p.idprenotazione, p.datainizio, p.datafine, p.stato,
                   a.idauto, a.targa, a.modello, a.stato AS stato_auto, a.costogiornaliero,
                   u.idutente, u.username, u.passwordhash, u.nome, u.cognome, u.email,
                   c.patente, c.credito
            FROM PRENOTAZIONE p
            JOIN AUTO a ON p.idauto = a.idauto
            JOIN CLIENTE c ON p.idcliente = c.idutente
            JOIN UTENTE u ON c.idutente = u.idutente
        """;
        try (Connection conn = ConnessioneDatabase.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mappaResultSetCompleto(rs));
        }
        return lista;
    }

    /**
     * Aggiorna le date e lo stato di una prenotazione esistente.
     *
     * @param p l'oggetto Prenotazione aggiornato
     * @throws SQLException se si verifica un errore durante l'esecuzione dell'istruzione SQL
     */
    @Override
    public void aggiornaPrenotazione(Prenotazione p) throws SQLException {
        String sql = """
            UPDATE PRENOTAZIONE
            SET datainizio = ?, datafine = ?, stato = ?
            WHERE idprenotazione = ?
        """;
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(p.getDataInizio().getTime()));
            if (p.getDataFine() != null) ps.setDate(2, new java.sql.Date(p.getDataFine().getTime()));
            else ps.setNull(2, Types.DATE);
            ps.setString(3, p.getStato().name());
            ps.setInt(4, p.getIdPrenotazione());
            ps.executeUpdate();
        }
    }

    /**
     * Elimina una prenotazione dal database.
     *
     * @param idPrenotazione l'ID della prenotazione da eliminare
     * @throws SQLException se si verifica un errore durante l'esecuzione dell'istruzione SQL
     */
    @Override
    public void eliminaPrenotazione(int idPrenotazione) throws SQLException {
        String sql = "DELETE FROM PRENOTAZIONE WHERE idprenotazione = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPrenotazione);
            ps.executeUpdate();
        }
    }

    /**
     * Crea una nuova prenotazione in stato 'IN_ATTESA' usando la data corrente.
     *
     * @param idCliente l'ID del cliente
     * @param idAuto    l'ID dell'auto
     * @throws SQLException se si verifica un errore durante l'esecuzione della query
     */
    @Override
    public void creaPrenotazione(int idCliente, int idAuto) throws SQLException {
        String sql = """
            INSERT INTO PRENOTAZIONE (datainizio, datafine, stato, idcliente, idauto)
            VALUES (CURRENT_DATE, NULL, 'IN_ATTESA', ?, ?)
        """;
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ps.setInt(2, idAuto);
            ps.executeUpdate();
        }
    }

    /**
     * Cerca una prenotazione attiva per una specifica auto.
     *
     * @param idAuto l'ID dell'auto
     * @return la prenotazione trovata, o null se l'auto è libera
     * @throws SQLException se si verifica un errore durante l'esecuzione della query
     */
    @Override
    public Prenotazione trovaPrenotazionePerAuto(int idAuto) throws SQLException {
        String sql = """
            SELECT p.idprenotazione, p.datainizio, p.datafine, p.stato,
                   a.idauto, a.targa, a.modello, a.stato AS stato_auto, a.costogiornaliero,
                   u.idutente, u.username, u.passwordhash, u.nome, u.cognome, u.email,
                   c.patente, c.credito
            FROM PRENOTAZIONE p
            JOIN AUTO a ON p.idauto = a.idauto
            JOIN CLIENTE c ON p.idcliente = c.idutente
            JOIN UTENTE u ON c.idutente = u.idutente
            WHERE p.idauto = ? AND p.stato <> 'ANNULLATA'
            LIMIT 1
        """;
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAuto);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mappaResultSetCompleto(rs);
        }
        return null;
    }

    /**
     * Aggiorna lo stato di una prenotazione specifica.
     *
     * @param idPrenotazione l'ID della prenotazione
     * @param nuovoStato     il nuovo stato da impostare
     * @throws SQLException se si verifica un errore durante l'esecuzione della query
     */
    @Override
    public void aggiornaStatoPrenotazione(int idPrenotazione, Prenotazione.StatoPren nuovoStato) throws SQLException {
        String sql = "UPDATE PRENOTAZIONE SET stato = ? WHERE idprenotazione = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuovoStato.name());
            ps.setInt(2, idPrenotazione);
            ps.executeUpdate();
        }
    }

    /**
     * Metodo di supporto per mappare una riga del ResultSet in un oggetto Prenotazione completo di Auto e Cliente.
     *
     * @param rs il ResultSet posizionato sulla riga corrente
     * @return un'istanza di Prenotazione popolata
     * @throws SQLException se si verifica un errore nella lettura delle colonne
     */
    private Prenotazione mappaResultSetCompleto(ResultSet rs) throws SQLException {
        Auto auto = new Auto(
                rs.getInt("idauto"),
                rs.getString("targa"),
                rs.getString("modello"),
                Auto.StatoAuto.valueOf(rs.getString("stato_auto").toUpperCase()),
                rs.getBigDecimal("costogiornaliero")
        );
        Cliente cliente = new Cliente(
                rs.getInt("idutente"),
                rs.getString("username"),
                rs.getString("passwordhash"),
                rs.getString("nome"),
                rs.getString("cognome"),
                rs.getString("email"),
                rs.getString("patente"),
                rs.getBigDecimal("credito")
        );
        return new Prenotazione(
                rs.getInt("idprenotazione"),
                rs.getDate("datainizio"),
                rs.getDate("datafine"),
                Prenotazione.StatoPren.valueOf(rs.getString("stato").toUpperCase()),
                cliente,
                auto
        );
    }
}