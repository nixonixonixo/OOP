public class PrenotazioneService {

    private final PrenotazioneDAO prenotazioneDAO;
    private final AutoDAO autoDAO;

    public PrenotazioneService(PrenotazioneDAO prenotazioneDAO, AutoDAO autoDAO) {
        this.prenotazioneDAO = prenotazioneDAO;
        this.autoDAO = autoDAO;
    }

    public void creaPrenotazione(int idCliente, int idAuto) throws Exception {
        prenotazioneDAO.creaPrenotazione(idCliente, idAuto);
    }

    public List<Prenotazione> getPrenotazioniCliente(int idCliente) throws Exception {
        return prenotazioneDAO.trovaPrenotazioniCliente(idCliente);
    }

    public List<Prenotazione> getTuttePrenotazioni() throws Exception {
        return prenotazioneDAO.trovaTuttePrenotazioni();
    }

    public void confermaPrenotazione(int id) throws Exception {
        prenotazioneDAO.aggiornaStatoPrenotazione(id, Prenotazione.StatoPren.CONFERMATA);
    }

    public void annullaPrenotazione(int id) throws Exception {
        prenotazioneDAO.aggiornaStatoPrenotazione(id, Prenotazione.StatoPren.ANNULLATA);
    }
}
