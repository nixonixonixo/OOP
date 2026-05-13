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

    public List<Auto> getTutteLeAuto() throws SQLException {
        return autoDAO.trovaTutteAuto();
    }

    public List<Auto> getAutoDisponibili() throws SQLException {
        return autoDAO.trovaAutoDisponibili();
    }

    public Auto getAutoById(int idAuto) throws SQLException {
        return autoDAO.trovaAutoPerId(idAuto);
    }

    public void impostaDisponibile(int idAuto) throws SQLException {
        autoDAO.aggiornaStatoAuto(idAuto, Auto.StatoAuto.DISPONIBILE);
    }

    public void impostaNoleggiata(int idAuto) throws SQLException {
        autoDAO.aggiornaStatoAuto(idAuto, Auto.StatoAuto.NOLEGGIATA);
    }

    public void impostaPrenotata(int idAuto) throws SQLException {
        autoDAO.aggiornaStatoAuto(idAuto, Auto.StatoAuto.PRENOTATA);
    }

    public void impostaInManutenzione(int idAuto) throws SQLException {
        autoDAO.aggiornaStatoAuto(idAuto, Auto.StatoAuto.IN_MANUTENZIONE);
    }
}