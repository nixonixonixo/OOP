package model;

public class Operatore  extends Utente{

    //costruttore Operatore
    public Operatore(int idUtente, String nome,String cognome, String email,Ruolo ruolo){
        super(idUtente,nome,cognome,email);
        this.ruolo=ruolo;
    }

    //enum Operatore
    public enum Ruolo{
        ADMIN,
        ADDETTONOLEGGIO,
        MANUTENTORE

    }

    //attributi Operatore
    private  Ruolo ruolo;

    //metodi Operatore
    @Override
    public String toString(){
        return super.toString() + " " + ruolo;
    }
}
