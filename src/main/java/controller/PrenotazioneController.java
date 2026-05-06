package controller;

import model.*;
import dao.*;
import java.util.Date;
import java.util.List;

public class PrenotazioneController {

    private PrenotazioneDAO prenotazioneDAO;
    private AutoDAO autoDAO;

    public PrenotazioneController(PrenotazioneDAO prenotazioneDAO, AutoDAO autoDAO) {
        this.prenotazioneDAO = prenotazioneDAO;
        this.autoDAO = autoDAO;
    }

    public Prenotazione creaPrenotazione(Cliente cliente, int idAuto, Date inizio, Date fine) {

        Auto auto = autoDAO.findById(idAuto);

        if (auto == null || !auto.isDisponibile()) {
            throw new IllegalArgumentException("Auto non disponibile");
        }

        // controllo sovrapposizioni
        List<Prenotazione> prenotazioni = prenotazioneDAO.findByAuto(idAuto);

        Prenotazione nuova = new Prenotazione(0, cliente, auto, inizio, fine, Prenotazione.StatoPren.IN_ATTESA);

        for (Prenotazione p : prenotazioni) {
            if (p.isSovrapposta(nuova) && p.isValida()) {
                throw new IllegalArgumentException("Date non disponibili");
            }
        }

        prenotazioneDAO.save(nuova);
        return nuova;
    }

    public void confermaPrenotazione(Prenotazione p) {
        p.conferma();
        p.getAuto().cambiaStato(Auto.StatoAuto.PRENOTATA);
        prenotazioneDAO.update(p);
    }

    public void annullaPrenotazione(Prenotazione p) {
        p.annulla();
        p.getAuto().cambiaStato(Auto.StatoAuto.DISPONIBILE);
        prenotazioneDAO.update(p);
    }
}