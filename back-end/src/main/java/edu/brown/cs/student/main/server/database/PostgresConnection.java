package edu.brown.cs.student.main.server.database;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;

import io.github.cdimascio.dotenv.Dotenv;

public class PostgresConnection {
    // private final String endpoint;
    // private final String key;

    private String url;
    private String user;
    private String password;

    /**
     * Constructor for PostgresConnection. Initializes the database schema
     */
    public PostgresConnection() {
        Dotenv dotenv = Dotenv.load();
        this.url = "jdbc:postgresql://db.yhtpfpemutniykkpmgbb.supabase.co:5432/postgres";
        this.user = "postgres";
        this.password = dotenv.get("POSTGRES_PASSWORD");
        // this.key = dotenv.get("POSTGRES_PRIVATE_KEY");
        // this.endpoint = "https://yhtpfpemutniykkpmgbb.supabase.co/rest/v1";
    }

    public HashSet<HashMap<String, Object>> getTranslationLimitTable() throws SQLException {
        String query = "SELECT * FROM \"TranslationAPILimits\"";
        Connection conn = DriverManager.getConnection(url, user, password);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        HashSet<HashMap<String, Object>> result = new HashSet<>();
        while (rs.next()) {
            HashMap<String, Object> tuple = new HashMap<>();
            tuple.put("name", rs.getString("name"));
            tuple.put("max_count", rs.getInt("max_count"));
            tuple.put("current_count", rs.getInt("current_count"));
            tuple.put("reset_date", rs.getDate("reset_date"));
            result.add(tuple);
        }
        return result;
    }

    public void incrementTranslationLimit(int increment, String name) throws SQLException {
        String query = "UPDATE \"TranslationAPILimits\" SET current_count = current_count + ? WHERE name = ?";
        Connection conn = DriverManager.getConnection(url, user, password);
        PreparedStatement pstmt = conn.prepareStatement(query);

        pstmt.setInt(1, increment);
        pstmt.setString(2, name);
        pstmt.executeUpdate();
    }
}