package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestore della connessione al database PostgreSQL.
 * <p>
 * Fornisce un metodo statico per ottenere una {@link Connection} attiva
 * verso il database di sistema.
 */
public class ConnessioneDatabase {

	private static final String URL = "jdbc:postgresql://localhost:5432/noleggio_auto";
	private static final String USER = "postgres";
	private static final String PASSWORD = "password";

	/**
	 * Apre e restituisce una nuova connessione al database.
	 *
	 * @return un oggetto {@link Connection} verso il database
	 * @throws SQLException se la connessione al database fallisce
	 */
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
}