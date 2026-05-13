package service;

import dao.AutoDAO;
import dao.PrenotazioneDAO;
import model.*;

import java.sql.SQLException;
import java.util.Date;

public class PrenotazioneService {

    private final PrenotazioneDAO prenotazioneDAO;
    private final AutoDAO autoDAO;

    public PrenotazioneService(PrenotazioneDAO prenotazioneDAO, AutoDAO autoDAO) {
        this.prenotazioneDAO = prenotazioneDAO;
        this.autoDAO = autoDAO;
    }

    public Prenotazione creaPrenotazione(
            int idPrenotazione,
            Cliente cliente,
            int idAuto,
            Date inizio,
            Date fine
    ) throws SQLException {

        Auto auto = autoDAO.trovaAutoPerId(idAuto);

        if (auto == null) {
            throw new IllegalArgumentException("Auto non trovata");
        }

        if (!auto.isDisponibile()) {
            throw new IllegalStateException("Auto non disponibile");
        }

        Prenotazione nuova = new Prenotazione(
                idPrenotazione,
                inizio,
                fine,
                Prenotazione.StatoPren.IN_ATTESA,
                cliente,
                auto
        );

        Prenotazione esistente = prenotazioneDAO.trovaPrenotazionePerAuto(idAuto);

        if (esistente != null && esistente.isSovrapposta(nuova)) {
            throw new IllegalStateException("Auto già prenotata nel periodo selezionato");
        }

        prenotazioneDAO.salvaPrenotazione(nuova);

        return nuova;
    }

    public void confermaPrenotazione(int idPrenotazione) throws SQLException {

        Prenotazione p = prenotazioneDAO.trovaPrenotazionePerId(idPrenotazione);

        if (p == null) {
            throw new IllegalArgumentException("Prenotazione non trovata");
        }

        if (p.getStato() != Prenotazione.StatoPren.IN_ATTESA) {
            throw new IllegalStateException("Prenotazione non confermabile");
        }

        p.setStato(Prenotazione.StatoPren.CONFERMATA);

        autoDAO.aggiornaStatoAuto(
                p.getAuto().getIdAuto(),
                Auto.StatoAuto.PRENOTATA
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