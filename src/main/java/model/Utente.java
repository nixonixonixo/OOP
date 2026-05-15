package model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Utente {

    private int idUtente;
    private String username;
    private String passwordHash;
    private String nome;
    private String cognome;
    private String email;

    /**
     * COSTRUTTORE 1: Registrazione / Nuovo Utente
     * Si usa quando un utente inserisce una password in chiaro.
     * La password viene hashata immediatamente.
     */
    public Utente(int idUtente, String username, String passwordChiara, String nome, String cognome, String email) {
        validaCampi(username, passwordChiara, nome, cognome, email);
        this.idUtente = idUtente;
        this.username = username;
        this.passwordHash = generaHash(passwordChiara); // Hash della password in chiaro
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }

    /**
     * COSTRUTTORE 2: Caricamento da Database (DAO)
     * Si usa quando i dati arrivano dal DB (dove la password è già hashata).
     * Il flag 'isAlreadyHashed' serve a distinguere i due costruttori.
     */
    public Utente(int idUtente, String username, String passwordHash, String nome, String cognome, String email, boolean isAlreadyHashed) {
        this.idUtente = idUtente;
        this.username = username;
        // Se isAlreadyHashed è true, assegniamo direttamente l'hash senza ricalcolarlo
        this.passwordHash = isAlreadyHashed ? passwordHash : generaHash(passwordHash);
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }

    // --- LOGICA DI SICUREZZA ---

    /**
     * Confronta una password in chiaro con l'hash memorizzato.
     */
    public boolean verificaPassword(String passwordChiara) {
        if (passwordChiara == null) return false;
        String hashDaVerificare = generaHash(passwordChiara);
        return this.passwordHash.equals(hashDaVerificare);
    }

    /**
     * Algoritmo SHA-256 per la creazione dell'hash.
     */
    private String generaHash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Errore critico: Algoritmo di hashing non disponibile", e);
        }
    }

    // --- VALIDAZIONE ---

    private void validaCampi(String username, String password, String nome, String cognome, String email) {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username obbligatorio");
        if (password == null || password.length() < 4) throw new IllegalArgumentException("Password troppo corta");
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome obbligatorio");
        if (cognome == null || cognome.isBlank()) throw new IllegalArgumentException("Cognome obbligatorio");
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Email non valida");
    }

    // --- GETTER E SETTER ---

    public int getIdUtente() { return idUtente; }
    public void setIdUtente(int idUtente) { this.idUtente = idUtente; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    // Nota: Non forniamo un setter semplice per la password per evitare errori,
    // preferiamo un metodo dedicato se l'utente vuole cambiare password.
    public void cambiaPassword(String nuovaPasswordChiara) {
        this.passwordHash = generaHash(nuovaPasswordChiara);
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return String.format("%s %s (%s)", nome, cognome, username);
    }
}