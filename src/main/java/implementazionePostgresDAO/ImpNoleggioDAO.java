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
    public void salvaNoleggio(Noleggio noleggio)
            throws SQLException {

        String sql = """
                INSERT INTO NOLEGGIO
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection conn =
                        ConnessioneDatabase.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(1, noleggio.getIdNoleggio());

            ps.setDate(
                    2,
                    new java.sql.Date(
                            noleggio.getDataRitiro().getTime()
                    )
            );

            if (noleggio.getDataRestituzione() != null) {

                ps.setDate(
                        3,
                        new java.sql.Date(
                                noleggio.getDataRestituzione().getTime()
                        )
                );

            } else {

                ps.setNull(3, Types.DATE);
            }

            ps.setBigDecimal(4, noleggio.getCostoTot());

            ps.setInt(
                    5,
                    noleggio.getPrenotazione()
                            .getIdPrenotazione()
            );

            ps.executeUpdate();
        }
    }

    @Override
    public Noleggio trovaNoleggioPerId(int idNoleggio)
            throws SQLException {

        String sql = """
                SELECT *
                FROM NOLEGGIO
                WHERE idNoleggio = ?
                """;

        try (
                Connection conn =
                        ConnessioneDatabase.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idNoleggio);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Prenotazione prenotazione = new Prenotazione(
                        rs.getInt("idPrenotazione"),
                        new java.util.Date(),
                        new java.util.Date(),
                        Prenotazione.StatoPren.CONFERMATA,
                        null,
                        null
                );

                Noleggio noleggio =
                        new Noleggio(
                                rs.getInt("idNoleggio"),
                                rs.getDate("dataRitiro"),
                                prenotazione
                        );

                return noleggio;
            }
        }

        return null;
    }

    @Override
    public List<Noleggio> trovaTuttiNoleggi()
            throws SQLException {

        List<Noleggio> noleggi =
                new ArrayList<>();

        String sql = "SELECT * FROM NOLEGGIO";

        try (
                Connection conn =
                        ConnessioneDatabase.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Prenotazione prenotazione = new Prenotazione(
                        rs.getInt("idPrenotazione"),
                        new java.util.Date(),
                        new java.util.Date(),
                        Prenotazione.StatoPren.CONFERMATA,
                        null,
                        null
                );

                Noleggio noleggio =
                        new Noleggio(
                                rs.getInt("idNoleggio"),
                                rs.getDate("dataRitiro"),
                                prenotazione
                        );

                noleggi.add(noleggio);
            }
        }

        return noleggi;
    }

    @Override
    public void aggiornaNoleggio(Noleggio noleggio)
            throws SQLException {

        String sql = """
                UPDATE NOLEGGIO
                SET dataRitiro = ?,
                    dataRestituzione = ?,
                    costoTotale = ?
                WHERE idNoleggio = ?
                """;

        try (
                Connection conn =
                        ConnessioneDatabase.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setDate(
                    1,
                    new java.sql.Date(
                            noleggio.getDataRitiro().getTime()
                    )
            );

            if (noleggio.getDataRestituzione() != null) {

                ps.setDate(
                        2,
                        new java.sql.Date(
                                noleggio.getDataRestituzione().getTime()
                        )
                );

            } else {

                ps.setNull(2, Types.DATE);
            }

            ps.setBigDecimal(
                    3,
                    noleggio.getCostoTot()
            );

            ps.setInt(
                    4,
                    noleggio.getIdNoleggio()
            );

            ps.executeUpdate();
        }
    }

    @Override
    public void eliminaNoleggio(int idNoleggio)
            throws SQLException {

        String sql =
                "DELETE FROM NOLEGGIO WHERE idNoleggio = ?";

        try (
                Connection conn =
                        ConnessioneDatabase.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idNoleggio);

            ps.executeUpdate();
        }
    }
}