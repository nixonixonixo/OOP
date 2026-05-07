package dao;

import model.Noleggio;

import java.sql.SQLException;
import java.util.List;

public interface NoleggioDAO {

    void salvaNoleggio(Noleggio noleggio)
            throws SQLException;

    Noleggio trovaNoleggioPerId(int idNoleggio)
            throws SQLException;

    List<Noleggio> trovaTuttiNoleggi()
            throws SQLException;

    void aggiornaNoleggio(Noleggio noleggio)
            throws SQLException;

    void eliminaNoleggio(int idNoleggio)
            throws SQLException;
}