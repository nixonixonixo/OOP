package service;

import dao.PrenotazioneDAO;
import dao.NoleggioDAO;
import model.Prenotazione;
import model.Noleggio;

import java.sql.SQLException;
import java.util.List;
import java.util.Date;

public class PrenotazioneService {

    private final PrenotazioneDAO prenotazioneDAO;
    private final NoleggioDAO noleggioDAO;

    public PrenotazioneService(PrenotazioneDAO prenotazioneDAO, NoleggioDAO noleggioDAO) {
        this.prenotazioneDAO = prenotazioneDAO;
        this.noleggioDAO = noleggioDAO;
    }

    /**
     * Recupera tutte le prenotazioni nel sistema (per l'operatore)
     */
    public List<Prenotazione> getTuttePrenotazioni() throws SQLException {
        return prenotazioneDAO.trovaTuttePrenotazioni();
    }

    /**
     * Recupera solo le prenotazioni di un determinato cliente
     */
    public List<Prenotazione> getPrenotazioniCliente(int idCliente) throws SQLException {
        return prenotazioneDAO.trovaPrenotazioniCliente(idCliente);
    }

    /**
     * FLUSSO CRITICO: Conferma la prenotazione e apre il noleggio
     */
    public void confermaPrenotazione(int idPrenotazione) throws Exception {
        // 1. Recuperiamo la prenotazione dal database
        Prenotazione p = prenotazioneDAO.trovaPrenotazionePerId(idPrenotazione);

        if (p == null) {
            throw new Exception("Errore: Prenotazione non trovata.");
        }

        if (p.getStato() != Prenotazione.StatoPren.IN_ATTESA) {
            throw new Exception("Errore: È possibile confermare solo prenotazioni 'IN ATTESA'.");
        }

        // 2. Aggiorniamo lo stato della prenotazione a CONFERMATA
        prenotazioneDAO.aggiornaStatoPrenotazione(idPrenotazione, Prenotazione.StatoPren.CONFERMATA);

        // 3. Creiamo l'oggetto Noleggio
        // L'ID è 0 perché il DB lo genera automaticamente (Identity)
        // La data di ritiro è la data attuale (new Date())
        Noleggio nuovoNoleggio = new Noleggio(0, new Date(), p);

        // 4. Salviamo il noleggio sul database tramite il DAO
        noleggioDAO.salvaNoleggio(nuovoNoleggio);
    }

    /**
     * Annulla la prenotazione e libera l'auto
     */
    public void annullaPrenotazione(int idPrenotazione) throws Exception {
        Prenotazione p = prenotazioneDAO.trovaPrenotazionePerId(idPrenotazione);

        if (p == null) {
            throw new Exception("Prenotazione non trovata.");
        }

        // Aggiorniamo lo stato a ANNULLATA
        prenotazioneDAO.aggiornaStatoPrenotazione(idPrenotazione, Prenotazione.StatoPren.ANNULLATA);

        // Nota: Qui potresti aggiungere una chiamata a autoDAO per rimettere
        // l'auto come DISPONIBILE se il sistema la blocca già alla richiesta.
    }
}