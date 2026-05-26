package implementazionePostgresDAO;

import dao.ClienteDAO;
import database.ConnessioneDatabase;
import model.Cliente;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione DAO per la persistenza dei dati relativi ai Clienti su database PostgreSQL.
 * Gestisce l'interazione tra l'entità Cliente e le tabelle UTENTE e CLIENTE.
 */
public class ImpClienteDAO implements ClienteDAO {

    /**
     * Salva i dati specifici del cliente nel database.
     *
     * @param cliente l'oggetto cliente contenente i dati da persistere
     * @throws SQLException se si verifica un errore durante l'esecuzione della query
     */
    @Override
    public void salvaCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO CLIENTE (idutente, patente, credito) VALUES (?, ?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cliente.getIdUtente());
            ps.setString(2, cliente.getPatente());
            ps.setBigDecimal(3, cliente.getCredito());

            ps.executeUpdate();
        }
    }

    /**
     * Recupera un cliente dal database tramite il suo ID utente, unendo i dati delle tabelle UTENTE e CLIENTE.
     *
     * @param idUtente l'ID univoco dell'utente
     * @return l'oggetto Cliente popolato, o null se non trovato
     * @throws SQLException se si verifica un errore durante l'esecuzione della query
     */
    @Override
    public Cliente trovaClientePerId(int idUtente) throws SQLException {
        String sql = """
                SELECT u.*, c.patente, c.credito
                FROM UTENTE u
                LEFT JOIN CLIENTE c ON u.idutente = c.idutente
                WHERE u.idutente = ?
                """;

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtente);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mappaResultSetInCliente(rs);
                }
            }
        }

        return null;
    }

    /**
     * Recupera l'elenco completo di tutti i clienti presenti nel sistema.
     *
     * @return una lista di oggetti Cliente
     * @throws SQLException se si verifica un errore durante l'esecuzione della query
     */
    @Override
    public List<Cliente> trovaTuttiClienti() throws SQLException {
        List<Cliente> clienti = new ArrayList<>();

        String sql = """
                SELECT u.*, c.patente, c.credito
                FROM UTENTE u
                LEFT JOIN CLIENTE c ON u.idutente = c.idutente
                """;

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente c = mappaResultSetInCliente(rs);
                if (c.getPatente() != null) {
                    clienti.add(c);
                }
            }
        }

        return clienti;
    }

    /**
     * Aggiorna i dati anagrafici e finanziari del cliente.
     *
     * @param cliente l'oggetto Cliente con i dati aggiornati
     * @throws SQLException se si verifica un errore durante l'esecuzione della query
     */
    @Override
    public void aggiornaCliente(Cliente cliente) throws SQLException {
        String sql = """
                UPDATE CLIENTE
                SET patente = ?, credito = ?
                WHERE idutente = ?
                """;

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cliente.getPatente());
            ps.setBigDecimal(2, cliente.getCredito());
            ps.setInt(3, cliente.getIdUtente());

            ps.executeUpdate();
        }
    }

    /**
     * Elimina i dati del cliente dal database.
     *
     * @param idUtente l'ID dell'utente da eliminare
     * @throws SQLException se si verifica un errore durante l'esecuzione della query
     */
    @Override
    public void eliminaCliente(int idUtente) throws SQLException {
        String sql = "DELETE FROM CLIENTE WHERE idutente = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtente);
            ps.executeUpdate();
        }
    }

    /**
     * Aggiorna il credito disponibile del cliente.
     *
     * @param idUtente     l'ID dell'utente
     * @param nuovoCredito il nuovo valore del credito
     * @throws SQLException se l'utente non viene trovato o c'è un errore di database
     */
    @Override
    public void aggiornaCredito(int idUtente, BigDecimal nuovoCredito) throws SQLException {
        String sql = "UPDATE CLIENTE SET credito = ? WHERE idutente = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBigDecimal(1, nuovoCredito);
            ps.setInt(2, idUtente);

            if (ps.executeUpdate() == 0) {
                throw new SQLException("Cliente non trovato");
            }
        }
    }

    /**
     * Riduce il saldo del cliente in seguito a un pagamento effettuato.
     *
     * @param idCliente l'ID del cliente
     * @param importo   l'importo da detrarre
     * @throws SQLException se si verifica un errore durante l'operazione di aggiornamento
     */
    @Override
    public void prelevaSaldo(int idCliente, BigDecimal importo) throws SQLException {
        String sql = "UPDATE CLIENTE SET credito = credito - ? WHERE idutente = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, importo);
            ps.setInt(2, idCliente);
            ps.executeUpdate();
        }
    }

    /**
     * Metodo di supporto per mappare una riga del ResultSet in un oggetto Cliente.
     *
     * @param rs il ResultSet corrente
     * @return un'istanza di Cliente popolata
     * @throws SQLException se si verifica un errore nella lettura delle colonne
     */
    private Cliente mappaResultSetInCliente(ResultSet rs) throws SQLException {
        BigDecimal credito = rs.getBigDecimal("credito");
        if (credito == null) {
            credito = BigDecimal.ZERO;
        }

        return new Cliente(
                rs.getInt("idutente"),
                rs.getString("username"),
                rs.getString("passwordhash"),
                rs.getString("nome"),
                rs.getString("cognome"),
                rs.getString("email"),
                rs.getString("patente"),
                credito
        );
    }
}