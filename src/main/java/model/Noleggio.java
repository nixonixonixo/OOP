package model;

import java.util.Date;

public class Noleggio {

    //costruttore Noleggio
    public Noleggio(int idNoleggio, Date dataRitiro, Date dataRestituzione, double costoTot){
        this.costoTot = costoTot;
        this.idNoleggio = idNoleggio;
        this.dataRestituzione = dataRestituzione;
        this.dataRitiro = dataRitiro;
    }

    //attributi Noleggio
    private int idNoleggio;
    private Date dataRitiro;
    private Date dataRestituzione;
    private double costoTot;

    //metodi Noleggio
    @Override
    public String toString(){return super.toString() + " " + idNoleggio+ "" + dataRestituzione +  "" +dataRitiro + "" + costoTot  ;}




}
