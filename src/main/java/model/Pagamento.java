package model;

public class Pagamento {

    //costruttore Pagamento
    public Pagamento(int idPagamento, double importo, StatoPagamento stato){
        this.idPagamento = idPagamento;
        this.importo = importo;
        this.stato = stato;
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

    //metodi Pagamento
    @Override
    public String toString(){
        return super.toString() + " " + idPagamento + " " + importo + " " + stato;
    }
}
