package service;

import dao.NoleggioDAO;
import dao.AutoDAO;
import model.Noleggio;
import model.Prenotazione;
import model.Auto;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;

public class NoleggioService {

    private final NoleggioDAO noleggioDAO;
    private final AutoDAO autoDAO;

    public NoleggioService(NoleggioDAO noleggioDAO, AutoDAO autoDAO) {
        this.noleggioDAO = noleggioDAO;
        this.autoDAO = autoDAO;
    }

    public Noleggio avviaNoleggio(Prenotazione p) throws SQLException {

        if (p.getStato() != Prenotazione.StatoPren.CONFERMATA) {
            throw new IllegalStateException("Prenotazione non confermata");
        }

        Noleggio n = new Noleggio(0, new Date(), p);

        autoDAO.aggiornaStatoAuto(p.getAuto().getIdAuto(), Auto.StatoAuto.NOLEGGIATA);

        noleggioDAO.salvaNoleggio(n);

        return n;
    }

    public void chiudiNoleggio(Noleggio n, Date restituzione) throws SQLException {

        Auto auto = n.getPrenotazione().getAuto();
        BigDecimal costo = auto.getCostoDaily();

        n.chiudiNoleggio(restituzione, costo);

        autoDAO.aggiornaStatoAuto(auto.getIdAuto(), Auto.StatoAuto.DISPONIBILE);

        noleggioDAO.aggiornaNoleggio(n);
    }
}