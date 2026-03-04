package com.example.team31project2;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * One-time database migration helper.
 *
 * @author team 31
 */
public class RunMigration {

    /**
     * Entry point for running the database migration.
     *
     * @param args command-line arguments (not used)
     * @return void
     * @throws SQLException if a database access error occurs
     */
    public static void main(String[] args) {
        String sql = "ALTER TABLE orderdetail ADD COLUMN IF NOT EXISTS notes TEXT;";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Migration successful: 'notes' column added to orderdetail.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}