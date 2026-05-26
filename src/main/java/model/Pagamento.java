package model;

import java.math.BigDecimal;

/**
 * The type Pagamento.
 */
public class Pagamento {

    /**
     * Instantiates a new Pagamento.
     *
     * @param idPagamento the id pagamento
     * @param importo     the importo
     * @param stato       the stato
     * @param noleggio    the noleggio
     */
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

    /**
     * The enum Stato pagamento.
     */
//enum Pagamento
    public enum StatoPagamento{
        /**
         * In attesa stato pagamento.
         */
        IN_ATTESA,
        /**
         * Completato stato pagamento.
         */
        COMPLETATO,
        /**
         * Fallito stato pagamento.
         */
        FALLITO
    }

    //attributi Pagamento
    private int idPagamento;
    private BigDecimal importo;
    private StatoPagamento stato;

    //associazioni Pagamento
    private Noleggio noleggio;

    /**
     * Gets id pagamento.
     *
     * @return the id pagamento
     */
//metodi Pagamento
    public int getIdPagamento() {
        return idPagamento;
    }

    /**
     * Gets importo.
     *
     * @return the importo
     */
    public BigDecimal getImporto() {
        return importo;
    }

    /**
     * Gets stato.
     *
     * @return the stato
     */
    public StatoPagamento getStato() {
        return stato;
    }

    /**
     * Gets noleggio.
     *
     * @return the noleggio
     */
    public Noleggio getNoleggio() {
        return noleggio;
    }

    /**
     * Set stato.
     *
     * @param stato the stato
     */
    public void setStato(StatoPagamento stato){
        this.stato = stato;
    }

    /**
     * Completa.
     */
    public void completa() {
        if (stato != StatoPagamento.IN_ATTESA) {
            throw new IllegalStateException("Pagamento non completabile");
        }
        stato = StatoPagamento.COMPLETATO;
    }

    /**
     * Fallisci.
     */
    public void fallisci() {
        if (stato != StatoPagamento.IN_ATTESA) {
            throw new IllegalStateException("Pagamento non fallibile");
        }
        stato = StatoPagamento.FALLITO;
    }

    @Override
    public String toString(){
        return idPagamento + " " + importo + " " + stato + " " + noleggio.getIdNoleggio();
    }
}
