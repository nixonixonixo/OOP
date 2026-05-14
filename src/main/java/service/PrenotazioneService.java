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

    // I Field della classe (necessari per far comunicare le diverse parti del sistema)
    private final PrenotazioneDAO prenotazioneDAO;
    private final NoleggioDAO noleggioDAO;
    private final AutoDAO autoDAO;

    // Il costruttore a 3 argomenti che ora combacia con la MainGUI
    public PrenotazioneService(PrenotazioneDAO prenotazioneDAO, NoleggioDAO noleggioDAO, AutoDAO autoDAO) {
        this.prenotazioneDAO = prenotazioneDAO;
        this.noleggioDAO = noleggioDAO;
        this.autoDAO = autoDAO;
    }

    /**
     * IL METODO CHE MANCAVA: Salva la prenotazione e mette l'auto in stato OCCUPATA
     */
    public void effettuaPrenotazione(Prenotazione p) throws SQLException {
        // 1. Salviamo la prenotazione nel DB
        prenotazioneDAO.salvaPrenotazione(p);

        // 2. Cambiamo lo stato dell'auto in OCCUPATA per non farla sparire dal catalogo
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

        // Aggiorna lo stato
        prenotazioneDAO.aggiornaStatoPrenotazione(idPrenotazione, Prenotazione.StatoPren.CONFERMATA);

        // Crea il noleggio automaticamente
        Noleggio n = new Noleggio(0, new Date(), p);
        noleggioDAO.salvaNoleggio(n);
    }

    public void annullaPrenotazione(int idPrenotazione) throws Exception {
        Prenotazione p = prenotazioneDAO.trovaPrenotazionePerId(idPrenotazione);
        if (p == null) throw new Exception("Prenotazione non trovata");

        // Cambia lo stato in Annullata
        prenotazioneDAO.aggiornaStatoPrenotazione(idPrenotazione, Prenotazione.StatoPren.ANNULLATA);

        // LIBERA L'AUTO (fondamentale per rimetterla sul mercato)
        autoDAO.aggiornaStatoAuto(p.getAuto().getIdAuto(), Auto.StatoAuto.DISPONIBILE);
    }
}