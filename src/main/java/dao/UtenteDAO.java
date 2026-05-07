package dao;

import model.Utente;

import java.sql.SQLException;
import java.util.List;

public interface UtenteDAO {

    void salvaUtente(Utente utente) throws SQLException;

    Utente trovaUtentePerId(int idUtente) throws SQLException;

    Utente trovaUtentePerUsername(String username) throws SQLException;

    List<Utente> trovaTuttiUtenti() throws SQLException;

    void aggiornaUtente(Utente utente) throws SQLException;

    void eliminaUtente(int idUtente) throws SQLException;
}