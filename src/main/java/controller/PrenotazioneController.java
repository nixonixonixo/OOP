package controller;

import model.*;
import dao.*;

import java.sql.SQLException;
import java.util.Date;

public class PrenotazioneController {

    private PrenotazioneDAO prenotazioneDAO;
    private AutoDAO autoDAO;

    public PrenotazioneController(PrenotazioneDAO prenotazioneDAO, AutoDAO autoDAO) {
        this.prenotazioneDAO = prenotazioneDAO;
        this.autoDAO = autoDAO;
    }

    public Prenotazione creaPrenotazione(int idPren, Cliente cliente, int idAuto, Date inizio, Date fine) throws SQLException {
        Auto auto = autoDAO.trovaAutoPerId(idAuto);
        if (auto == null || !auto.isDisponibile()) {
            throw new IllegalArgumentException("Auto non disponibile");
        }

        Prenotazione pEsistente = prenotazioneDAO.trovaPrenotazionePerAuto(idAuto);
        Prenotazione nuova = new Prenotazione(idPren, inizio, fine, Prenotazione.StatoPren.IN_ATTESA, cliente, auto);

        if (pEsistente != null) {
            if (pEsistente.isSovrapposta(nuova)) {
                throw new IllegalArgumentException("L'auto è già impegnata per queste date");
            }
        }

        prenotazioneDAO.salvaPrenotazione(nuova);
        return nuova;
    }

    public void confermaPrenotazione(Prenotazione p) throws SQLException {
        p.setStato(Prenotazione.StatoPren.CONFERMATA);
        p.getAuto().cambiaStato(Auto.StatoAuto.PRENOTATA);
        prenotazioneDAO.aggiornaPrenotazione(p);
        autoDAO.aggiornaStatoAuto(p.getAuto().getIdAuto(), Auto.StatoAuto.PRENOTATA);
    }

    public void annullaPrenotazione(Prenotazione p) throws SQLException {
        p.setStato(Prenotazione.StatoPren.ANNULLATA);
        p.getAuto().cambiaStato(Auto.StatoAuto.DISPONIBILE);
        prenotazioneDAO.aggiornaPrenotazione(p);
        autoDAO.aggiornaStatoAuto(p.getAuto().getIdAuto(), Auto.StatoAuto.DISPONIBILE);
    }
}