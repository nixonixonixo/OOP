package dao;

import model.Operatore;

import java.sql.SQLException;
import java.util.List;

public interface OperatoreDAO {

    void salvaOperatore(Operatore operatore) throws SQLException;

    Operatore trovaOperatorePerId(int idUtente) throws SQLException;

    List<Operatore> trovaTuttiOperatori() throws SQLException;

    void aggiornaOperatore(Operatore operatore) throws SQLException;

    void eliminaOperatore(int idUtente) throws SQLException;
}