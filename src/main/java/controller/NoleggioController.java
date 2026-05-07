package controller;

import model.*;
import dao.*;

import java.sql.SQLException;
import java.util.Date;
import java.math.BigDecimal;

public class NoleggioController {

    private NoleggioDAO noleggioDAO;

    public NoleggioController(NoleggioDAO noleggioDAO) {
        this.noleggioDAO = noleggioDAO;
    }

    public Noleggio avviaNoleggio(Prenotazione p) throws SQLException {

        if (p.getStato() != Prenotazione.StatoPren.CONFERMATA) {
            throw new IllegalStateException("Prenotazione non confermata");
        }

        Noleggio n = new Noleggio(0, new Date(), p);
        p.getAuto().cambiaStato(Auto.StatoAuto.NOLEGGIATA);

        noleggioDAO.salvaNoleggio(n);
        return n;
    }

    public void chiudiNoleggio(Noleggio n, Date restituzione) throws SQLException {

        Auto auto = n.getPrenotazione().getAuto();
        BigDecimal costoGiornaliero = auto.getCostoDaily();

        n.chiudiNoleggio(restituzione, costoGiornaliero);

        auto.cambiaStato(Auto.StatoAuto.DISPONIBILE);

        noleggioDAO.aggiornaNoleggio(n);
    }
}