package controller;

import model.*;
import dao.*;
import java.util.List;

public class AutoController {

    private AutoDAO autoDAO;

    public AutoController(AutoDAO autoDAO) {
        this.autoDAO = autoDAO;
    }

    public List<Auto> getAutoDisponibili() {
        return autoDAO.findDisponibili();
    }

    public void aggiornaStatoAuto(Auto auto, Auto.StatoAuto stato) {
        auto.cambiaStato(stato);
        autoDAO.update(auto);
    }
}