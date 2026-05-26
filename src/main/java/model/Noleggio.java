package model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * The type Noleggio.
 */
public class Noleggio {

    /**
     * Instantiates a new Noleggio.
     *
     * @param idNoleggio   the id noleggio
     * @param dataRitiro   the data ritiro
     * @param prenotazione the prenotazione
     */
//costruttore Noleggio
    public Noleggio(int idNoleggio, Date dataRitiro, Prenotazione prenotazione) {
        if (dataRitiro == null) {
            throw new IllegalArgumentException("Data ritiro non valida");
        }
        this.idNoleggio = idNoleggio;
        this.dataRitiro = dataRitiro;
        this.dataRestituzione = null;
        this.costoTot = new BigDecimal("0");
        this.prenotazione = prenotazione;
    }

    //attributi Noleggio
    private int idNoleggio;
    private Date dataRitiro;
    private Date dataRestituzione;
    private BigDecimal costoTot;

    //associazioni Noleggio
    private Prenotazione prenotazione;

    /**
     * Gets id noleggio.
     *
     * @return the id noleggio
     */
//metodi Noleggio
    public int getIdNoleggio() {
        return idNoleggio;
    }

    /**
     * Gets data ritiro.
     *
     * @return the data ritiro
     */
    public Date getDataRitiro() {
        return dataRitiro;
    }

    /**
     * Gets data restituzione.
     *
     * @return the data restituzione
     */
    public Date getDataRestituzione() {
        return dataRestituzione;
    }

    /**
     * Gets costo tot.
     *
     * @return the costo tot
     */
    public BigDecimal getCostoTot() {
        return costoTot;
    }

    /**
     * Gets prenotazione.
     *
     * @return the prenotazione
     */
    public Prenotazione getPrenotazione() {
        return prenotazione;
    }

    /**
     * Gets cliente.
     *
     * @return the cliente
     */
    public Cliente getCliente() {
        return this.prenotazione.getCliente();
    }

    /**
     * Gets auto.
     *
     * @return the auto
     */
    public Auto getAuto() {
        return this.prenotazione.getAuto();
    }

    /**
     * Is attivo boolean.
     *
     * @return the boolean
     */
    public boolean isAttivo() {
        return this.dataRestituzione == null;
    }

    /**
     * Set data restituzione.
     *
     * @param dataRestituzione the data restituzione
     */
    public void setDataRestituzione(Date dataRestituzione){
        this.dataRestituzione = dataRestituzione;
    }

    /**
     * Set costo tot.
     *
     * @param costoTot the costo tot
     */
    public void setCostoTot(BigDecimal costoTot){
        this.costoTot = costoTot;
    }

    /**
     * Chiudi noleggio.
     *
     * @param dataRestituzione the data restituzione
     * @param costoGiornaliero the costo giornaliero
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
     * Calcola durata giorni int.
     *
     * @return the int
     */
    public int calcolaDurataGiorni() {
        if (dataRestituzione == null) {
            return 0;
        }
        long diff = dataRestituzione.getTime() - dataRitiro.getTime();
        return (int) Math.ceil(diff / (1000.0 * 60 * 60 * 24));
    }

    @Override
    public String toString(){
        return idNoleggio+ " " + dataRestituzione +  " " +dataRitiro + " " + costoTot
        + " " + prenotazione.getIdPrenotazione();
    }

}
