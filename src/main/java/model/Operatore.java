package model;

public class Operatore  extends Utente{

    //enum Operatore
    public enum Ruolo{
        ADMIN,
        ADDETTO_NOLEGGIO,
        MANUTENTORE
    }

    //costruttore Operatore
    public Operatore(int idUtente, String nome,String cognome, String email,Ruolo ruolo){
        super(idUtente,nome,cognome,email);
        if (ruolo == null) {
            throw new IllegalArgumentException("Ruolo non valido");
        }
        this.ruolo = ruolo;
    }

    //attributi Operatore
    private Ruolo ruolo;

    //metodi Operatore
    public Ruolo getRuolo() {
        return ruolo;
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }

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
