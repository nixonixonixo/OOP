package implementazionePostgresDAO;

import dao.NoleggioDAO;
import database.ConnessioneDatabase;
import model.Noleggio;
import model.Prenotazione;
import model.Auto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImpNoleggioDAO implements NoleggioDAO {

    @Override
    public void salvaNoleggio(Noleggio noleggio) throws SQLException {
        // FIX: Rimosso idnoleggio per permettere l'autoincremento sul DB
        String sql = """
            INSERT INTO NOLEGGIO (dataritiro, costototale, idprenotazione)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(noleggio.getDataRitiro().getTime()));

            // Gestione costo totale (potrebbe essere null alla creazione)
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
        String sql = """
            SELECT 
                n.idnoleggio, n.dataritiro, n.datarestituzione, n.costototale,
                n.idprenotazione,
                p.datainizio, p.datafine, p.stato AS stato_pren,
                a.idauto, a.targa, a.modello, a.stato AS stato_auto, a.costogiornaliero
            FROM NOLEGGIO n
            JOIN PRENOTAZIONE p ON n.idprenotazione = p.idprenotazione
            JOIN AUTO a ON p.idauto = a.idauto
            WHERE n.idnoleggio = ?
        """;

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
        String sql = """
            SELECT 
                n.idnoleggio, n.dataritiro, n.datarestituzione, n.costototale,
                n.idprenotazione,
                p.datainizio, p.datafine, p.stato AS stato_pren,
                a.idauto, a.targa, a.modello, a.stato AS stato_auto, a.costogiornaliero
            FROM NOLEGGIO n
            JOIN PRENOTAZIONE p ON n.idprenotazione = p.idprenotazione
            JOIN AUTO a ON p.idauto = a.idauto
        """;

        try (Connection conn = ConnessioneDatabase.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

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
        // Mappatura Auto
        Auto.StatoAuto statoAuto = Auto.StatoAuto.valueOf(rs.getString("stato_auto").toUpperCase());
        Auto auto = new Auto(
                rs.getInt("idauto"),
                rs.getString("targa"),
                rs.getString("modello"),
                statoAuto,
                rs.getBigDecimal("costogiornaliero")
        );

        // Mappatura Prenotazione
        Prenotazione.StatoPren statoPren = Prenotazione.StatoPren.valueOf(rs.getString("stato_pren").toUpperCase());
        Prenotazione prenotazione = new Prenotazione(
                rs.getInt("idprenotazione"),
                rs.getDate("datainizio"),
                rs.getDate("datafine"),
                statoPren,
                null, // idcliente può restare null qui o essere mappato se necessario
                auto
        );

        // Mappatura Noleggio
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