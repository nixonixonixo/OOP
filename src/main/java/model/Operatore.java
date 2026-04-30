package model;

public class Operatore  extends Utente{

    public Operatore(int idUtente, String nome,String cognome, String email,Ruolo ruolo){
        super(idUtente,nome,cognome,email);
        this.ruolo=ruolo;
    }

    private  Ruolo ruolo;

    public enum Ruolo{
        ADMIN,
        ADDETTONOLEGGIO,
        MANUTENTORE

    }

    //metodi Operatore
    @Override
    public String toString(){
        return super.toString() + " " + ruolo;
    }
}
