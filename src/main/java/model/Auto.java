package model;

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
    public Auto(int idAuto, String targa, String modello, StatoAuto stato,double costodaily){
        if (targa == null || modello == null || stato == null) {
            throw new IllegalArgumentException("Parametri non validi");
        }
        if (costodaily <= 0) {
            throw new IllegalArgumentException("Costo giornaliero non valido");
        }
        this.idAuto = idAuto;
        this.targa = targa;
        this.modello = modello;
        this.stato = stato;
        this.costodaily = costodaily;
    }

    //attributi Auto
    private int idAuto;
    private String targa;
    private String modello;
    private StatoAuto stato;
    private double costodaily;

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

    public void cambiaStato(StatoAuto nuovoStato) {
        if (nuovoStato == null) {
            throw new IllegalArgumentException("Stato non valido");
        }
        this.stato = nuovoStato;
    }

    @Override
    public String toString(){
        return idAuto + " " + targa + " " + modello + " " + stato;
    }
}
