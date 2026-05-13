package service;

import dao.UtenteDAO;
import model.Utente;

import java.sql.SQLException;

public class AuthService {

    private final UtenteDAO utenteDAO;

    public AuthService(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }

    public Utente login(String username, String password) throws SQLException {

        Utente utente = utenteDAO.trovaUtentePerUsername(username);

        if (utente == null || !utente.verificaPassword(password)) {
            throw new IllegalArgumentException("Credenziali non valide");
        }

        return utente;
    }
}