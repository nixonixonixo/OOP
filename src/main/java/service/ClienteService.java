package service;

import dao.ClienteDAO;
import model.Cliente;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ClienteService {

    private final ClienteDAO clienteDAO;

    public ClienteService(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    public List<Cliente> getTuttiClienti() throws SQLException {
        return clienteDAO.trovaTuttiClienti();
    }

    public Cliente getCliente(int id) throws SQLException {
        return clienteDAO.trovaClientePerId(id);
    }

    public void aggiornaCredito(int id, BigDecimal nuovoCredito) throws SQLException {
        clienteDAO.aggiornaCredito(id, nuovoCredito);
    }
}