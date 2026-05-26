package model;

import java.math.BigDecimal;

/**
 * Rappresenta un veicolo all'interno del sistema di noleggio.
 * Contiene le informazioni anagrafiche del veicolo, il suo stato attuale
 * e il costo giornaliero associato.
 */
public class Auto {

    /**
     * Definisce i possibili stati in cui un veicolo può trovarsi.
     */
    public enum StatoAuto {
        /** Veicolo pronto per essere noleggiato. */
        DISPONIBILE,
        /** Veicolo prenotato da un cliente. */
        PRENOTATA,
        /** Veicolo attualmente in uso. */
        NOLEGGIATA,
        /** Veicolo in fase di manutenzione. */
        IN_MANUTENZIONE,
        /** Veicolo non disponibile per altri motivi. */
        NON_DISPONIBILE
    }

    private int idAuto;
    private String targa;
    private String modello;
    private StatoAuto stato;
    private BigDecimal costoDaily;

    /**
     * Crea una nuova istanza di Auto con i parametri specificati.
     *
     * @param idAuto     l'identificativo univoco dell'auto
     * @param targa      la targa del veicolo
     * @param modello    il modello del veicolo
     * @param stato      lo stato iniziale dell'auto
     * @param costoDaily il costo giornaliero del noleggio
     * @throws IllegalArgumentException se i parametri obbligatori sono nulli o il costo è negativo
     */
    public Auto(int idAuto, String targa, String modello, StatoAuto stato, BigDecimal costoDaily) {
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
     * Costruttore vuoto per inizializzazioni differite.
     */
    public Auto() {
    }

    /**
     * Restituisce l'ID dell'auto.
     * @return l'ID univoco
     */
    public int getIdAuto() {
        return idAuto;
    }

    /**
     * Restituisce la targa del veicolo.
     * @return la targa
     */
    public String getTarga() {
        return targa;
    }

    /**
     * Restituisce il modello del veicolo.
     * @return il modello
     */
    public String getModello() {
        return modello;
    }

    /**
     * Restituisce lo stato attuale dell'auto.
     * @return lo {@link StatoAuto} corrente
     */
    public StatoAuto getStato() {
        return stato;
    }

    /**
     * Restituisce il costo giornaliero di noleggio.
     * @return il costo come BigDecimal
     */
    public BigDecimal getCostoDaily() {
        return costoDaily;
    }

    /**
     * Imposta l'ID dell'auto.
     * @param idAuto l'ID da assegnare
     */
    public void setIdAuto(int idAuto) {
        this.idAuto = idAuto;
    }

    /**
     * Modifica lo stato attuale del veicolo.
     *
     * @param nuovoStato il nuovo {@link StatoAuto} da impostare
     * @throws IllegalArgumentException se lo stato fornito è null
     */
    public void cambiaStato(StatoAuto nuovoStato) {
        if (nuovoStato == null) {
            throw new IllegalArgumentException("Stato non valido");
        }
        this.stato = nuovoStato;
    }

    /**
     * Verifica se il veicolo è disponibile per un nuovo noleggio.
     *
     * @return true se l'auto è disponibile, false altrimenti
     */
    public boolean isDisponibile() {
        return this.stato == StatoAuto.DISPONIBILE;
    }

    @Override
    public String toString() {
        return idAuto + " " + targa + " " + modello + " " + stato;
    }
}