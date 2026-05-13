package implementazionePostgresDAO;

import dao.PrenotazioneDAO;
import database.ConnessioneDatabase;
import model.Auto;
import model.Cliente;
import model.Prenotazione;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImpPrenotazioneDAO implements PrenotazioneDAO {

    @Override
    public void salvaPrenotazione(Prenotazione p) throws SQLException {
        String sql = """
                INSERT INTO PRENOTAZIONE (idprenotazione, datainizio, datafine, stato, idcliente, idauto)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
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
        String sql = """
                SELECT p.*, a.modello, a.targa, a.costogiornaliero, a.stato as stato_auto, 
                       u.nome, u.cognome, u.email, c.patente, c.credito
                FROM PRENOTAZIONE p
                JOIN AUTO a ON p.idauto = a.idauto
                JOIN CLIENTE c ON p.idcliente = c.idutente
                JOIN UTENTE u ON c.idutente = u.idutente
                WHERE p.idprenotazione = ?
                """;
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPrenotazione);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mappaResultSetCompleto(rs);
            }
        }
        return null;
    }

    @Override
    public List<Prenotazione> trovaPrenotazioniCliente(int idCliente) throws SQLException {
        List<Prenotazione> lista = new ArrayList<>();
        String sql = """
                SELECT p.*, a.modello, a.targa, a.costogiornaliero, a.stato as stato_auto, 
                       u.nome, u.cognome, u.email, c.patente, c.credito
                FROM PRENOTAZIONE p
                JOIN AUTO a ON p.idauto = a.idauto
                JOIN CLIENTE c ON p.idcliente = c.idutente
                JOIN UTENTE u ON c.idutente = u.idutente
                WHERE p.idcliente = ?
                """;
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mappaResultSetCompleto(rs));
            }
        }
        return lista;
    }

    @Override
    public List<Prenotazione> trovaTuttePrenotazioni() throws SQLException {
        List<Prenotazione> lista = new ArrayList<>();
        String sql = """
                SELECT p.*, a.modello, a.targa, a.costogiornaliero, a.stato as stato_auto, 
                       u.nome, u.cognome, u.email, c.patente, c.credito
                FROM PRENOTAZIONE p
                JOIN AUTO a ON p.idauto = a.idauto
                JOIN CLIENTE c ON p.idcliente = c.idutente
                JOIN UTENTE u ON c.idutente = u.idutente
                """;
        try (Connection conn = ConnessioneDatabase.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mappaResultSetCompleto(rs));
            }
        }
        return lista;
    }

    @Override
    public void aggiornaPrenotazione(Prenotazione p) throws SQLException {
        String sql = "UPDATE PRENOTAZIONE SET datainizio = ?, datafine = ?, stato = ? WHERE idprenotazione = ?";
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

    @Override
    public Prenotazione trovaPrenotazionePerAuto(int idAuto) throws SQLException {
        String sql = """
                SELECT p.*, a.modello, a.targa, a.costogiornaliero, a.stato as stato_auto, 
                       u.nome, u.cognome, u.email, c.patente, c.credito
                FROM PRENOTAZIONE p
                JOIN AUTO a ON p.idauto = a.idauto
                JOIN CLIENTE c ON p.idcliente = c.idutente
                JOIN UTENTE u ON c.idutente = u.idutente
                WHERE p.idauto = ? AND p.stato != 'ANNULLATA'
                LIMIT 1
                """;
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAuto);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mappaResultSetCompleto(rs);
            }
        }
        return null;
    }

    @Override
    public void aggiornaStatoPrenotazione(int idPrenotazione, Prenotazione.StatoPren nuovoStato) throws SQLException {
        String sql = "UPDATE PRENOTAZIONE SET stato = ? WHERE idprenotazione = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuovoStato.toString());
            ps.setInt(2, idPrenotazione);
            ps.executeUpdate();
        }
    }

    private Prenotazione mappaResultSetCompleto(ResultSet rs) throws SQLException {
        Auto auto = new Auto(
                rs.getInt("idauto"),
                rs.getString("targa"),
                rs.getString("modello"),
                Auto.StatoAuto.valueOf(rs.getString("stato_auto").toUpperCase()),
                rs.getBigDecimal("costogiornaliero")
        );

        Cliente cliente = new Cliente(
                rs.getInt("idcliente"),
                null,
                null,
                rs.getString("nome"),
                rs.getString("cognome"),
                rs.getString("email"),
                rs.getString("patente"),
                rs.getBigDecimal("credito")
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
}