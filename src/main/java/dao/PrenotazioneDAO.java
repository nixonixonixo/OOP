package dao;

import model.Prenotazione;

import java.sql.SQLException;
import java.util.List;

public interface PrenotazioneDAO {

    void salvaPrenotazione(Prenotazione prenotazione)
            throws SQLException;

    Prenotazione trovaPrenotazionePerId(int idPrenotazione)
            throws SQLException;

    List<Prenotazione> trovaPrenotazioniCliente(int idCliente)
            throws SQLException;

    List<Prenotazione> trovaTuttePrenotazioni()
            throws SQLException;

    void aggiornaPrenotazione(Prenotazione prenotazione)
            throws SQLException;

    void eliminaPrenotazione(int idPrenotazione)
            throws SQLException;
}