package service;

import dao.UtenteDAO;
import dao.ClienteDAO;
import dao.OperatoreDAO;
import model.Cliente;
import model.Operatore;
import model.Utente;

import java.sql.SQLException;

public class UtenteService {

    private final UtenteDAO utenteDAO;
    private final ClienteDAO clienteDAO;
    private final OperatoreDAO operatoreDAO;

    public UtenteService(UtenteDAO utenteDAO,
                         ClienteDAO clienteDAO,
                         OperatoreDAO operatoreDAO) {
        this.utenteDAO = utenteDAO;
        this.clienteDAO = clienteDAO;
        this.operatoreDAO = operatoreDAO;
    }

    // LOGIN
    public Utente login(String username, String password) throws SQLException {
        Utente u = utenteDAO.trovaUtentePerUsername(username);

        if (u == null || !u.verificaPassword(password)) {
            throw new IllegalArgumentException("Credenziali errate");
        }

        return u;
    }

    // REGISTRAZIONE
    public void registraCliente(Cliente cliente) throws SQLException {
        utenteDAO.salvaUtente(cliente);
        clienteDAO.salvaCliente(cliente);
    }
}