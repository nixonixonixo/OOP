package model;

import java.math.BigDecimal;

public class Cliente extends Utente {

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

    //attributi Cliente
    private final String patente;
    private BigDecimal credito;

    //metodi Cliente
    public void ricaricaCredito(BigDecimal importo) {
        if (importo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Importo non valido");
        }
        credito = credito.add(importo);
    }

    public void scalaCredito(BigDecimal importo) {
        if (importo == null || importo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Importo non valido");
        }
        if (credito.compareTo(importo) < 0) {
            throw new IllegalArgumentException("Credito insufficiente");
        }
        credito = credito.subtract(importo);
    }

    public BigDecimal getCredito(){
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
