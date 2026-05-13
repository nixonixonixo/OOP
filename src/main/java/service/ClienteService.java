package service;

import dao.ClienteDAO;
import model.Cliente;

import java.math.BigDecimal;
import java.sql.SQLException;

public class ClienteService {

    private final ClienteDAO clienteDAO;

    public ClienteService(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    public Cliente getClienteById(int id) throws SQLException {
        return clienteDAO.trovaClientePerId(id);
    }

    public void ricaricaCredito(int id, BigDecimal importo) throws SQLException {
        clienteDAO.aggiornaCredito(id, importo);
    }
}