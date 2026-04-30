package model;

public class Cliente extends Utente {

    //costruttore Cliente
    public Cliente(int idUtente,String nome, String cognome, String email,String patente, double credito){
        super(idUtente,nome,cognome,email);
        this.patente=patente;
        this.credito=credito;
    }

    //attributi Cliente
    private String patente;
    private double credito;

    //metodi Cliente
    @Override
    public String toString() {
       return super.toString() + " " + patente + " " + credito;
    }

}
