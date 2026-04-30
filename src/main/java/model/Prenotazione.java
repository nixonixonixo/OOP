package model;

import java.util.Date;

public class Prenotazione {

    //enum Prenotazione
    public enum StatoPren{
        IN_ATTESA,
        CONFERMATA,
        ANNULLATA
    }

    //costruttore Prenotazione
    public Prenotazione(int idPrenotazione, Date dataInizio, Date dataFine, StatoPren stato){
        if (dataInizio == null || dataFine == null || stato == null) {
            throw new IllegalArgumentException("Parametri non validi");
        }
        if (dataFine.before(dataInizio)) {
            throw new IllegalArgumentException("Intervallo di date non valido");
        }
        this.idPrenotazione = idPrenotazione;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.stato = stato;
    }

    //attributi Prenotazione
    private int idPrenotazione;
    private Date dataInizio;
    private Date dataFine;
    private StatoPren stato;

    //metodi Prenotazione
    public int getIdPrenotazione() {
        return idPrenotazione;
    }

    public Date getDataInizio() {
        return dataInizio;
    }

    public Date getDataFine() {
        return dataFine;
    }

    public StatoPren getStato() {
        return stato;
    }

    public void conferma() {
        if (stato == StatoPren.ANNULLATA) {
            throw new IllegalStateException("Prenotazione annullata");
        }
        stato = StatoPren.CONFERMATA;
    }

    public void annulla() {
        stato = StatoPren.ANNULLATA;
    }

    public boolean isSovrapposta(Prenotazione altra) {
        if (altra == null) return false;
        return this.dataInizio.before(altra.dataFine) &&
                this.dataFine.after(altra.dataInizio);
    }

    public boolean isValida() {
        return stato != StatoPren.ANNULLATA;
    }

    @Override
    public String toString() {
        return idPrenotazione + " " + dataInizio + " " + dataFine  + " " + stato;
    }
}
