package model;

import java.math.BigDecimal;

public class Pagamento {

    //costruttore Pagamento
    public Pagamento(int idPagamento, BigDecimal importo, StatoPagamento stato, Noleggio noleggio){
        if (importo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Importo non valido");
        }
        if (stato == null) {
            throw new IllegalArgumentException("Stato non valido");
        }
        this.idPagamento = idPagamento;
        this.importo = importo;
        this.stato = stato;
        this.noleggio = noleggio;
    }

    //enum Pagamento
    public enum StatoPagamento{
        IN_ATTESA,
        COMPLETATO,
        FALLITO,
        RIMBORSATO
    }

    //attributi Pagamento
    private int idPagamento;
    private BigDecimal importo;
    private StatoPagamento stato;

    //associazioni Pagamento
    private Noleggio noleggio;

    //metodi Pagamento
    public int getIdPagamento() {
        return idPagamento;
    }

    public BigDecimal getImporto() {
        return importo;
    }

    public StatoPagamento getStato() {
        return stato;
    }

    public Noleggio getNoleggio() {
        return noleggio;
    }

    public void setStato(StatoPagamento stato){
        this.stato = stato;
    }

    public void completa() {
        if (stato != StatoPagamento.IN_ATTESA) {
            throw new IllegalStateException("Pagamento non completabile");
        }
        stato = StatoPagamento.COMPLETATO;
    }

    public void fallisci() {
        if (stato != StatoPagamento.IN_ATTESA) {
            throw new IllegalStateException("Pagamento non fallibile");
        }
        stato = StatoPagamento.FALLITO;
    }

    public void rimborsa() {
        if (stato != StatoPagamento.COMPLETATO) {
            throw new IllegalStateException("Rimborso non possibile");
        }
        stato = StatoPagamento.RIMBORSATO;
    }

    @Override
    public String toString(){
        return idPagamento + " " + importo + " " + stato + " " + noleggio.getIdNoleggio();
    }
}
