package dao;

import model.Cliente;

import java.sql.SQLException;
import java.util.List;

public interface ClienteDAO {

    void salvaCliente(Cliente cliente) throws SQLException;

    Cliente trovaClientePerId(int idUtente) throws SQLException;

    List<Cliente> trovaTuttiClienti() throws SQLException;

    void aggiornaCliente(Cliente cliente) throws SQLException;

    void eliminaCliente(int idUtente) throws SQLException;
}