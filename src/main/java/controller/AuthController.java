package controller;

import model.Utente;
import dao.UtenteDAO;

import java.sql.SQLException;

public class AuthController {

    private UtenteDAO utenteDAO;

    public AuthController(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }

    public Utente login(String username, String password) throws SQLException {

        Utente u =
                utenteDAO.trovaUtentePerUsername(username);

        if (u == null || !u.verificaPassword(password)) {

            throw new IllegalArgumentException(
                    "Credenziali non valide"
            );
        }

        return u;
    }
}