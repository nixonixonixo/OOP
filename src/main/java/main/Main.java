package main;

import controller.Controller;
import dao.*;
import gui.LoginFrame;
import implementazionePostgresDAO.*;
import javax.swing.*;

/**
 * The type Main.
 */
public class Main {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
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