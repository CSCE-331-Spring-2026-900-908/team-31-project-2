package com.example.team31project2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConnection {

    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    private static final String URL = dotenv.get("DB_URL", "jdbc:postgresql://localhost:5432/your_database_name");
    private static final String USER = dotenv.get("DB_USER", "your_username");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD", "your_password");

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("PostgreSQL JDBC Driver not found.");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
