package implementazionePostgresDAO;

import dao.NoleggioDAO;
import database.ConnessioneDatabase;
import model.Noleggio;
import model.Prenotazione;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImpNoleggioDAO implements NoleggioDAO {

    @Override
    public void salvaNoleggio(Noleggio noleggio) throws SQLException {
        String sql = "INSERT INTO NOLEGGIO (idnoleggio, dataritiro, datarestituzione, costototale, idprenotazione) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, noleggio.getIdNoleggio());
            ps.setDate(2, new java.sql.Date(noleggio.getDataRitiro().getTime()));

            if (noleggio.getDataRestituzione() != null) {
                ps.setDate(3, new java.sql.Date(noleggio.getDataRestituzione().getTime()));
            } else {
                ps.setNull(3, Types.DATE);
            }

            ps.setBigDecimal(4, noleggio.getCostoTot());
            ps.setInt(5, noleggio.getPrenotazione().getIdPrenotazione());

            ps.executeUpdate();
        }
    }

    @Override
    public Noleggio trovaNoleggioPerId(int idNoleggio) throws SQLException {
        String sql = "SELECT * FROM NOLEGGIO WHERE idnoleggio = ?";

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
        String sql = "SELECT * FROM NOLEGGIO";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
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
        int idPren = rs.getInt("idprenotazione");

        model.Cliente clienteDummy = new model.Cliente(0, "Utente", "", "", "", "", "", java.math.BigDecimal.ZERO);
        model.Auto autoDummy = new model.Auto(0, "TARGA", "MODELLO", model.Auto.StatoAuto.DISPONIBILE, java.math.BigDecimal.ZERO);

        Prenotazione prenotazione = new Prenotazione(
                idPren,
                new java.util.Date(),
                new java.util.Date(),
                Prenotazione.StatoPren.CONFERMATA,
                clienteDummy,
                autoDummy
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