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
        String sql = "INSERT INTO NOLEGGIO (idnoleggio, dataritiro, costototale, idprenotazione) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, noleggio.getIdNoleggio());
            ps.setDate(2, new java.sql.Date(noleggio.getDataRitiro().getTime()));
            ps.setBigDecimal(3, noleggio.getCostoTot());
            ps.setInt(4, noleggio.getPrenotazione().getIdPrenotazione());

            ps.executeUpdate();
        }
    }

    @Override
    public Noleggio trovaNoleggioPerId(int idNoleggio) throws SQLException {
        String sql = """
            SELECT n.idnoleggio, n.dataritiro, n.datarestituzione, n.costototale, n.idprenotazione,
                   p.datainizio, p.datafine, p.stato as stato_pren,
                   a.idauto, a.targa, a.modello, a.stato as stato_auto, a.costogiornaliero
            FROM NOLEGGIO n
            JOIN PRENOTAZIONE p ON n.idprenotazione = p.idprenotazione
            JOIN AUTO a ON p.idauto = a.idauto
            WHERE n.idnoleggio = ?
            """;

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idNoleggio);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mappaResultSetInNoleggio(rs);
            }
        }
        return null;
    }

    @Override
    public List<Noleggio> trovaTuttiNoleggi() throws SQLException {
        List<Noleggio> noleggi = new ArrayList<>();
        String sql = """
            SELECT n.idnoleggio, n.dataritiro, n.datarestituzione, n.costototale, n.idprenotazione,
                   p.datainizio, p.datafine, p.stato as stato_pren,
                   a.idauto, a.targa, a.modello, a.stato as stato_auto, a.costogiornaliero
            FROM NOLEGGIO n
            JOIN PRENOTAZIONE p ON n.idprenotazione = p.idprenotazione
            JOIN AUTO a ON p.idauto = a.idauto
            """;

        try (Connection conn = ConnessioneDatabase.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                noleggi.add(mappaResultSetInNoleggio(rs));
            }
        }
        return noleggi;
    }

    @Override
    public void aggiornaNoleggio(Noleggio noleggio) throws SQLException {
        String sql = "UPDATE NOLEGGIO SET dataritiro = ?, datarestituzione = ?, costototale = ? WHERE idnoleggio = ?";

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
        Auto.StatoAuto statoAuto = Auto.StatoAuto.valueOf(rs.getString("stato_auto").toUpperCase());
        Auto auto = new Auto(
                rs.getInt("idauto"),
                rs.getString("targa"),
                rs.getString("modello"),
                statoAuto,
                rs.getBigDecimal("costogiornaliero")
        );

        Prenotazione.StatoPren statoPren = Prenotazione.StatoPren.valueOf(rs.getString("stato_pren").toUpperCase());
        Prenotazione prenotazione = new Prenotazione(
                rs.getInt("idprenotazione"),
                rs.getDate("datainizio"),
                rs.getDate("datafine"),
                statoPren,
                null,
                auto
        );

        Noleggio noleggio = new Noleggio(
                rs.getInt("idnoleggio"),
                rs.getDate("dataritiro"),
                prenotazione
        );

        noleggio.setDataRestituzione(rs.getDate("datarestituzione"));
        noleggio.setCostoTot(rs.getBigDecimal("costototale"));

        return noleggio;
    }
}