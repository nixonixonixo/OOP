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

    // Quando un cliente prenota, l'auto deve diventare "PRENOTATA"
    public void effettuaPrenotazione(Prenotazione p) throws Exception {
        if (p == null || p.getCliente() == null || p.getAuto() == null) {
            throw new Exception("Dati incompleti");
        }

        // 1. Salva la prenotazione nel DB
        prenotazioneDAO.salvaPrenotazione(p);

        // 2. Cambia lo stato dell'auto in 'PRENOTATA' o 'OCCUPATA'
        // così sparisce dalla lista delle auto disponibili
        autoDAO.aggiornaStatoAuto(p.getAuto().getIdAuto(), Auto.StatoAuto.NOLEGGIATA);
    }

    // Quando l'operatore conferma la prenotazione
    public void confermaPrenotazione(int id) throws Exception {
        prenotazioneDAO.aggiornaStatoPrenotazione(id, Prenotazione.StatoPren.CONFERMATA);
    }

    // Se l'operatore annulla, l'auto deve tornare "DISPONIBILE"
    public void annullaPrenotazione(int id) throws Exception {
        // Recuperiamo la prenotazione per sapere quale auto liberare
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