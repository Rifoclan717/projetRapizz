package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OrderDao {

    public int insertOrder(Connection conn, int clientId, int driverId, int vehicleId) throws SQLException {
        String sql = "INSERT INTO Orders (deliveryTime, orderDate, tenthOfGift, clientId, driverId, vehicleId) "
                  + "VALUES (?, NOW(), ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setObject(1, null);      // deliveryTime NULL au départ
            stmt.setBoolean(2, false);    // tenthOfGift
            stmt.setInt(3, clientId);
            stmt.setInt(4, driverId);
            stmt.setInt(5, vehicleId);

            int rows = stmt.executeUpdate();
            if (rows == 0) throw new SQLException("Insertion Orders a échoué (0 ligne).");

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
                throw new SQLException("Insertion Orders OK mais aucun id retourné.");
            }
        }
    }
}