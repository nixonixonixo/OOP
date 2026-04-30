package model;

import java.util.Date;

public class Prenotazione {

    //costruttore Prenotazione
    public Prenotazione(int idPrenotazione, Date dataInizio, Date dataFine, StatoPren stato){
        this.idPrenotazione = idPrenotazione;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.stato = stato;
    }

    //enum Prenotazione
    public enum StatoPren{
        IN_ATTESA,
        CONFERMATA,
        ANNULLATA
    }

    //attributi Prenotazione
    private int idPrenotazione;
    private Date dataInizio;
    private Date dataFine;
    private StatoPren stato;

    //metodi Prenotazione
    @Override
    public String toString() {
        return idPrenotazione + " " + dataInizio + " " + dataFine  + " " + stato;
    }
}
