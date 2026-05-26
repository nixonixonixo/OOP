package implementazionePostgresDAO;

import dao.PagamentoDAO;
import database.ConnessioneDatabase;
import model.Pagamento;
import model.Noleggio;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione DAO per la gestione dei pagamenti su database PostgreSQL.
 * Gestisce la persistenza dei pagamenti e le operazioni correlate sul saldo dei clienti.
 */
public class ImpPagamentoDAO implements PagamentoDAO {

    /**
     * Incrementa il credito disponibile di un cliente nel database.
     *
     * @param idCliente l'ID dell'utente cliente
     * @param importo   l'importo da accreditare
     * @throws SQLException se l'aggiornamento fallisce o il cliente non esiste
     */
    @Override
    public void ricaricaSaldoCliente(int idCliente, BigDecimal importo) throws SQLException {
        String sql = "UPDATE CLIENTE SET credito = credito + ? WHERE idutente = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBigDecimal(1, importo);
            ps.setInt(2, idCliente);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Nessun cliente trovato con ID: " + idCliente);
            }
        }
    }

    /**
     * Salva una nuova richiesta di pagamento nel database.
     *
     * @param p l'oggetto Pagamento da persistere
     * @throws SQLException se si verifica un errore durante l'istruzione SQL
     */
    @Override
    public void salvaPagamento(Pagamento p) throws SQLException {
        String sql = "INSERT INTO PAGAMENTO (importo, stato, idnoleggio) VALUES (?, ?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBigDecimal(1, p.getImporto());
            ps.setString(2, p.getStato().toString());
            ps.setInt(3, p.getNoleggio().getIdNoleggio());

            ps.executeUpdate();
        }
    }

    /**
     * Recupera tutti i pagamenti associati a uno specifico cliente.
     *
     * @param idCliente l'ID del cliente
     * @return una lista di pagamenti trovati
     * @throws SQLException se si verifica un errore durante la query
     */
    @Override
    public List<Pagamento> trovaPagamentiCliente(int idCliente) throws SQLException {
        List<Pagamento> lista = new ArrayList<>();

        String sql = """
            SELECT p.*, n.idnoleggio, n.dataritiro
            FROM PAGAMENTO p
            JOIN NOLEGGIO n ON p.idnoleggio = n.idnoleggio
            JOIN PRENOTAZIONE pr ON n.idprenotazione = pr.idprenotazione
            WHERE pr.idcliente = ?
        """;
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mappaResultSetInPagamento(rs));
            }
        }
        return lista;
    }

    /**
     * Recupera un pagamento tramite il suo ID univoco.
     *
     * @param idPagamento l'ID del pagamento
     * @return l'oggetto Pagamento o null se non trovato
     * @throws SQLException se si verifica un errore durante la query
     */
    @Override
    public Pagamento trovaPagamentoPerId(int idPagamento) throws SQLException {
        String sql = "SELECT p.*, n.idnoleggio, n.dataritiro FROM PAGAMENTO p JOIN NOLEGGIO n ON p.idnoleggio = n.idnoleggio WHERE p.idpagamento = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPagamento);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mappaResultSetInPagamento(rs);
        }
        return null;
    }

    /**
     * Recupera tutti i pagamenti associati a un noleggio.
     *
     * @param idNoleggio l'ID del noleggio
     * @return lista di pagamenti
     * @throws SQLException se si verifica un errore durante la query
     */
    @Override
    public List<Pagamento> trovaPagamentiNoleggio(int idNoleggio) throws SQLException {
        List<Pagamento> lista = new ArrayList<>();
        String sql = "SELECT p.*, n.idnoleggio, n.dataritiro FROM PAGAMENTO p JOIN NOLEGGIO n ON p.idnoleggio = n.idnoleggio WHERE p.idnoleggio = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idNoleggio);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mappaResultSetInPagamento(rs));
        }
        return lista;
    }

    /**
     * Recupera l'elenco completo dei pagamenti nel sistema.
     *
     * @return lista di tutti i pagamenti
     * @throws SQLException se si verifica un errore durante la query
     */
    @Override
    public List<Pagamento> trovaTuttiPagamenti() throws SQLException {
        List<Pagamento> lista = new ArrayList<>();
        String sql = "SELECT p.*, n.idnoleggio, n.dataritiro FROM PAGAMENTO p JOIN NOLEGGIO n ON p.idnoleggio = n.idnoleggio";
        try (Connection conn = ConnessioneDatabase.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mappaResultSetInPagamento(rs));
        }
        return lista;
    }

    /**
     * Aggiorna i dati di un pagamento esistente.
     *
     * @param p l'oggetto Pagamento aggiornato
     * @throws SQLException se l'aggiornamento fallisce
     */
    @Override
    public void aggiornaPagamento(Pagamento p) throws SQLException {
        String sql = "UPDATE PAGAMENTO SET importo = ?, stato = ? WHERE idpagamento = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, p.getImporto());
            ps.setString(2, p.getStato().name());
            ps.setInt(3, p.getIdPagamento());
            ps.executeUpdate();
        }
    }

    /**
     * Elimina un pagamento dal database.
     *
     * @param idPagamento l'ID del pagamento da eliminare
     * @throws SQLException se la cancellazione fallisce
     */
    @Override
    public void eliminaPagamento(int idPagamento) throws SQLException {
        String sql = "DELETE FROM PAGAMENTO WHERE idpagamento = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPagamento);
            ps.executeUpdate();
        }
    }

    /**
     * Aggiorna lo stato di un pagamento specifico (es. da IN_ATTESA a COMPLETATO).
     *
     * @param idPagamento l'ID del pagamento
     * @param nuovoStato  il nuovo stato da impostare
     * @throws SQLException se l'aggiornamento fallisce
     */
    @Override
    public void aggiornaStatoPagamento(int idPagamento, Pagamento.StatoPagamento nuovoStato) throws SQLException {
        String sql = "UPDATE PAGAMENTO SET stato = ? WHERE idpagamento = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuovoStato.name());
            ps.setInt(2, idPagamento);
            ps.executeUpdate();
        }
    }

    /**
     * Metodo di supporto per mappare una riga del ResultSet in un oggetto Pagamento.
     *
     * @param rs il ResultSet posizionato sulla riga corrente
     * @return un'istanza di Pagamento popolata
     * @throws SQLException se la lettura delle colonne fallisce
     */
    private Pagamento mappaResultSetInPagamento(ResultSet rs) throws SQLException {
        Noleggio noleggio = new Noleggio(rs.getInt("idnoleggio"), rs.getDate("dataritiro"), null);
        String statoStr = rs.getString("stato").toUpperCase();
        Pagamento.StatoPagamento statoEnum = Pagamento.StatoPagamento.valueOf(statoStr);
        return new Pagamento(rs.getInt("idpagamento"), rs.getBigDecimal("importo"), statoEnum, noleggio);
    }
}