package com.mycompany.dealornodeal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/dealornodeal";
    private static final String USER = "localhost"; // Update with actual MySQL username
    private static final String PASSWORD = "12345678910"; // Update with actual password

    public static void saveGameResult(String playerName, int amountWon) {
        String query = "INSERT INTO game_results (player_name, amount_won) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, playerName);
            stmt.setInt(2, amountWon);
            stmt.executeUpdate();
            System.out.println("Data inserted successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
