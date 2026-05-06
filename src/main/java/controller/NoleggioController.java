package controller;

import model.*;
import dao.*;
import java.util.Date;
import java.math.BigDecimal;

public class NoleggioController {

    private NoleggioDAO noleggioDAO;

    public NoleggioController(NoleggioDAO noleggioDAO) {
        this.noleggioDAO = noleggioDAO;
    }

    public Noleggio avviaNoleggio(Prenotazione p) {

        if (p.getStato() != Prenotazione.StatoPren.CONFERMATA) {
            throw new IllegalStateException("Prenotazione non confermata");
        }

        Noleggio n = new Noleggio(0, new Date(), p);
        p.getAuto().cambiaStato(Auto.StatoAuto.NOLEGGIATA);

        noleggioDAO.save(n);
        return n;
    }

    public void chiudiNoleggio(Noleggio n, Date restituzione) {

        Auto auto = n.getPrenotazione().getAuto();
        BigDecimal costoGiornaliero = auto.getCostoGiornaliero();

        n.chiudiNoleggio(restituzione, costoGiornaliero);

        auto.cambiaStato(Auto.StatoAuto.DISPONIBILE);

        noleggioDAO.update(n);
    }
}