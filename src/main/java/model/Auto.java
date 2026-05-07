package model;

import java.math.BigDecimal;

public class Auto {

    //enum Auto
    public enum StatoAuto{
        DISPONIBILE,
        PRENOTATA,
        NOLEGGIATA,
        IN_MANUTENZIONE,
        NON_DISPONIBILE
    }

    //costruttore Auto
    public Auto(int idAuto, String targa, String modello, StatoAuto stato,BigDecimal costoDaily){
        if (targa == null || modello == null || stato == null) {
            throw new IllegalArgumentException("Parametri non validi");
        }
        if (costoDaily == null || costoDaily.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Costo giornaliero non valido");
        }
        this.idAuto = idAuto;
        this.targa = targa;
        this.modello = modello;
        this.stato = stato;
        this.costoDaily = costoDaily;
    }

    //attributi Auto
    private int idAuto;
    private String targa;
    private String modello;
    private StatoAuto stato;
    private BigDecimal costoDaily;

    //metodi Auto
    public int getIdAuto() {
        return idAuto;
    }

    public String getTarga() {
        return targa;
    }

    public String getModello() {
        return modello;
    }

    public StatoAuto getStato() {
        return stato;
    }

    public BigDecimal getCostoDaily(){
        return costoDaily;
    }

    public void cambiaStato(StatoAuto nuovoStato) {
        if (nuovoStato == null) {
            throw new IllegalArgumentException("Stato non valido");
        }
        this.stato = nuovoStato;
    }

    public boolean isDisponibile() {
        return this.stato == StatoAuto.DISPONIBILE;
    }

    @Override
    public String toString(){
        return idAuto + " " + targa + " " + modello + " " + stato;
    }
}
