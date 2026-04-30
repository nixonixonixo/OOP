package model;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Noleggio {

    //costruttore Noleggio
    public Noleggio(int idNoleggio, Date dataRitiro){
        if (dataRitiro == null) {
            throw new IllegalArgumentException("Data ritiro non valida");
        }
        this.idNoleggio = idNoleggio;
        this.dataRitiro = dataRitiro;
        this.dataRestituzione = null;
        this.costoTot = 0;
    }

    //attributi Noleggio
    private int idNoleggio;
    private Date dataRitiro;
    private Date dataRestituzione;
    private double costoTot;

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

    public double getCostoTot() {
        return costoTot;
    }

    public void chiudiNoleggio(Date dataRestituzione, double costoGiornaliero) {
        if (dataRestituzione == null) {
            throw new IllegalArgumentException("Data restituzione non valida");
        }
        if (dataRestituzione.before(dataRitiro)) {
            throw new IllegalArgumentException("Restituzione prima del ritiro");
        }
        this.dataRestituzione = dataRestituzione;
        int giorni = calcolaDurataGiorni();
        this.costoTot = giorni * costoGiornaliero;
    }

    public int calcolaDurataGiorni() {
        if (dataRestituzione == null) {
            return 0;
        }
        long diff = dataRestituzione.getTime() - dataRitiro.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public String toString(){return idNoleggio+ " " + dataRestituzione +  " " +dataRitiro + " " + costoTot;}

}
