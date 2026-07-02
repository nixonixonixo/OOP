package model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Rappresenta l'entità base di un utente nel sistema.
 */
public class Utente {

    private int idUtente;
    private String username;
    private String passwordHash;
    private String nome;
    private String cognome;
    private String email;

    /**
     * Crea un nuovo utente con password da hashare.
     *
     * @param idUtente       l'ID univoco
     * @param username       lo username
     * @param passwordChiara la password in chiaro (verrà convertita in hash)
     * @param nome           il nome
     * @param cognome        il cognome
     * @param email          l'email
     */
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
     * Costruttore utilizzato dai DAO per istanziare un utente dal database.
     *
     * @param idUtente        l'ID univoco
     * @param username        lo username
     * @param passwordHash    la password già hashata
     * @param nome            il nome
     * @param cognome         il cognome
     * @param email           l'email
     * @param isAlreadyHashed flag per indicare se la stringa fornita è già un hash
     */
    public Utente(int idUtente, String username, String passwordHash, String nome, String cognome, String email, boolean isAlreadyHashed) {
        this.idUtente = idUtente;
        this.username = username;
        this.passwordHash = isAlreadyHashed ? passwordHash : generaHash(passwordHash);
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }

    /**
     * Verifica se la password fornita in chiaro corrisponde all'hash salvato.
     *
     * @param passwordChiara la password da verificare
     * @return true se la password è corretta, false altrimenti
     */
    public boolean verificaPassword(String passwordChiara) {
        if (passwordChiara == null) return false;
        String hashDaVerificare = generaHash(passwordChiara);
        return this.passwordHash.equals(hashDaVerificare);
    }

    /**
     * Genera l'hash SHA-256 della password fornita.
     *
     * @param password la password in chiaro
     * @return la password hashata in formato Base64
     */
    private String generaHash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritmo di hashing non disponibile", e);
        }
    }

    /**
     * Valida i campi obbligatori dell'utente.
     */
    private void validaCampi(String username, String password, String nome, String cognome, String email) {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username obbligatorio");
        if (password == null || password.length() < 4) throw new IllegalArgumentException("Password troppo corta");
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome obbligatorio");
        if (cognome == null || cognome.isBlank()) throw new IllegalArgumentException("Cognome obbligatorio");
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Email non valida");
    }

    // --- Getter e Setter ---

    public int getIdUtente() { return idUtente; }
    public void setIdUtente(int idUtente) { this.idUtente = idUtente; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }

    /**
     * Aggiorna la password dell'utente.
     * @param nuovaPasswordChiara la nuova password in chiaro
     */
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