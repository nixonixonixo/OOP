package model;

public class Utente {

    //costruttore Utente
    public Utente(int idUtente, String nome, String cognome, String email){
        this.idUtente = idUtente;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }

    //attributi Utente
    protected int idUtente;
    protected String nome;
    protected String cognome;
    protected String email;

    //metodi Utente
    @Override
    public String toString() {
        return idUtente + " " + nome + " " + cognome  + " " + email;
    }
}
