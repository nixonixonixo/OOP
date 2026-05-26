package model;

import java.math.BigDecimal;

/**
 * The type Cliente.
 */
public class Cliente extends Utente {

    /**
     * Instantiates a new Cliente.
     *
     * @param idUtente the id utente
     * @param username the username
     * @param password the password
     * @param nome     the nome
     * @param cognome  the cognome
     * @param email    the email
     * @param patente  the patente
     * @param credito  the credito
     */
    //costruttore Cliente
    public Cliente(int idUtente, String username, String password, String nome, String cognome, String email,String patente, BigDecimal credito){
        super(idUtente,username,password,nome,cognome,email);
        if (patente == null || patente.isBlank()) {
            throw new IllegalArgumentException("Patente non valida");
        }
        if (credito.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Credito non valido");
        }
        this.patente=patente;
        this.credito=credito;
    }

    /**
     * Instantiates a new Cliente.
     *
     * @param idUtente        the id utente
     * @param username        the username
     * @param passwordHash    the password hash
     * @param nome            the nome
     * @param cognome         the cognome
     * @param email           the email
     * @param patente         the patente
     * @param credito         the credito
     * @param isAlreadyHashed parametro per capire se la password è già hashata
     */
    //costruttore per DAO
    public Cliente(int idUtente, String username, String passwordHash, String nome, String cognome, String email, String patente, BigDecimal credito, boolean isAlreadyHashed) {
        super(idUtente, username, passwordHash, nome, cognome, email, isAlreadyHashed);
        this.patente = patente;
        this.credito = credito;
    }

    //attributi Cliente
    private final String patente;
    private BigDecimal credito;

    /**
     * Ricarica credito.
     *
     * @param importo the importo
     */
    //metodi Cliente
    public void ricaricaCredito(BigDecimal importo) {
        if (importo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Importo non valido");
        }
        credito = credito.add(importo);
    }

    /**
     * Scala credito.
     *
     * @param importo the importo
     */
    public void scalaCredito(BigDecimal importo) {
        if (importo == null || importo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Importo non valido");
        }
        if (credito.compareTo(importo) < 0) {
            throw new IllegalArgumentException("Credito insufficiente");
        }
        credito = credito.subtract(importo);
    }

    /**
     * Get credito big decimal.
     *
     * @return the big decimal
     */
    public BigDecimal getCredito(){
        return credito;
    }

    /**
     * Gets patente.
     *
     * @return the patente
     */
    public String getPatente() {
        return patente;
    }

    /**
     * Set credito.
     *
     * @param credito the credito
     */
    public void setCredito(BigDecimal credito){
        this.credito = credito;
    }

    @Override
    public String toString() {
        return super.toString() + " " + patente + " " + credito;
    }

}
