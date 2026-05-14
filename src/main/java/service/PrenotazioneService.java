package service;

import dao.AutoDAO;
import dao.PrenotazioneDAO;
import model.Auto;
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


    public void effettuaPrenotazione(Prenotazione p) throws Exception {
        if (p == null || p.getCliente() == null || p.getAuto() == null) {
            throw new Exception("Dati incompleti");
        }


        prenotazioneDAO.salvaPrenotazione(p);


        autoDAO.aggiornaStatoAuto(p.getAuto().getIdAuto(), Auto.StatoAuto.NOLEGGIATA);
    }

    public void confermaPrenotazione(int id) throws Exception {
        prenotazioneDAO.aggiornaStatoPrenotazione(id, Prenotazione.StatoPren.CONFERMATA);
    }


    public void annullaPrenotazione(int id) throws Exception {

        Prenotazione p = prenotazioneDAO.trovaPrenotazionePerId(id);

        prenotazioneDAO.aggiornaStatoPrenotazione(id, Prenotazione.StatoPren.ANNULLATA);

        if (p != null && p.getAuto() != null) {
            autoDAO.aggiornaStatoAuto(p.getAuto().getIdAuto(), Auto.StatoAuto.DISPONIBILE);
        }
    }

    public List<Prenotazione> getPrenotazioniCliente(int idCliente) throws Exception {
        return prenotazioneDAO.trovaPrenotazioniCliente(idCliente);
    }

    public List<Prenotazione> getTuttePrenotazioni() throws Exception {
        return prenotazioneDAO.trovaTuttePrenotazioni();
    }
}