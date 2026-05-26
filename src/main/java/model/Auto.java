package model;

import java.math.BigDecimal;

/**
 * The type Auto.
 */
public class Auto {

    /**
     * The enum Stato auto.
     */
//enum Auto
    public enum StatoAuto{
        /**
         * Disponibile stato auto.
         */
        DISPONIBILE,
        /**
         * Prenotata stato auto.
         */
        PRENOTATA,
        /**
         * Noleggiata stato auto.
         */
        NOLEGGIATA,
        /**
         * In manutenzione stato auto.
         */
        IN_MANUTENZIONE,
        /**
         * Non disponibile stato auto.
         */
        NON_DISPONIBILE
    }

    /**
     * Instantiates a new Auto.
     *
     * @param idAuto     the id auto
     * @param targa      the targa
     * @param modello    the modello
     * @param stato      the stato
     * @param costoDaily the costo daily
     */
//costruttore Auto
    public Auto(int idAuto, String targa, String modello, StatoAuto stato,BigDecimal costoDaily){
        if (targa == null || modello == null || stato == null) {
            throw new IllegalArgumentException("Parametri non validi");
        }
        if (costoDaily == null || costoDaily.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Costo giornaliero non valido");
        }
        this.idAuto = idAuto;
        this.targa = targa;
        this.modello = modello;
        this.stato = stato;
        this.costoDaily = costoDaily;
    }

    /**
     * Instantiates a new Auto.
     */
//costruttore vuoto
    public Auto() {
    }

    //attributi Auto
    private int idAuto;
    private String targa;
    private String modello;
    private StatoAuto stato;
    private BigDecimal costoDaily;

    /**
     * Gets id auto.
     *
     * @return the id auto
     */
//metodi Auto
    public int getIdAuto() {
        return idAuto;
    }

    /**
     * Gets targa.
     *
     * @return the targa
     */
    public String getTarga() {
        return targa;
    }

    /**
     * Gets modello.
     *
     * @return the modello
     */
    public String getModello() {
        return modello;
    }

    /**
     * Gets stato.
     *
     * @return the stato
     */
    public StatoAuto getStato() {
        return stato;
    }

    /**
     * Get costo daily big decimal.
     *
     * @return the big decimal
     */
    public BigDecimal getCostoDaily(){
        return costoDaily;
    }

    /**
     * Sets id auto.
     *
     * @param idAuto the id auto
     */
    public void setIdAuto(int idAuto) {
        this.idAuto = idAuto;
    }

    /**
     * Cambia stato.
     *
     * @param nuovoStato the nuovo stato
     */
    public void cambiaStato(StatoAuto nuovoStato) {
        if (nuovoStato == null) {
            throw new IllegalArgumentException("Stato non valido");
        }
        this.stato = nuovoStato;
    }

    /**
     * Is disponibile boolean.
     *
     * @return the boolean
     */
    public boolean isDisponibile() {
        return this.stato == StatoAuto.DISPONIBILE;
    }

    @Override
    public String toString(){
        return idAuto + " " + targa + " " + modello + " " + stato;
    }
}
