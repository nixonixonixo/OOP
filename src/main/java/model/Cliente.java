package model;

import java.math.BigDecimal;

/**
 * Rappresenta un cliente del sistema.
 * Estende la classe {@link Utente} aggiungendo informazioni specifiche come
 * la patente di guida e il saldo del credito disponibile.
 */
public class Cliente extends Utente {

    private final String patente;
    private BigDecimal credito;

    /**
     * Crea una nuova istanza di Cliente.
     *
     * @param idUtente l'ID dell'utente
     * @param username lo username
     * @param password la password in chiaro (verrà processata dal sistema)
     * @param nome     il nome
     * @param cognome  il cognome
     * @param email    l'email
     * @param patente  il numero di patente
     * @param credito  il credito iniziale
     * @throws IllegalArgumentException se la patente è nulla/vuota o il credito è negativo
     */
    public Cliente(int idUtente, String username, String password, String nome, String cognome, String email, String patente, BigDecimal credito) {
        super(idUtente, username, password, nome, cognome, email);
        if (patente == null || patente.isBlank()) {
            throw new IllegalArgumentException("Patente non valida");
        }
        if (credito == null || credito.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Credito non valido");
        }
        this.patente = patente;
        this.credito = credito;
    }

    /**
     * Costruttore utilizzato dai DAO per istanziare un cliente recuperato dal database.
     *
     * @param idUtente        l'ID dell'utente
     * @param username        lo username
     * @param passwordHash    la password già hashata
     * @param nome            il nome
     * @param cognome         il cognome
     * @param email           l'email
     * @param patente         il numero di patente
     * @param credito         il credito corrente
     * @param isAlreadyHashed flag che indica che la password è già stata hashata
     */
    public Cliente(int idUtente, String username, String passwordHash, String nome, String cognome, String email, String patente, BigDecimal credito, boolean isAlreadyHashed) {
        super(idUtente, username, passwordHash, nome, cognome, email, isAlreadyHashed);
        this.patente = patente;
        this.credito = credito;
    }

    /**
     * Incrementa il credito del cliente.
     *
     * @param importo l'importo da aggiungere
     * @throws IllegalArgumentException se l'importo è minore o uguale a zero
     */
    public void ricaricaCredito(BigDecimal importo) {
        if (importo == null || importo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Importo non valido");
        }
        credito = credito.add(importo);
    }

    /**
     * Detrae un importo dal credito del cliente.
     *
     * @param importo l'importo da scalare
     * @throws IllegalArgumentException se l'importo è non valido o il credito è insufficiente
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
     * Restituisce il credito corrente del cliente.
     *
     * @return il saldo attuale come {@link BigDecimal}
     */
    public BigDecimal getCredito() {
        return credito;
    }

    /**
     * Restituisce il numero di patente del cliente.
     *
     * @return la patente
     */
    public String getPatente() {
        return patente;
    }

    /**
     * Imposta manualmente il credito del cliente.
     *
     * @param credito il nuovo valore del saldo
     */
    public void setCredito(BigDecimal credito) {
        this.credito = credito;
    }

    @Override
    public String toString() {
        return super.toString() + " | Patente: " + patente + " | Credito: " + credito;
    }
}