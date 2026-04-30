package model;

public class Auto {

    //costruttore Auto
    public Auto(int idAuto, String targa, String modello, StatoAuto stato){
        this.idAuto = idAuto;
        this.targa = targa;
        this.modello = modello;
        this.stato = stato;
    }

    //enum Auto
    public enum StatoAuto{
        DISPONIBILE,
        PRENOTATA,
        NOLEGGIATA,
        IN_MANUTENZIONE,
        NON_DISPONIBILE
    }

    //attributi Auto
    private int idAuto;
    private String targa;
    private String modello;
    private StatoAuto stato;

    //metodi Auto
    @Override
    public String toString(){
        return idAuto + " " + targa + " " + modello + " " + stato;
    }
}
