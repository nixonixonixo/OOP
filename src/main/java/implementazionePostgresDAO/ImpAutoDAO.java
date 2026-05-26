package implementazionePostgresDAO;

import dao.AutoDAO;
import database.ConnessioneDatabase;
import model.Auto;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Implementazione auto dao.
 */
public class ImpAutoDAO implements AutoDAO {

    /**
     * Salva una nuova entità Auto nel database.
     *
     * @param auto l'oggetto Auto da persistire
     * @throws SQLException se si verifica un errore durante l'esecuzione dell'istruzione SQL
     */
    @Override
    public void salvaAuto(Auto auto) throws SQLException {
        String sql = "INSERT INTO AUTO (idauto, targa, modello, stato, costogiornaliero) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, auto.getIdAuto());
            ps.setString(2, auto.getTarga());
            ps.setString(3, auto.getModello());
            ps.setString(4, auto.getStato().name());
            ps.setBigDecimal(5, auto.getCostoDaily());

            ps.executeUpdate();
        }
    }

    /**
     * Recupera un'Auto dal database tramite il suo identificativo univoco.
     *
     * @param idAuto l'ID dell'auto da cercare
     * @return l'oggetto Auto corrispondente, o null se non trovato
     * @throws SQLException se si verifica un errore durante l'esecuzione della query
     */
    @Override
    public Auto trovaAutoPerId(int idAuto) throws SQLException {
        String sql = "SELECT * FROM AUTO WHERE idauto = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idAuto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mappaResultSetInAuto(rs);
                }
            }
        }

        return null;
    }

    /**
     * Recupera l'elenco completo di tutte le auto presenti nel database.
     *
     * @return una lista di oggetti Auto
     * @throws SQLException se si verifica un errore durante l'esecuzione della query
     */
    @Override
    public List<Auto> trovaTutteAuto() throws SQLException {
        List<Auto> lista = new ArrayList<>();
        String sql = "SELECT * FROM AUTO";

        try (Connection conn = ConnessioneDatabase.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mappaResultSetInAuto(rs));
            }
        }

        return lista;
    }

    /**
     * Recupera l'elenco delle sole auto attualmente disponibili per il noleggio.
     *
     * @return una lista di oggetti Auto con stato DISPONIBILE
     * @throws SQLException se si verifica un errore durante l'esecuzione della query
     */
    @Override
    public List<Auto> trovaAutoDisponibili() throws SQLException {
        List<Auto> lista = new ArrayList<>();
        String sql = "SELECT * FROM AUTO WHERE stato = 'DISPONIBILE'";

        try (Connection conn = ConnessioneDatabase.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mappaResultSetInAuto(rs));
            }
        }

        return lista;
    }

    /**
     * Aggiorna i dati di un'auto esistente nel database.
     *
     * @param auto l'oggetto Auto contenente i dati aggiornati
     * @throws SQLException se si verifica un errore durante l'esecuzione dell'istruzione SQL
     */
    @Override
    public void aggiornaAuto(Auto auto) throws SQLException {
        String sql = "UPDATE AUTO SET targa = ?, modello = ?, stato = ?, costogiornaliero = ? WHERE idauto = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, auto.getTarga());
            ps.setString(2, auto.getModello());
            ps.setString(3, auto.getStato().name());
            ps.setBigDecimal(4, auto.getCostoDaily());
            ps.setInt(5, auto.getIdAuto());

            ps.executeUpdate();
        }
    }

    /**
     * Aggiorna esclusivamente lo stato di un'auto specifica nel database.
     *
     * @param idAuto l'ID dell'auto da aggiornare
     * @param stato  il nuovo stato da impostare
     * @throws SQLException se si verifica un errore durante l'esecuzione dell'istruzione SQL
     */
    @Override
    public void aggiornaStatoAuto(int idAuto, Auto.StatoAuto stato) throws SQLException {
        String sql = "UPDATE AUTO SET stato = ? WHERE idauto = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, stato.name());
            ps.setInt(2, idAuto);

            ps.executeUpdate();
        }
    }

    /**
     * Rimuove un'auto dal database tramite il suo identificativo.
     *
     * @param idAuto l'ID dell'auto da eliminare
     * @throws SQLException se si verifica un errore durante l'esecuzione dell'istruzione SQL
     */
    @Override
    public void eliminaAuto(int idAuto) throws SQLException {
        String sql = "DELETE FROM AUTO WHERE idauto = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idAuto);
            ps.executeUpdate();
        }
    }

    /**
     * Metodo di supporto per mappare una riga del ResultSet in un oggetto Auto.
     *
     * @param rs il ResultSet corrente posizionato sulla riga da mappare
     * @return un'istanza di Auto popolata con i dati del database
     * @throws SQLException se si verifica un errore nell'estrazione dei dati dalle colonne
     */
    private Auto mappaResultSetInAuto(ResultSet rs) throws SQLException {
        String statoStr = rs.getString("stato");
        Auto.StatoAuto statoEnum;

        try {
            statoEnum = Auto.StatoAuto.valueOf(statoStr.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            System.err.println("Errore mapping stato auto: " + statoStr + ". Impostato default DISPONIBILE.");
            statoEnum = Auto.StatoAuto.DISPONIBILE;
        }

        return new Auto(
                rs.getInt("idauto"),
                rs.getString("targa"),
                rs.getString("modello"),
                statoEnum,
                rs.getBigDecimal("costogiornaliero")
        );
    }
}