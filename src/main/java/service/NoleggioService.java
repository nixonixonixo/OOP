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

    // Il costruttore ora richiede 3 DAO per completare l'operazione di chiusura
    public NoleggioService(NoleggioDAO noleggioDAO, AutoDAO autoDAO, PagamentoDAO pagamentoDAO) {
        this.noleggioDAO = noleggioDAO;
        this.autoDAO = autoDAO;
        this.pagamentoDAO = pagamentoDAO;
    }

    /**
     * Recupera tutti i noleggi (utile per lo storico dell'operatore)
     */
    public List<Noleggio> getTuttiNoleggi() throws SQLException {
        return noleggioDAO.trovaTuttiNoleggi();
    }

    /**
     * CHIUSURA NOLEGGIO: Il cuore della logica di business
     * Calcola il costo, libera l'auto e crea il pagamento.
     */
    public void chiudiNoleggio(int idNoleggio) throws Exception {
        // 1. Recupero il noleggio completo dal DB (con i JOIN su Auto e Prenotazione)
        Noleggio n = noleggioDAO.trovaNoleggioPerId(idNoleggio);

        if (n == null) {
            throw new Exception("Errore: Noleggio non trovato.");
        }

        if (n.getDataRestituzione() != null) {
            throw new Exception("Errore: Questo noleggio è già stato chiuso.");
        }

        // 2. Calcolo del costo finale
        // Usiamo la data odierna come data di restituzione
        Date dataOggi = new Date();
        // Recuperiamo il costo giornaliero dall'auto associata alla prenotazione
        double costoDaily = n.getPrenotazione().getAuto().getCostoDaily().doubleValue();

        // Usiamo il metodo che hai scritto nella classe Model Noleggio
        n.chiudiNoleggio(dataOggi, java.math.BigDecimal.valueOf(costoDaily));

        // 3. AGGIORNAMENTO DB: Salviamo i dati di chiusura (data restituzione e costo totale)
        noleggioDAO.aggiornaNoleggio(n);

        // 4. GENERAZIONE PAGAMENTO: Creiamo la pendenza per il cliente
        // Lo stato iniziale è IN_ATTESA (il cliente pagherà dalla sua area personale)
        Pagamento nuovoPagamento = new Pagamento(
                0,
                n.getCostoTot(),
                Pagamento.StatoPagamento.IN_ATTESA,
                n
        );
        pagamentoDAO.salvaPagamento(nuovoPagamento);

        // 5. LIBERAZIONE AUTO: L'auto torna disponibile per nuove prenotazioni
        int idAuto = n.getPrenotazione().getAuto().getIdAuto();
        autoDAO.aggiornaStatoAuto(idAuto, Auto.StatoAuto.DISPONIBILE);
    }
}