package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderProductDao {

    public void insertOrderProduct(Connection conn, int orderId, int productId, int quantity, String sizeDbValue)
            throws SQLException {

        String sql = "INSERT INTO OrderProduct (orderId, productId, quantity, size) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);
            stmt.setString(4, sizeDbValue);
            stmt.executeUpdate();
        }
    }
}