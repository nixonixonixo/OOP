package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnessioneDatabase {

	private static ConnessioneDatabase instace;    //oggetto statico che evita problemi di concorrenza
	private static final String URL =
			"jdbc:postgresql://localhost:5432/noleggio_auto";

	private static final String USER = "postgres";

	private static final String PASSWORD = "password";

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
}