package model;

import java.util.Date;

/**
 * Rappresenta una richiesta di noleggio effettuata da un cliente per un veicolo.
 */
public class Prenotazione {

    /**
     * Definisce i possibili stati della prenotazione.
     */
    public enum StatoPren {
        /** Richiesta inviata, in attesa di approvazione. */
        IN_ATTESA,
        /** Prenotazione confermata e valida. */
        CONFERMATA,
        /** Prenotazione annullata dal cliente o dal sistema. */
        ANNULLATA
    }

    private int idPrenotazione;
    private Date dataInizio;
    private Date dataFine;
    private StatoPren stato;
    private Cliente cliente;
    private Auto auto;

    /**
     * Crea una nuova istanza di Prenotazione.
     *
     * @param idPrenotazione l'ID univoco della prenotazione
     * @param dataInizio     la data di inizio prenotazione
     * @param dataFine       la data di fine prenotazione
     * @param stato          lo {@link StatoPren} iniziale
     * @param cliente        il {@link Cliente} che effettua la richiesta
     * @param auto           l'{@link Auto} richiesta
     * @throws IllegalArgumentException se i parametri obbligatori sono nulli o la fine precede l'inizio
     */
    public Prenotazione(int idPrenotazione, Date dataInizio, Date dataFine, StatoPren stato, Cliente cliente, Auto auto) {
        if (dataInizio == null || stato == null) {
            throw new IllegalArgumentException("Parametri obbligatori mancanti");
        }
        if (dataFine != null && dataFine.before(dataInizio)) {
            throw new IllegalArgumentException("La data di fine non può essere precedente all'inizio");
        }

        this.idPrenotazione = idPrenotazione;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.stato = stato;
        this.cliente = cliente;
        this.auto = auto;
    }

    /**
     * Costruttore vuoto per uso con framework di persistenza.
     */
    public Prenotazione() {}

    /**
     * Restituisce l'ID della prenotazione.
     * @return l'ID univoco
     */
    public int getIdPrenotazione() { return idPrenotazione; }

    /**
     * Restituisce la data di inizio.
     * @return la data di inizio
     */
    public Date getDataInizio() { return dataInizio; }

    /**
     * Restituisce la data di fine.
     * @return la data di fine
     */
    public Date getDataFine() { return dataFine; }

    /**
     * Restituisce lo stato corrente.
     * @return lo {@link StatoPren}
     */
    public StatoPren getStato() { return stato; }

    /**
     * Restituisce il cliente associato.
     * @return l'oggetto {@link Cliente}
     */
    public Cliente getCliente() { return cliente; }

    /**
     * Restituisce l'auto prenotata.
     * @return l'oggetto {@link Auto}
     */
    public Auto getAuto() { return auto; }

    /**
     * Imposta l'auto per la prenotazione.
     * @param auto l'auto da associare
     */
    public void setAuto(Auto auto) { this.auto = auto; }

    /**
     * Imposta il cliente per la prenotazione.
     * @param cliente il cliente da associare
     */
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    /**
     * Imposta la data di inizio.
     * @param dataInizio la data di inizio
     */
    public void setDataInizio(Date dataInizio) { this.dataInizio = dataInizio; }

    /**
     * Imposta la data di fine.
     * @param dataFine la data di fine
     */
    public void setDataFine(Date dataFine) { this.dataFine = dataFine; }

    /**
     * Imposta lo stato della prenotazione.
     * @param stato il nuovo {@link StatoPren}
     */
    public void setStato(StatoPren stato) { this.stato = stato; }

    /**
     * Imposta l'ID della prenotazione.
     * @param idPrenotazione l'ID da assegnare
     */
    public void setIdPrenotazione(int idPrenotazione) { this.idPrenotazione = idPrenotazione; }

    /**
     * Transita lo stato a {@link StatoPren#CONFERMATA}.
     * @throws IllegalStateException se la prenotazione è già annullata
     */
    public void conferma() {
        if (stato == StatoPren.ANNULLATA) {
            throw new IllegalStateException("Prenotazione già annullata");
        }
        this.stato = StatoPren.CONFERMATA;
    }

    /**
     * Annulla la prenotazione portando lo stato a {@link StatoPren#ANNULLATA}.
     */
    public void annulla() {
        this.stato = StatoPren.ANNULLATA;
    }

    /**
     * Verifica se questa prenotazione si sovrappone temporalmente con un'altra.
     *
     * @param altra l'altra prenotazione da confrontare
     * @return true se c'è sovrapposizione, false altrimenti
     */
    public boolean isSovrapposta(Prenotazione altra) {
        if (altra == null || this.dataFine == null || altra.dataFine == null) return false;
        return this.dataInizio.before(altra.dataFine) &&
                this.dataFine.after(altra.dataInizio);
    }

    @Override
    public String toString() {
        return "Prenotazione #" + idPrenotazione + " | Periodo: " + dataInizio + " - " + dataFine +
                " | Stato: " + stato + " | Cliente: " + cliente.getCognome() + " | Auto: " + auto.getTarga();
    }
}