package edu.brown.cs.student.main.server.database;

import java.sql.*;

import io.github.cdimascio.dotenv.Dotenv;

public class PostgresConnection {
    // private final String endpoint;
    // private final String key;

    private String url;
    private String user;
    private String password;

    /**
     * Constructor for PostgresConnection. Initializes the database schema
     * 
     * @param refresh- boolean, will delete and recreate the entire schema if true
     */
    public PostgresConnection(boolean refresh) {
        Dotenv dotenv = Dotenv.load();
        this.url = "jdbc:postgresql://db.yhtpfpemutniykkpmgbb.supabase.co:5432/postgres";
        this.user = "postgres";
        this.password = dotenv.get("POSTGRES_PASSWORD");
        // this.key = dotenv.get("POSTGRES_PRIVATE_KEY");
        // this.endpoint = "https://yhtpfpemutniykkpmgbb.supabase.co/rest/v1";

        String query = "SELECT * FROM TranslationAPILimits";
        try (Connection conn = DriverManager.getConnection(url, user, password);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                // Retrieve data from each column using rs.getXXX methods
                String data = rs.getString("name");
                System.out.println(data);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
