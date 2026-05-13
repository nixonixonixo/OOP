package implementazionePostgresDAO;

import dao.PrenotazioneDAO;
import database.ConnessioneDatabase;
import model.Prenotazione;
import model.Cliente;
import model.Auto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImpPrenotazioneDAO implements PrenotazioneDAO {

    @Override
    public void salvaPrenotazione(Prenotazione p) throws SQLException {
        String sql = "INSERT INTO PRENOTAZIONE (idprenotazione, data_inizio, data_fine, stato, idutente, idauto) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, p.getIdPrenotazione());
            ps.setDate(2, new java.sql.Date(p.getDataInizio().getTime()));
            ps.setDate(3, new java.sql.Date(p.getDataFine().getTime()));
            ps.setString(4, p.getStato().toString());
            ps.setInt(5, p.getCliente().getIdUtente());
            ps.setInt(6, p.getAuto().getIdAuto());

            ps.executeUpdate();
        }
    }

    @Override
    public Prenotazione trovaPrenotazionePerId(int idPrenotazione) throws SQLException {
        String sql = "SELECT * FROM PRENOTAZIONE WHERE idprenotazione = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPrenotazione);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mappaResultSetInPrenotazione(rs);
            }
        }
        return null;
    }

    @Override
    public List<Prenotazione> trovaPrenotazioniCliente(int idCliente) throws SQLException {
        List<Prenotazione> lista = new ArrayList<>();
        // Chiave esterna verso cliente è idutente
        String sql = "SELECT * FROM PRENOTAZIONE WHERE idutente = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mappaResultSetInPrenotazione(rs));
            }
        }
        return lista;
    }

    @Override
    public List<Prenotazione> trovaTuttePrenotazioni() throws SQLException {
        List<Prenotazione> lista = new ArrayList<>();
        String sql = "SELECT * FROM PRENOTAZIONE";

        try (Connection conn = ConnessioneDatabase.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mappaResultSetInPrenotazione(rs));
            }
        }
        return lista;
    }

    @Override
    public void aggiornaPrenotazione(Prenotazione p) throws SQLException {
        String sql = "UPDATE PRENOTAZIONE SET data_inizio = ?, data_fine = ?, stato = ? WHERE idprenotazione = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(p.getDataInizio().getTime()));
            ps.setDate(2, new java.sql.Date(p.getDataFine().getTime()));
            ps.setString(3, p.getStato().toString());
            ps.setInt(4, p.getIdPrenotazione());

            ps.executeUpdate();
        }
    }

    @Override
    public void eliminaPrenotazione(int idPrenotazione) throws SQLException {
        String sql = "DELETE FROM PRENOTAZIONE WHERE idprenotazione = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPrenotazione);
            ps.executeUpdate();
        }
    }

    private Prenotazione mappaResultSetInPrenotazione(ResultSet rs) throws SQLException {
        int idUtenteRecuperato = rs.getInt("idcliente");

        Cliente cliente = new Cliente(
                idUtenteRecuperato,
                "placeholder",
                "placeholder",
                "placeholder",
                "placeholder",
                "placeholder",
                "SCONOSCIUTA",
                java.math.BigDecimal.ZERO
        );

        Auto auto = new Auto(
                rs.getInt("idauto"),
                "PROVVISORIA",
                "MOD_GENERICO",
                Auto.StatoAuto.DISPONIBILE,
                java.math.BigDecimal.ZERO
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

    @Override
    public Prenotazione trovaPrenotazionePerAuto(int idAuto) throws SQLException {
        String sql = "SELECT * FROM PRENOTAZIONE WHERE idauto = ? AND stato != 'ANNULLATA' LIMIT 1";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAuto);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mappaResultSetInPrenotazione(rs);
            }
        }
        return null;
    }
}