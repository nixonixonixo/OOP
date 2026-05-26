package model;

/**
 * Rappresenta un operatore del sistema di noleggio.
 * Estende {@link Utente} e definisce il ruolo specifico ricoperto
 * all'interno dell'organizzazione (es. Admin, Addetto Noleggio, Manutentore).
 */
public class Operatore extends Utente {

    /**
     * Definisce i possibili ruoli operativi all'interno del sistema.
     */
    public enum Ruolo {
        /** Amministratore con privilegi elevati. */
        ADMIN,
        /** Operatore dedicato alla gestione delle pratiche di noleggio. */
        ADDETTO_NOLEGGIO,
        /** Operatore responsabile della manutenzione dei veicoli. */
        MANUTENTORE
    }

    private Ruolo ruolo;

    /**
     * Crea una nuova istanza di Operatore.
     *
     * @param idUtente l'ID dell'utente
     * @param username lo username
     * @param password la password in chiaro
     * @param nome     il nome
     * @param cognome  il cognome
     * @param email    l'email
     * @param ruolo    il {@link Ruolo} assegnato
     * @throws IllegalArgumentException se il ruolo fornito è nullo
     */
    public Operatore(int idUtente, String username, String password, String nome, String cognome, String email, Ruolo ruolo) {
        super(idUtente, username, password, nome, cognome, email);
        if (ruolo == null) {
            throw new IllegalArgumentException("Ruolo non valido");
        }
        this.ruolo = ruolo;
    }

    /**
     * Costruttore utilizzato dai DAO per istanziare un operatore recuperato dal database.
     *
     * @param idUtente        l'ID dell'utente
     * @param username        lo username
     * @param passwordHash    la password già hashata
     * @param nome            il nome
     * @param cognome         il cognome
     * @param email           l'email
     * @param ruolo           il {@link Ruolo} assegnato
     * @param isAlreadyHashed flag per indicare che la password è già stata processata
     */
    public Operatore(int idUtente, String username, String passwordHash, String nome, String cognome, String email, Ruolo ruolo, boolean isAlreadyHashed) {
        super(idUtente, username, passwordHash, nome, cognome, email, isAlreadyHashed);
        this.ruolo = ruolo;
    }

    /**
     * Restituisce il ruolo dell'operatore.
     *
     * @return il {@link Ruolo} corrente
     */
    public Ruolo getRuolo() {
        return ruolo;
    }

    /**
     * Modifica il ruolo dell'operatore.
     *
     * @param ruolo il nuovo {@link Ruolo} da assegnare
     */
    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }

    /**
     * Aggiorna lo stato di un'auto nel sistema.
     *
     * @param auto       l'oggetto {@link Auto} da modificare
     * @param nuovoStato il nuovo {@link Auto.StatoAuto} da impostare
     * @throws IllegalArgumentException se l'auto o lo stato fornito sono nulli
     */
    public void aggiornaStatoAuto(Auto auto, Auto.StatoAuto nuovoStato) {
        if (auto == null || nuovoStato == null) {
            throw new IllegalArgumentException("Parametri non validi");
        }
        auto.cambiaStato(nuovoStato);
    }

    @Override
    public String toString() {
        return super.toString() + " | Ruolo: " + ruolo;
    }
}