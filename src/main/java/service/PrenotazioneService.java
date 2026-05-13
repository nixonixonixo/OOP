package service;

import dao.AutoDAO;
import dao.PrenotazioneDAO;
import model.Auto;
import model.Cliente;
import model.Prenotazione;

import java.sql.SQLException;
import java.util.Date;

public class PrenotazioneService {

    private final PrenotazioneDAO prenotazioneDAO;
    private final AutoDAO autoDAO;

    public PrenotazioneService(PrenotazioneDAO prenotazioneDAO, AutoDAO autoDAO) {
        this.prenotazioneDAO = prenotazioneDAO;
        this.autoDAO = autoDAO;
    }

    public Prenotazione creaPrenotazione(int idPren, Cliente cliente, int idAuto, Date inizio, Date fine)
            throws SQLException {

        Auto auto = autoDAO.trovaAutoPerId(idAuto);

        if (auto == null || auto.getStato() != Auto.StatoAuto.DISPONIBILE) {
            throw new IllegalStateException("Auto non disponibile");
        }

        Prenotazione nuova = new Prenotazione(
                idPren,
                inizio,
                fine,
                Prenotazione.StatoPren.IN_ATTESA,
                cliente,
                auto
        );

        Prenotazione esistente = prenotazioneDAO.trovaPrenotazionePerAuto(idAuto);

        if (esistente != null && esistente.isSovrapposta(nuova)) {
            throw new IllegalArgumentException("Sovrapposizione prenotazioni");
        }

        prenotazioneDAO.salvaPrenotazione(nuova);
        return nuova;
    }

    public void confermaPrenotazione(int idPrenotazione) throws SQLException {

        Prenotazione p = prenotazioneDAO.trovaPrenotazionePerId(idPrenotazione);

        if (p == null) {
            throw new IllegalArgumentException("Prenotazione non trovata");
        }

        p.setStato(Prenotazione.StatoPren.CONFERMATA);

        autoDAO.aggiornaStatoAuto(
                p.getAuto().getIdAuto(),
                Auto.StatoAuto.NOLEGGIATA
        );

        prenotazioneDAO.aggiornaPrenotazione(p);
    }

    public void annullaPrenotazione(int idPrenotazione) throws SQLException {

        Prenotazione p = prenotazioneDAO.trovaPrenotazionePerId(idPrenotazione);

        if (p == null) {
            throw new IllegalArgumentException("Prenotazione non trovata");
        }

        p.setStato(Prenotazione.StatoPren.ANNULLATA);

        autoDAO.aggiornaStatoAuto(
                p.getAuto().getIdAuto(),
                Auto.StatoAuto.DISPONIBILE
        );

        prenotazioneDAO.aggiornaPrenotazione(p);
    }
}