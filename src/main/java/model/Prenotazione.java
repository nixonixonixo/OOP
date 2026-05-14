package model;

import java.util.Date;

public class Prenotazione {

    public enum StatoPren {
        IN_ATTESA,
        CONFERMATA,
        ANNULLATA
    }

    private int idPrenotazione;
    private Date dataInizio;
    private Date dataFine;
    private StatoPren stato;
    private Cliente cliente;
    private Auto auto;

    // Costruttore aggiornato: rimosso il vincolo null su dataFine
    public Prenotazione(int idPrenotazione, Date dataInizio, Date dataFine, StatoPren stato, Cliente cliente, Auto auto) {
        if (dataInizio == null || stato == null) {
            throw new IllegalArgumentException("Parametri obbligatori mancanti");
        }

        // Controllo date solo se dataFine è presente
        if (dataFine != null && dataFine.before(dataInizio)) {
            throw new IllegalArgumentException("La data di fine non può essere precedente all'inizio");
        }

        this.idPrenotazione = idPrenotazione;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.stato = stato;
        this.cliente = cliente;
        this.auto = auto;
    }

    // Costruttore vuoto (opzionale, utile per i form della GUI)
    public Prenotazione() {}

    // --- GETTER ---
    public int getIdPrenotazione() { return idPrenotazione; }
    public Date getDataInizio() { return dataInizio; }
    public Date getDataFine() { return dataFine; }
    public StatoPren getStato() { return stato; }
    public Cliente getCliente() { return cliente; }
    public Auto getAuto() { return auto; }

    // --- SETTER (Aggiunti e aggiornati) ---
    public void setAuto(Auto auto) {
        this.auto = auto;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setDataInizio(Date dataInizio) {
        this.dataInizio = dataInizio;
    }

    public void setDataFine(Date dataFine) {
        this.dataFine = dataFine;
    }

    public void setStato(StatoPren stato) {
        this.stato = stato;
    }

    public void setIdPrenotazione(int idPrenotazione) {
        this.idPrenotazione = idPrenotazione;
    }

    // --- METODI DI LOGICA ---
    public void conferma() {
        if (stato == StatoPren.ANNULLATA) {
            throw new IllegalStateException("Prenotazione già annullata");
        }
        this.stato = StatoPren.CONFERMATA;
    }

    public void annulla() {
        this.stato = StatoPren.ANNULLATA;
    }

    public boolean isSovrapposta(Prenotazione altra) {
        if (altra == null || this.dataFine == null || altra.dataFine == null) return false;
        return this.dataInizio.before(altra.dataFine) &&
                this.dataFine.after(altra.dataInizio);
    }

    @Override
    public String toString() {
        return idPrenotazione + " " + dataInizio + " " + dataFine  + " " + stato + " " + cliente.getCognome()
                + " " + auto.getTarga();
    }
}
