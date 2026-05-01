package model;

//import per creazione password sicura
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Utente {

    //costruttore Utente
    public Utente(int idUtente, String username, String password, String nome, String cognome, String email){
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username non valido");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password non valida");
        }
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
        this.username = username;
        this.passwordHash = createHash(password);
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }

    //attributi Utente
    private int idUtente;
    private String username;
    private String passwordHash;
    private String nome;
    private String cognome;
    private String email;

    //metodi Utente
    //metodo per la crittografia della password
    private String createHash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritmo di hashing non trovato", e);
        }
    }

    public int getIdUtente(){
        return idUtente;
    }

    public String getUsername() {
        return username;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return idUtente + " " + nome + " " + cognome  + " " + email;
    }
}
