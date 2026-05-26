package model;

import java.util.Date;

/**
 * The type Prenotazione.
 */
public class Prenotazione {

    /**
     * The enum Stato pren.
     */
    //enum
    public enum StatoPren {
        /**
         * In attesa stato pren.
         */
        IN_ATTESA,
        /**
         * Confermata stato pren.
         */
        CONFERMATA,
        /**
         * Annullata stato pren.
         */
        ANNULLATA
    }

    //attributi Prenotazione
    private int idPrenotazione;
    private Date dataInizio;
    private Date dataFine;
    private StatoPren stato;
    private Cliente cliente;
    private Auto auto;


    /**
     * Instantiates a new Prenotazione.
     *
     * @param idPrenotazione the id prenotazione
     * @param dataInizio     the data inizio
     * @param dataFine       the data fine
     * @param stato          the stato
     * @param cliente        the cliente
     * @param auto           the auto
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
     * Instantiates a new Prenotazione.
     */
    //metodi Prenotazione
    public Prenotazione() {}

    /**
     * Gets id prenotazione.
     *
     * @return the id prenotazione
     */
    public int getIdPrenotazione() { return idPrenotazione; }

    /**
     * Gets data inizio.
     *
     * @return the data inizio
     */
    public Date getDataInizio() { return dataInizio; }

    /**
     * Gets data fine.
     *
     * @return the data fine
     */
    public Date getDataFine() { return dataFine; }

    /**
     * Gets stato.
     *
     * @return the stato
     */
    public StatoPren getStato() { return stato; }

    /**
     * Gets cliente.
     *
     * @return the cliente
     */
    public Cliente getCliente() { return cliente; }

    /**
     * Gets auto.
     *
     * @return the auto
     */
    public Auto getAuto() { return auto; }


    /**
     * Sets auto.
     *
     * @param auto the auto
     */
    public void setAuto(Auto auto) {
        this.auto = auto;
    }

    /**
     * Sets cliente.
     *
     * @param cliente the cliente
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Sets data inizio.
     *
     * @param dataInizio the data inizio
     */
    public void setDataInizio(Date dataInizio) {
        this.dataInizio = dataInizio;
    }

    /**
     * Sets data fine.
     *
     * @param dataFine the data fine
     */
    public void setDataFine(Date dataFine) {
        this.dataFine = dataFine;
    }

    /**
     * Sets stato.
     *
     * @param stato the stato
     */
    public void setStato(StatoPren stato) {
        this.stato = stato;
    }

    /**
     * Sets id prenotazione.
     *
     * @param idPrenotazione the id prenotazione
     */
    public void setIdPrenotazione(int idPrenotazione) {
        this.idPrenotazione = idPrenotazione;
    }

    /**
     * Conferma.
     */
    //metodi di logica
    public void conferma() {
        if (stato == StatoPren.ANNULLATA) {
            throw new IllegalStateException("Prenotazione già annullata");
        }
        this.stato = StatoPren.CONFERMATA;
    }

    /**
     * Annulla.
     */
    public void annulla() {
        this.stato = StatoPren.ANNULLATA;
    }

    /**
     * Is sovrapposta boolean.
     *
     * @param altra the altra
     * @return the boolean
     */
    public boolean isSovrapposta(Prenotazione altra) {
        if (altra == null || this.dataFine == null || altra.dataFine == null) return false;
        return this.dataInizio.before(altra.dataFine) &&
                this.dataFine.after(altra.dataInizio);
    }

    @Override
    public String toString() {
        return idPrenotazione + " " + dataInizio + " " + dataFine  + " " + stato + " " + cliente.getCognome()
                + " " + auto.getTarga();
    }
}
