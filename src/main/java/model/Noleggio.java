package model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Noleggio {

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

    //metodi Noleggio
    public int getIdNoleggio() {
        return idNoleggio;
    }

    public Date getDataRitiro() {
        return dataRitiro;
    }

    public Date getDataRestituzione() {
        return dataRestituzione;
    }

    public BigDecimal getCostoTot() {
        return costoTot;
    }

    public Prenotazione getPrenotazione() {
        return prenotazione;
    }

    public void setDataRestituzione(Date dataRestituzione){
        this.dataRestituzione = dataRestituzione;
    }

    public void setCostoTot(BigDecimal costoTot){
        this.costoTot = costoTot;
    }

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

    public int calcolaDurataGiorni() {
        if (dataRestituzione == null) {
            return 0;
        }
        long diff = dataRestituzione.getTime() - dataRitiro.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public String toString(){
        return idNoleggio+ " " + dataRestituzione +  " " +dataRitiro + " " + costoTot
        + " " + prenotazione.getIdPrenotazione();
    }

}
