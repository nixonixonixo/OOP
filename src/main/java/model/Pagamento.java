package model;

public class Pagamento {

    //costruttore Pagamento
    public Pagamento(int idPagamento, double importo, StatoPagamento stato, Noleggio noleggio){
        if (importo <= 0) {
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
    private double importo;
    private StatoPagamento stato;

    //associazioni Pagamento
    private Noleggio noleggio;

    //metodi Pagamento
    public int getIdPagamento() {
        return idPagamento;
    }

    public double getImporto() {
        return importo;
    }

    public StatoPagamento getStato() {
        return stato;
    }

    public Noleggio getNoleggio() {
        return noleggio;
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
