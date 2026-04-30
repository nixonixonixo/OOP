package model;

public class Operatore  extends Utente{
    //costruttore
    public Operatore(int idUtente, String nome,String cognome, String email,Ruolo ruolo){
        super(idUtente,nome,cognome,email);
        this.ruolo=ruolo;
    }
    //attributo Operatore
    private  Ruolo ruolo;

    //enumerazione ruoli
    public enum Ruolo{
        ADMIN,
        ADDETTONOLEGGIO,
        MANUTENTORE

    }

    //metodi Operatore
    @Override
    public String toString(){return super.toString() + " " + ruolo;}

}
