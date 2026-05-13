package service;

import dao.NoleggioDAO;
import dao.PrenotazioneDAO;
import model.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class NoleggioService {

    private final NoleggioDAO noleggioDAO;
    private final PrenotazioneDAO prenotazioneDAO;

    public NoleggioService(NoleggioDAO noleggioDAO, PrenotazioneDAO prenotazioneDAO) {
        this.noleggioDAO = noleggioDAO;
        this.prenotazioneDAO = prenotazioneDAO;
    }

    public Noleggio avviaNoleggio(int idPrenotazione) throws SQLException {

        Prenotazione p = prenotazioneDAO.trovaPrenotazionePerId(idPrenotazione);

        if (p == null || p.getStato() != Prenotazione.StatoPren.CONFERMATA) {
            throw new IllegalStateException("Prenotazione non valida");
        }

        Noleggio n = new Noleggio(
                0,
                new Date(),
                p
        );

        noleggioDAO.salvaNoleggio(n);

        return n;
    }

    public Noleggio chiudiNoleggio(int idNoleggio) throws SQLException {

        Noleggio n = noleggioDAO.trovaNoleggioPerId(idNoleggio);

        if (n == null) {
            throw new IllegalArgumentException("Noleggio non trovato");
        }

        Date oggi = new Date();

        long giorni = TimeUnit.DAYS.convert(
                Math.abs(oggi.getTime() - n.getDataRitiro().getTime()),
                TimeUnit.MILLISECONDS
        );

        if (giorni <= 0) giorni = 1;

        BigDecimal costo = n.getPrenotazione()
                .getAuto()
                .getCostoDaily()
                .multiply(BigDecimal.valueOf(giorni));

        n.chiudiNoleggio(oggi, costo);

        noleggioDAO.aggiornaNoleggio(n);

        return n;
    }
}