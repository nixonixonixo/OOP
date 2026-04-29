# Sistema di Noleggio Auto

## Descrizione
Questo progetto consiste nello sviluppo di un sistema informativo per la gestione del noleggio di automobili.

L’applicazione permette ai clienti di:
- prenotare un’auto per un intervallo di tempo
- avviare un noleggio
- effettuare pagamenti

Il sistema gestisce inoltre la disponibilità dei veicoli e garantisce il rispetto dei vincoli di business.

## Obiettivi
- Modellare un dominio reale (autonoleggio)
- Progettare un database relazionale in PostgreSQL
- Sviluppare un’applicazione Java secondo il pattern BCE + DAO
- Garantire la coerenza dei dati tramite vincoli e trigger

## Modello del dominio

Le principali entità del sistema sono:
- Utente
- Cliente
- Operatore
- Auto
- Prenotazione
- Noleggio
- Pagamento

### Relazioni principali
- Un cliente può effettuare più prenotazioni
- Un’auto può essere prenotata più volte in tempi diversi
- Una prenotazione genera un noleggio
- Un noleggio può avere uno o più pagamenti

## Tecnologie utilizzate
- Java
- PostgreSQL
- JDBC
- Maven

## Vincoli e regole di business
Il sistema implementa diversi vincoli, tra cui:
- nessuna sovrapposizione di prenotazioni per la stessa auto
- disponibilità dell’auto al momento della prenotazione
- calcolo automatico del costo del noleggio
- aggiornamento dello stato dell’auto

## Gruppo di lavoro
- Nicola Ragozzini (DE1000042)
- Gaetano Russo (DE1000051)

## Repository
https://github.com/nixonixonixo/OOP
