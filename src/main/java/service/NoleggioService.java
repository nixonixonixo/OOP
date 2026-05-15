package service;

import dao.NoleggioDAO;
import dao.AutoDAO;
import dao.PagamentoDAO;
import model.Noleggio;
import model.Pagamento;
import model.Auto;

import java.sql.SQLException;
import java.util.List;
import java.util.Date;

public class NoleggioService {

    private final NoleggioDAO noleggioDAO;
    private final AutoDAO autoDAO;
    private final PagamentoDAO pagamentoDAO;

    public NoleggioService(NoleggioDAO noleggioDAO, AutoDAO autoDAO, PagamentoDAO pagamentoDAO) {
        this.noleggioDAO = noleggioDAO;
        this.autoDAO = autoDAO;
        this.pagamentoDAO = pagamentoDAO;
    }

    public List<Noleggio> getTuttiNoleggi() throws SQLException {
        return noleggioDAO.trovaTuttiNoleggi();
    }

    public void chiudiNoleggio(int idNoleggio) throws Exception {
        Noleggio n = noleggioDAO.trovaNoleggioPerId(idNoleggio);

        if (n == null) {
            throw new Exception("Errore: Noleggio non trovato.");
        }

        if (n.getDataRestituzione() != null) {
            throw new Exception("Errore: Questo noleggio è già stato chiuso.");
        }

        Date dataOggi = new Date();
        double costoDaily = n.getPrenotazione().getAuto().getCostoDaily().doubleValue();

        n.chiudiNoleggio(dataOggi, java.math.BigDecimal.valueOf(costoDaily));

        noleggioDAO.aggiornaNoleggio(n);

        Pagamento nuovoPagamento = new Pagamento(
                0,
                n.getCostoTot(),
                Pagamento.StatoPagamento.IN_ATTESA,
                n
        );
        pagamentoDAO.salvaPagamento(nuovoPagamento);

        int idAuto = n.getPrenotazione().getAuto().getIdAuto();
        autoDAO.aggiornaStatoAuto(idAuto, Auto.StatoAuto.DISPONIBILE);
    }
}