package implementazionePostgresDAO;

import dao.AutoDAO;
import database.ConnessioneDatabase;
import model.Auto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImpAutoDAO implements AutoDAO {

    @Override
    public void salvaAuto(Auto auto) throws SQLException {
        // Usiamo i nomi delle colonne definiti nel tuo schema logico
        String sql = "INSERT INTO AUTO (id_auto, targa, modello, stato, costo_daily) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, auto.getIdAuto());
            ps.setString(2, auto.getTarga());
            ps.setString(3, auto.getModello());
            ps.setString(4, auto.getStato().toString()); // Converte Enum in Stringa
            ps.setBigDecimal(5, auto.getCostoDaily());

            ps.executeUpdate();
        }
    }

    @Override
    public Auto trovaAutoPerId(int idAuto) throws SQLException {
        String sql = "SELECT * FROM AUTO WHERE id_auto = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idAuto);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mappaResultSetInAuto(rs);
            }
        }
        return null;
    }

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

    @Override
    public List<Auto> trovaAutoDisponibili() throws SQLException {
        List<Auto> lista = new ArrayList<>();
        // Filtriamo per lo stato 'DISPONIBILE' come richiesto dalle regole di business
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

    @Override
    public void aggiornaAuto(Auto auto) throws SQLException {
        String sql = "UPDATE AUTO SET targa = ?, modello = ?, stato = ?, costo_daily = ? WHERE id_auto = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, auto.getTarga());
            ps.setString(2, auto.getModello());
            ps.setString(3, auto.getStato().toString());
            ps.setBigDecimal(4, auto.getCostoDaily());
            ps.setInt(5, auto.getIdAuto());

            ps.executeUpdate();
        }
    }

    @Override
    public void aggiornaStatoAuto(int idAuto, Auto.StatoAuto stato) throws SQLException {
        String sql = "UPDATE AUTO SET stato = ? WHERE id_auto = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, stato.toString());
            ps.setInt(2, idAuto);

            ps.executeUpdate();
        }
    }

    @Override
    public void eliminaAuto(int idAuto) throws SQLException {
        String sql = "DELETE FROM AUTO WHERE id_auto = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idAuto);
            ps.executeUpdate();
        }
    }

    private Auto mappaResultSetInAuto(ResultSet rs) throws SQLException {
        return new Auto(
                rs.getInt("id_auto"),
                rs.getString("targa"),
                rs.getString("modello"),
                Auto.StatoAuto.valueOf(rs.getString("stato")),
                rs.getBigDecimal("costo_daily")
        );
    }
}