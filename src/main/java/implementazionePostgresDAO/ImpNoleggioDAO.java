package implementazionePostgresDAO;

import dao.NoleggioDAO;
import database.ConnessioneDatabase;
import model.Cliente;
import model.Noleggio;
import model.Prenotazione;
import model.Auto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImpNoleggioDAO implements NoleggioDAO {

    private static final String SELECT_QUERY = """
            SELECT 
                n.idnoleggio, n.dataritiro, n.datarestituzione, n.costototale,
                n.idprenotazione,
                p.datainizio, p.datafine, p.stato AS stato_pren,
                a.idauto, a.targa, a.modello, a.stato AS stato_auto, a.costogiornaliero,
                u.idutente AS id_utente_finale, u.nome, u.cognome, u.email, c.patente
            FROM NOLEGGIO n
            JOIN PRENOTAZIONE p ON n.idprenotazione = p.idprenotazione
            JOIN AUTO a ON p.idauto = a.idauto
            JOIN CLIENTE c ON p.idcliente = c.idutente
            JOIN UTENTE u ON c.idutente = u.idutente
            """;

    @Override
    public void salvaNoleggio(Noleggio noleggio) throws SQLException {
        String sql = "INSERT INTO NOLEGGIO (dataritiro, costototale, idprenotazione) VALUES (?, ?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(noleggio.getDataRitiro().getTime()));

            if (noleggio.getCostoTot() != null) {
                ps.setBigDecimal(2, noleggio.getCostoTot());
            } else {
                ps.setNull(2, Types.NUMERIC);
            }

            ps.setInt(3, noleggio.getPrenotazione().getIdPrenotazione());
            ps.executeUpdate();
        }
    }

    @Override
    public Noleggio trovaNoleggioPerId(int idNoleggio) throws SQLException {
        String sql = SELECT_QUERY + " WHERE n.idnoleggio = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idNoleggio);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mappaResultSetInNoleggio(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Noleggio> trovaTuttiNoleggi() throws SQLException {
        List<Noleggio> lista = new ArrayList<>();
        try (Connection conn = ConnessioneDatabase.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(SELECT_QUERY)) {

            while (rs.next()) {
                lista.add(mappaResultSetInNoleggio(rs));
            }
        }
        return lista;
    }

    @Override
    public void aggiornaNoleggio(Noleggio noleggio) throws SQLException {
        String sql = """
            UPDATE NOLEGGIO
            SET dataritiro = ?, datarestituzione = ?, costototale = ?
            WHERE idnoleggio = ?
        """;

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(noleggio.getDataRitiro().getTime()));

            if (noleggio.getDataRestituzione() != null) {
                ps.setDate(2, new java.sql.Date(noleggio.getDataRestituzione().getTime()));
            } else {
                ps.setNull(2, Types.DATE);
            }

            ps.setBigDecimal(3, noleggio.getCostoTot());
            ps.setInt(4, noleggio.getIdNoleggio());

            ps.executeUpdate();
        }
    }

    @Override
    public void eliminaNoleggio(int idNoleggio) throws SQLException {
        String sql = "DELETE FROM NOLEGGIO WHERE idnoleggio = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idNoleggio);
            ps.executeUpdate();
        }
    }

    private Noleggio mappaResultSetInNoleggio(ResultSet rs) throws SQLException {
        Auto auto = new Auto(
                rs.getInt("idauto"),
                rs.getString("targa"),
                rs.getString("modello"),
                Auto.StatoAuto.valueOf(rs.getString("stato_auto").toUpperCase()),
                rs.getBigDecimal("costogiornaliero")
        );

        Cliente cliente = new Cliente(
                rs.getInt("id_utente_finale"),
                "N/A", // Username
                "N/A", // Password (hashata)
                rs.getString("nome"),
                rs.getString("cognome"),
                rs.getString("email"),
                rs.getString("patente"),
                java.math.BigDecimal.ZERO,
                true
        );

        Prenotazione prenotazione = new Prenotazione(
                rs.getInt("idprenotazione"),
                rs.getDate("datainizio"),
                rs.getDate("datafine"),
                Prenotazione.StatoPren.valueOf(rs.getString("stato_pren").toUpperCase()),
                cliente,
                auto
        );

        Noleggio n = new Noleggio(
                rs.getInt("idnoleggio"),
                rs.getDate("dataritiro"),
                prenotazione
        );

        n.setDataRestituzione(rs.getDate("datarestituzione"));
        n.setCostoTot(rs.getBigDecimal("costototale"));

        return n;
    }
}