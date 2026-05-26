package main;

import controller.Controller;
import dao.*;
import gui.LoginFrame;
import implementazionePostgresDAO.*;
import javax.swing.*;

/**
 * Classe di avvio dell'applicativo.
 * <p>
 * Questa classe rappresenta il punto di ingresso (entry point) del programma.
 * Si occupa di inizializzare lo strato di persistenza (DAO), istanziare il
 * {@link controller.Controller} principale e lanciare la finestra di autenticazione iniziale.
 */
public class Main {
    /**
     * Metodo main che avvia l'applicazione all'interno dell'Event Dispatch Thread (EDT).
     *
     * @param args argomenti passati dalla riga di comando (non utilizzati).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Controller controller = new Controller(
                    new ImpUtenteDAO(), new ImpClienteDAO(), new ImpOperatoreDAO(),
                    new ImpAutoDAO(), new ImpNoleggioDAO(), new ImpPagamentoDAO(), new ImpPrenotazioneDAO()
            );

            LoginFrame loginFrame = new LoginFrame(controller);
            loginFrame.setVisible(true);
        });
    }
}