package service;

import dao.PrenotazioneDAO;
import dao.NoleggioDAO;
import dao.AutoDAO;
import model.Prenotazione;
import model.Noleggio;
import model.Auto;

import java.sql.SQLException;
import java.util.List;
import java.util.Date;

public class PrenotazioneService {

    private final PrenotazioneDAO prenotazioneDAO;
    private final NoleggioDAO noleggioDAO;
    private final AutoDAO autoDAO;

    public PrenotazioneService(PrenotazioneDAO prenotazioneDAO, NoleggioDAO noleggioDAO, AutoDAO autoDAO) {
        this.prenotazioneDAO = prenotazioneDAO;
        this.noleggioDAO = noleggioDAO;
        this.autoDAO = autoDAO;
    }

    public void effettuaPrenotazione(Prenotazione p) throws SQLException {
        prenotazioneDAO.salvaPrenotazione(p);

        autoDAO.aggiornaStatoAuto(p.getAuto().getIdAuto(), Auto.StatoAuto.NOLEGGIATA);
    }

    public List<Prenotazione> getTuttePrenotazioni() throws SQLException {
        return prenotazioneDAO.trovaTuttePrenotazioni();
    }

    public List<Prenotazione> getPrenotazioniCliente(int idCliente) throws SQLException {
        return prenotazioneDAO.trovaPrenotazioniCliente(idCliente);
    }

    public void confermaPrenotazione(int idPrenotazione) throws Exception {
        Prenotazione p = prenotazioneDAO.trovaPrenotazionePerId(idPrenotazione);
        if (p == null) throw new Exception("Prenotazione non trovata");

        prenotazioneDAO.aggiornaStatoPrenotazione(idPrenotazione, Prenotazione.StatoPren.CONFERMATA);

        Noleggio n = new Noleggio(0, new Date(), p);
        noleggioDAO.salvaNoleggio(n);
    }

    public void annullaPrenotazione(int idPrenotazione) throws Exception {
        Prenotazione p = prenotazioneDAO.trovaPrenotazionePerId(idPrenotazione);
        if (p == null) throw new Exception("Prenotazione non trovata");

        prenotazioneDAO.aggiornaStatoPrenotazione(idPrenotazione, Prenotazione.StatoPren.ANNULLATA);

        autoDAO.aggiornaStatoAuto(p.getAuto().getIdAuto(), Auto.StatoAuto.DISPONIBILE);
    }
}