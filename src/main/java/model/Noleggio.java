package model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Rappresenta un contratto di noleggio effettivo concluso tra il cliente e l'azienda.
 * Gestisce le date di ritiro e restituzione, il calcolo dei costi e il riferimento
 * alla {@link Prenotazione} originaria.
 */
public class Noleggio {

    private int idNoleggio;
    private Date dataRitiro;
    private Date dataRestituzione;
    private BigDecimal costoTot;
    private Prenotazione prenotazione;

    /**
     * Crea una nuova istanza di Noleggio.
     *
     * @param idNoleggio   l'ID univoco del noleggio
     * @param dataRitiro   la data di inizio noleggio
     * @param prenotazione la {@link Prenotazione} da cui deriva il noleggio
     * @throws IllegalArgumentException se la data di ritiro è nulla
     */
    public Noleggio(int idNoleggio, Date dataRitiro, Prenotazione prenotazione) {
        if (dataRitiro == null) {
            throw new IllegalArgumentException("Data ritiro non valida");
        }
        this.idNoleggio = idNoleggio;
        this.dataRitiro = dataRitiro;
        this.dataRestituzione = null;
        this.costoTot = BigDecimal.ZERO;
        this.prenotazione = prenotazione;
    }

    /**
     * Restituisce l'ID del noleggio.
     * @return l'ID univoco
     */
    public int getIdNoleggio() {
        return idNoleggio;
    }

    /**
     * Restituisce la data di ritiro.
     * @return la data di ritiro
     */
    public Date getDataRitiro() {
        return dataRitiro;
    }

    /**
     * Restituisce la data di restituzione (null se il noleggio è ancora attivo).
     * @return la data di restituzione
     */
    public Date getDataRestituzione() {
        return dataRestituzione;
    }

    /**
     * Restituisce il costo totale calcolato per il noleggio.
     * @return il costo totale
     */
    public BigDecimal getCostoTot() {
        return costoTot;
    }

    /**
     * Restituisce la prenotazione associata.
     * @return l'oggetto {@link Prenotazione}
     */
    public Prenotazione getPrenotazione() {
        return prenotazione;
    }

    /**
     * Restituisce il cliente che ha effettuato il noleggio.
     * @return l'oggetto {@link Cliente}
     */
    public Cliente getCliente() {
        return this.prenotazione.getCliente();
    }

    /**
     * Restituisce l'auto noleggiata.
     * @return l'oggetto {@link Auto}
     */
    public Auto getAuto() {
        return this.prenotazione.getAuto();
    }

    /**
     * Verifica se il noleggio è attualmente in corso.
     *
     * @return true se il veicolo non è ancora stato restituito
     */
    public boolean isAttivo() {
        return this.dataRestituzione == null;
    }

    /**
     * Imposta la data di restituzione del veicolo.
     * @param dataRestituzione la data di fine noleggio
     */
    public void setDataRestituzione(Date dataRestituzione) {
        this.dataRestituzione = dataRestituzione;
    }

    /**
     * Imposta manualmente il costo totale del noleggio.
     * @param costoTot l'importo calcolato
     */
    public void setCostoTot(BigDecimal costoTot) {
        this.costoTot = costoTot;
    }

    /**
     * Finalizza il noleggio impostando la data di restituzione e calcolando il costo finale.
     *
     * @param dataRestituzione la data di fine noleggio
     * @param costoGiornaliero il costo giornaliero dell'auto
     * @throws IllegalArgumentException se la data di restituzione è nulla o precedente al ritiro
     */
    public void chiudiNoleggio(Date dataRestituzione, BigDecimal costoGiornaliero) {
        if (dataRestituzione == null) {
            throw new IllegalArgumentException("Data restituzione non valida");
        }
        if (dataRestituzione.before(dataRitiro)) {
            throw new IllegalArgumentException("Restituzione prima del ritiro");
        }
        this.dataRestituzione = dataRestituzione;
        int giorni = calcolaDurataGiorni();
        this.costoTot = costoGiornaliero.multiply(BigDecimal.valueOf(giorni));
    }

    /**
     * Calcola la durata del noleggio in giorni.
     *
     * @return il numero di giorni trascorsi, o 0 se il noleggio non è terminato
     */
    public int calcolaDurataGiorni() {
        if (dataRestituzione == null) {
            return 0;
        }
        long diff = dataRestituzione.getTime() - dataRitiro.getTime();
        // Converte i millisecondi in giorni arrotondando per eccesso
        return (int) Math.ceil(diff / (1000.0 * 60 * 60 * 24));
    }

    @Override
    public String toString() {
        return "Noleggio #" + idNoleggio + " | Ritiro: " + dataRitiro +
                " | Restituzione: " + dataRestituzione + " | Costo: " + costoTot;
    }
}