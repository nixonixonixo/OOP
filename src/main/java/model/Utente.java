package model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * The type Utente.
 */
public class Utente {

    //attributi Utente
    private int idUtente;
    private String username;
    private String passwordHash;
    private String nome;
    private String cognome;
    private String email;

    /**
     * Instantiates a new Utente.
     *
     * @param idUtente       the id utente
     * @param username       the username
     * @param passwordChiara the password chiara
     * @param nome           the nome
     * @param cognome        the cognome
     * @param email          the email
     */
    //costruttore Utente
    public Utente(int idUtente, String username, String passwordChiara, String nome, String cognome, String email) {
        validaCampi(username, passwordChiara, nome, cognome, email);
        this.idUtente = idUtente;
        this.username = username;
        this.passwordHash = generaHash(passwordChiara);
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }

    /**
     * Instantiates a new Utente.
     *
     * @param idUtente        the id utente
     * @param username        the username
     * @param passwordHash    the password hash
     * @param nome            the nome
     * @param cognome         the cognome
     * @param email           the email
     * @param isAlreadyHashed parametro per capire se la password è già hashata
     */
    //costruttore DAO
    public Utente(int idUtente, String username, String passwordHash, String nome, String cognome, String email, boolean isAlreadyHashed) {
        this.idUtente = idUtente;
        this.username = username;
        this.passwordHash = isAlreadyHashed ? passwordHash : generaHash(passwordHash);
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }

    /**
     * Verifica password boolean.
     *
     * @param passwordChiara the password chiara
     * @return the boolean
     */
    //metodi di logica
    public boolean verificaPassword(String passwordChiara) {
        if (passwordChiara == null) return false;
        String hashDaVerificare = generaHash(passwordChiara);
        return this.passwordHash.equals(hashDaVerificare);
    }

    //metodo per hashing con algoritmo SHA-256
    private String generaHash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritmo di hashing non disponibile", e);
        }
    }

    private void validaCampi(String username, String password, String nome, String cognome, String email) {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username obbligatorio");
        if (password == null || password.length() < 4) throw new IllegalArgumentException("Password troppo corta");
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome obbligatorio");
        if (cognome == null || cognome.isBlank()) throw new IllegalArgumentException("Cognome obbligatorio");
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Email non valida");
    }

    /**
     * Gets id utente.
     *
     * @return the id utente
     */
    //metodi Utente
    public int getIdUtente() { return idUtente; }

    /**
     * Sets id utente.
     *
     * @param idUtente the id utente
     */
    public void setIdUtente(int idUtente) { this.idUtente = idUtente; }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() { return username; }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Gets password hash.
     *
     * @return the password hash
     */
    public String getPasswordHash() { return passwordHash; }

    /**
     * Cambia password.
     *
     * @param nuovaPasswordChiara the nuova password chiara
     */
    public void cambiaPassword(String nuovaPasswordChiara) {
        this.passwordHash = generaHash(nuovaPasswordChiara);
    }

    /**
     * Gets nome.
     *
     * @return the nome
     */
    public String getNome() { return nome; }

    /**
     * Sets nome.
     *
     * @param nome the nome
     */
    public void setNome(String nome) { this.nome = nome; }

    /**
     * Gets cognome.
     *
     * @return the cognome
     */
    public String getCognome() { return cognome; }

    /**
     * Sets cognome.
     *
     * @param cognome the cognome
     */
    public void setCognome(String cognome) { this.cognome = cognome; }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() { return email; }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return String.format("%s %s (%s)", nome, cognome, username);
    }
}