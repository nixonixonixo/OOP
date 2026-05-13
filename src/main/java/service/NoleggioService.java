package service;

import dao.AutoDAO;
import dao.NoleggioDAO;
import model.Noleggio;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class NoleggioService {

    private final NoleggioDAO noleggioDAO;
    private final AutoDAO autoDAO;

    public NoleggioService(NoleggioDAO noleggioDAO, AutoDAO autoDAO) {
        this.noleggioDAO = noleggioDAO;
        this.autoDAO = autoDAO;
    }

    public List<Noleggio> getTuttiNoleggi() throws SQLException {
        return noleggioDAO.trovaTuttiNoleggi();
    }

    public void chiudiNoleggio(int idNoleggio, Date dataRestituzione) throws SQLException {
        Noleggio n = noleggioDAO.trovaNoleggioPerId(idNoleggio);

        n.setDataRestituzione(dataRestituzione);
        noleggioDAO.aggiornaNoleggio(n);
    }
}