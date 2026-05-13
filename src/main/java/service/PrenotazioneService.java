package service;

import dao.AutoDAO;
import dao.PrenotazioneDAO;
import model.Prenotazione;

import java.sql.SQLException;
import java.util.List;

public class PrenotazioneService {

    private final PrenotazioneDAO prenotazioneDAO;
    private final AutoDAO autoDAO;

    public PrenotazioneService(PrenotazioneDAO prenotazioneDAO, AutoDAO autoDAO) {
        this.prenotazioneDAO = prenotazioneDAO;
        this.autoDAO = autoDAO;
    }

    public List<Prenotazione> getTuttePrenotazioni() throws SQLException {
        return prenotazioneDAO.trovaTuttePrenotazioni();
    }

    public List<Prenotazione> getPrenotazioniCliente(int id) throws SQLException {
        return prenotazioneDAO.trovaPrenotazioniCliente(id);
    }

    public void confermaPrenotazione(int id) throws SQLException {
        prenotazioneDAO.aggiornaStatoPrenotazione(id, Prenotazione.StatoPren.CONFERMATA);
    }

    public void annullaPrenotazione(int id) throws SQLException {
        prenotazioneDAO.aggiornaStatoPrenotazione(id, Prenotazione.StatoPren.ANNULLATA);
    }
}