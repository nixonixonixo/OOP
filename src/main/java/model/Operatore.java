package model;

/**
 * The type Operatore.
 */
public class Operatore  extends Utente{

    /**
     * The enum Ruolo.
     */
    //enum Operatore
    public enum Ruolo{
        /**
         * Admin ruolo.
         */
        ADMIN,
        /**
         * Addetto noleggio ruolo.
         */
        ADDETTO_NOLEGGIO,
        /**
         * Manutentore ruolo.
         */
        MANUTENTORE
    }

    /**
     * Instantiates a new Operatore.
     *
     * @param idUtente the id utente
     * @param username the username
     * @param password the password
     * @param nome     the nome
     * @param cognome  the cognome
     * @param email    the email
     * @param ruolo    the ruolo
     */
    //costruttore Operatore
    public Operatore(int idUtente, String username, String password, String nome, String cognome, String email,Ruolo ruolo){
        super(idUtente,username,password,nome,cognome,email);
        if (ruolo == null) {
            throw new IllegalArgumentException("Ruolo non valido");
        }
        this.ruolo = ruolo;
    }

    /**
     * Instantiates a new Operatore.
     *
     * @param idUtente        the id utente
     * @param username        the username
     * @param passwordHash    the password hash
     * @param nome            the nome
     * @param cognome         the cognome
     * @param email           the email
     * @param ruolo           the ruolo
     * @param isAlreadyHashed the is already hashed
     */
    //costruttore per DAO
    public Operatore(int idUtente, String username, String passwordHash, String nome, String cognome, String email, Ruolo ruolo, boolean isAlreadyHashed) {
        super(idUtente, username, passwordHash, nome, cognome, email, isAlreadyHashed);
        this.ruolo = ruolo;
    }

    //attributi Operatore
    private Ruolo ruolo;

    /**
     * Gets ruolo.
     *
     * @return the ruolo
     */
    //metodi Operatore
    public Ruolo getRuolo() {
        return ruolo;
    }

    /**
     * Sets ruolo.
     *
     * @param ruolo the ruolo
     */
    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }

    /**
     * Aggiorna stato auto.
     *
     * @param auto       the auto
     * @param nuovoStato the nuovo stato
     */
    public void aggiornaStatoAuto(Auto auto, Auto.StatoAuto nuovoStato) {
        if (auto == null || nuovoStato == null) {
            throw new IllegalArgumentException("Parametri non validi");
        }
        auto.cambiaStato(nuovoStato);
    }

    @Override
    public String toString(){
        return super.toString() + " " + ruolo;
    }
}
