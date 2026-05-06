package implementazionePostgresDAO;

import database.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EsempioImplementazionePostgresDAO implements EsempioDAO {

	private Connection connection;

	@Override
	public void esempioQuery() {

		String sql = "SELECT * FROM utente";

		try (PreparedStatement ps = connection.prepareStatement(sql)) {

			ps.executeQuery();

			System.out.println("Query eseguita con successo");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}