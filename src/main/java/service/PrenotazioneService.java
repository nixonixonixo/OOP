package service;

import dao.PrenotazioneDAO;
import dao.AutoDAO;
import model.Prenotazione;
import model.Auto;

import java.sql.SQLException;
import java.util.List;

public class PrenotazioneService {

    private final PrenotazioneDAO prenotazioneDAO;
    private final AutoDAO autoDAO;

    public PrenotazioneService(PrenotazioneDAO prenotazioneDAO, AutoDAO autoDAO) {
        this.prenotazioneDAO = prenotazioneDAO;
        this.autoDAO = autoDAO;
    }

    public List<Prenotazione> getPrenotazioniCliente(int idCliente) throws SQLException {
        return prenotazioneDAO.trovaPrenotazioniCliente(idCliente);
    }

    public List<Prenotazione> getTuttePrenotazioni() throws SQLException {
        return prenotazioneDAO.trovaTuttePrenotazioni();
    }

    public Prenotazione creaPrenotazione(Prenotazione p) throws SQLException {
        prenotazioneDAO.salvaPrenotazione(p);
        return p;
    }

    public void confermaPrenotazione(int idPren) throws SQLException {
        prenotazioneDAO.aggiornaStatoPrenotazione(idPren, Prenotazione.StatoPren.CONFERMATA);
    }

    public void annullaPrenotazione(int idPren) throws SQLException {
        prenotazioneDAO.aggiornaStatoPrenotazione(idPren, Prenotazione.StatoPren.ANNULLATA);
    }

    public void aggiornaAuto(Prenotazione p, Auto.StatoAuto stato) throws SQLException {
        autoDAO.aggiornaStatoAuto(p.getAuto().getIdAuto(), stato);
    }
}