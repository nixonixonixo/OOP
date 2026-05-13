package service;

import dao.AutoDAO;
import model.Auto;

import java.sql.SQLException;
import java.util.List;

public class AutoService {

    private final AutoDAO autoDAO;

    public AutoService(AutoDAO autoDAO) {
        this.autoDAO = autoDAO;
    }

    public List<Auto> getTutte() throws SQLException {
        return autoDAO.trovaTutteAuto();
    }

    public List<Auto> getAutoDisponibili() throws SQLException {
        return autoDAO.trovaAutoDisponibili();
    }

    public void cambiaStato(int idAuto, Auto.StatoAuto stato) throws SQLException {
        autoDAO.aggiornaStatoAuto(idAuto, stato);
    }
}