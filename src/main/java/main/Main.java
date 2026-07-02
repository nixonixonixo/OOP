package main;

import controller.Controller;
import dao.*;
import gui.LoginFrame;
import implementazionePostgresDAO.*;
import javax.swing.*;

/**
 * Classe di avvio dell'applicativo.
 */
public class Main {
    /**
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