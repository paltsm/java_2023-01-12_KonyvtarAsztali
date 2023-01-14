package com.example.konyvtarasztali;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KonyvDB {
	private Connection conn;
	public static String DB_DRIVER = "mysql";
	public static String DB_HOST = "localhost";
	public static String DB_PORT = "3306";
	public static String DB_DBNAME = "java";
	public static String DB_USERNAME = "root";
	public static String DB_PASSWORD = "";

	public KonyvDB() throws SQLException {
		String url = String.format("jdbc:%s://%s:%s/%s", DB_DRIVER, DB_HOST, DB_PORT, DB_DBNAME);
		this.conn = DriverManager.getConnection(url, DB_USERNAME, DB_PASSWORD);
	}

	public List<Konyv> konyvlistaz() throws SQLException {
		List<Konyv> konyvek = new ArrayList<>();
		String sql = "SELECT * FROM konyvtar";
		Statement stmt = conn.createStatement();
		ResultSet result = stmt.executeQuery(sql);
		while (result.next()) {
			int id = result.getInt("id");
			String title = result.getString("title");
			String author = result.getString("author");
			int publish_year = result.getInt("publish_year");
			int page_count = result.getInt("page_count");

			Konyv konyv = new Konyv(id, title, author, publish_year, page_count);
			konyvek.add(konyv);
		}
		return konyvek;
	}

	public boolean konyvTorlese(Konyv konyv) throws SQLException {
		String sql = "DELETE FROM konyvtar WHERE id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, konyv.getId());
		return stmt.executeUpdate() > 0;
	}
}