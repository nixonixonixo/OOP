package service;

import dao.OperatoreDAO;
import model.Operatore;

import java.sql.SQLException;
import java.util.List;

public class OperatoreService {

    private final OperatoreDAO operatoreDAO;

    public OperatoreService(OperatoreDAO operatoreDAO) {
        this.operatoreDAO = operatoreDAO;
    }

    public Operatore getOperatore(int id) throws SQLException {
        return operatoreDAO.trovaOperatorePerId(id);
    }

    public List<Operatore> getTuttiOperatori() throws SQLException {
        return operatoreDAO.trovaTuttiOperatori();
    }

    public void salvaOperatore(Operatore o) throws SQLException {
        operatoreDAO.salvaOperatore(o);
    }

    public void aggiornaOperatore(Operatore o) throws SQLException {
        operatoreDAO.aggiornaOperatore(o);
    }

    public void eliminaOperatore(int id) throws SQLException {
        operatoreDAO.eliminaOperatore(id);
    }
}