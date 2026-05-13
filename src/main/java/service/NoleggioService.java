package service;

import dao.NoleggioDAO;
import dao.PrenotazioneDAO;
import dao.AutoDAO;
import model.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class NoleggioService {

    private final NoleggioDAO noleggioDAO;
    private final PrenotazioneDAO prenotazioneDAO;
    private final AutoDAO autoDAO;

    public NoleggioService(
            NoleggioDAO noleggioDAO,
            PrenotazioneDAO prenotazioneDAO,
            AutoDAO autoDAO
    ) {
        this.noleggioDAO = noleggioDAO;
        this.prenotazioneDAO = prenotazioneDAO;
        this.autoDAO = autoDAO;
    }

    /**
     * Avvia il noleggio da una prenotazione confermata
     */
    public Noleggio avviaNoleggio(int idPrenotazione) throws SQLException {

        Prenotazione p = prenotazioneDAO.trovaPrenotazionePerId(idPrenotazione);

        if (p == null) {
            throw new IllegalArgumentException("Prenotazione non trovata");
        }

        if (p.getStato() != Prenotazione.StatoPren.CONFERMATA) {
            throw new IllegalStateException("Prenotazione non confermata");
        }

        Auto auto = p.getAuto();

        if (!auto.isDisponibile()) {
            throw new IllegalStateException("Auto non disponibile per noleggio");
        }

        // crea noleggio
        Noleggio n = new Noleggio(
                0,
                new Date(),
                p
        );

        // aggiorna stato auto
        autoDAO.aggiornaStatoAuto(
                auto.getIdAuto(),
                Auto.StatoAuto.NOLEGGIATA
        );

        noleggioDAO.salvaNoleggio(n);

        return n;
    }

    /**
     * Chiude il noleggio e calcola il costo finale
     */
    public Noleggio chiudiNoleggio(int idNoleggio, Date dataRestituzione) throws SQLException {

        Noleggio n = noleggioDAO.trovaNoleggioPerId(idNoleggio);

        if (n == null) {
            throw new IllegalArgumentException("Noleggio non trovato");
        }

        if (n.getDataRestituzione() != null) {
            throw new IllegalStateException("Noleggio già chiuso");
        }

        long diffMs = Math.abs(dataRestituzione.getTime() - n.getDataRitiro().getTime());
        long giorni = TimeUnit.DAYS.convert(diffMs, TimeUnit.MILLISECONDS);

        if (giorni <= 0) giorni = 1;

        BigDecimal tariffa = n.getPrenotazione()
                .getAuto()
                .getCostoDaily();

        BigDecimal costoTotale = tariffa.multiply(BigDecimal.valueOf(giorni));

        n.setDataRestituzione(dataRestituzione);
        n.setCostoTot(costoTotale);

        // auto torna disponibile
        autoDAO.aggiornaStatoAuto(
                n.getPrenotazione().getAuto().getIdAuto(),
                Auto.StatoAuto.DISPONIBILE
        );

        noleggioDAO.aggiornaNoleggio(n);

        return n;
    }
}