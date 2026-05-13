package service;

import dao.UtenteDAO;
import dao.ClienteDAO;
import model.Utente;

import java.sql.SQLException;

public class UtenteService {

    private final UtenteDAO utenteDAO;
    private final ClienteDAO clienteDAO;

    public UtenteService(UtenteDAO utenteDAO, ClienteDAO clienteDAO) {
        this.utenteDAO = utenteDAO;
        this.clienteDAO = clienteDAO;
    }

    public Utente login(String username, String password) throws SQLException {
        Utente u = utenteDAO.trovaUtentePerUsername(username);

        if (u == null || !u.verificaPassword(password)) {
            throw new IllegalArgumentException("Credenziali non valide");
        }

        return u;
    }

    public void registraUtente(Utente u) throws SQLException {
        utenteDAO.salvaUtente(u);

        if (u instanceof model.Cliente c) {
            clienteDAO.salvaCliente(c);
        }
    }
}