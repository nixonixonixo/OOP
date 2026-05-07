package controller;

import model.*;
import dao.*;

import java.sql.SQLException;
import java.util.List;

public class AutoController {

    private AutoDAO autoDAO;

    public AutoController(AutoDAO autoDAO) {
        this.autoDAO = autoDAO;
    }

    public List<Auto> getAutoDisponibili() throws SQLException {
        return autoDAO.trovaAutoDisponibili();
    }

    public void aggiornaStatoAuto(Auto auto, Auto.StatoAuto stato) throws SQLException {
        auto.cambiaStato(stato);
        autoDAO.aggiornaAuto(auto);
    }
}