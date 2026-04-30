package model;

public class Utente {

    //costruttore Utente
    public Utente(int idUtente, String nome, String cognome, String email){
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome non valido");
        }
        if (cognome == null || cognome.isBlank()) {
            throw new IllegalArgumentException("Cognome non valido");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email non valida");
        }
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
