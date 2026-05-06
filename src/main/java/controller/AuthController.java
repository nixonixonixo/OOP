package controller;

import model.Utente;
import dao.UtenteDAO;

public class AuthController {

    private UtenteDAO utenteDAO;

    public AuthController(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }

    public Utente login(String username, String password) {
        Utente u = utenteDAO.findByUsername(username);

        if (u == null || !u.verificaPassword(password)) {
            throw new IllegalArgumentException("Credenziali non valide");
        }

        return u;
    }
}