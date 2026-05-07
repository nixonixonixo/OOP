package dao;

import model.Auto;

import java.sql.SQLException;
import java.util.List;

public interface AutoDAO {

    void salvaAuto(Auto auto) throws SQLException;

    Auto trovaAutoPerId(int idAuto) throws SQLException;

    List<Auto> trovaTutteAuto() throws SQLException;

    List<Auto> trovaAutoDisponibili() throws SQLException;

    void aggiornaAuto(Auto auto) throws SQLException;

    void aggiornaStatoAuto(int idAuto, Auto.StatoAuto stato)
            throws SQLException;

    void eliminaAuto(int idAuto) throws SQLException;
}