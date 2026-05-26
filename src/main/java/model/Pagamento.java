package model;

import java.math.BigDecimal;

/**
 * Rappresenta una transazione di pagamento associata a un contratto di {@link Noleggio}.
 * Gestisce l'importo della transazione e il suo ciclo di vita (Stato).
 */
public class Pagamento {

    /**
     * Definisce i possibili stati in cui un pagamento può trovarsi.
     */
    public enum StatoPagamento {
        /** La transazione è in attesa di elaborazione. */
        IN_ATTESA,
        /** La transazione è stata conclusa con successo. */
        COMPLETATO,
        /** La transazione non è andata a buon fine. */
        FALLITO
    }

    private int idPagamento;
    private BigDecimal importo;
    private StatoPagamento stato;
    private Noleggio noleggio;

    /**
     * Crea una nuova istanza di Pagamento.
     *
     * @param idPagamento l'ID univoco del pagamento
     * @param importo     l'importo della transazione
     * @param stato       lo {@link StatoPagamento} iniziale
     * @param noleggio    il {@link Noleggio} a cui il pagamento si riferisce
     * @throws IllegalArgumentException se l'importo è non valido o lo stato è nullo
     */
    public Pagamento(int idPagamento, BigDecimal importo, StatoPagamento stato, Noleggio noleggio) {
        if (importo == null || importo.compareTo(BigDecimal.ZERO) <= 0) {
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
     * Restituisce l'ID del pagamento.
     * @return l'ID univoco
     */
    public int getIdPagamento() {
        return idPagamento;
    }

    /**
     * Restituisce l'importo del pagamento.
     * @return l'importo come {@link BigDecimal}
     */
    public BigDecimal getImporto() {
        return importo;
    }

    /**
     * Restituisce lo stato attuale del pagamento.
     * @return lo {@link StatoPagamento} corrente
     */
    public StatoPagamento getStato() {
        return stato;
    }

    /**
     * Restituisce il noleggio associato a questo pagamento.
     * @return l'oggetto {@link Noleggio}
     */
    public Noleggio getNoleggio() {
        return noleggio;
    }

    /**
     * Imposta lo stato del pagamento.
     * @param stato il nuovo {@link StatoPagamento}
     */
    public void setStato(StatoPagamento stato) {
        this.stato = stato;
    }

    /**
     * Imposta lo stato del pagamento a {@link StatoPagamento#COMPLETATO}.
     *
     * @throws IllegalStateException se il pagamento non è attualmente in stato IN_ATTESA
     */
    public void completa() {
        if (stato != StatoPagamento.IN_ATTESA) {
            throw new IllegalStateException("Pagamento non completabile");
        }
        stato = StatoPagamento.COMPLETATO;
    }

    /**
     * Imposta lo stato del pagamento a {@link StatoPagamento#FALLITO}.
     *
     * @throws IllegalStateException se il pagamento non è attualmente in stato IN_ATTESA
     */
    public void fallisci() {
        if (stato != StatoPagamento.IN_ATTESA) {
            throw new IllegalStateException("Pagamento non fallibile");
        }
        stato = StatoPagamento.FALLITO;
    }

    @Override
    public String toString() {
        return "Pagamento #" + idPagamento + " | Importo: " + importo +
                " | Stato: " + stato + " | Noleggio ID: " + noleggio.getIdNoleggio();
    }
}