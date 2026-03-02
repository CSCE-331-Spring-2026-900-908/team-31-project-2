package com.example.team31project2;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * One-time migration helper. Run this class directly to add the notes column.
 * Safe to run multiple times (uses IF NOT EXISTS).
 */
public class RunMigration {
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
