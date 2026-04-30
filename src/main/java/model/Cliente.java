package model;

public class Cliente extends Utente {

    //costruttore Cliente
    public Cliente(int idUtente,String nome, String cognome, String email,String patente, double credito){
        super(idUtente,nome,cognome,email);
        if (patente == null || patente.isBlank()) {
            throw new IllegalArgumentException("Patente non valida");
        }
        if (credito < 0) {
            throw new IllegalArgumentException("Credito non valido");
        }
        this.patente=patente;
        this.credito=credito;
    }

    //attributi Cliente
    private String patente;
    private double credito;

    //metodi Cliente
    public void ricaricaCredito(double importo) {
        if (importo <= 0) {
            throw new IllegalArgumentException("Importo non valido");
        }
        credito += importo;
    }

    public void scalaCredito(double importo) {
        if (importo <= 0) {
            throw new IllegalArgumentException("Importo non valido");
        }
        if (credito < importo) {
            throw new IllegalArgumentException("Credito insufficiente");
        }
        credito -= importo;
    }

    public double getCredito(){
        return credito;
    }

    public String getPatente() {
        return patente;
    }

    @Override
    public String toString() {
        return super.toString() + " " + patente + " " + credito;
    }

}
