package com.example.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.example.model.DeliveryTicket;
import com.example.repositories.DatabaseConnection;

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

    public List<DeliveryTicket> getDeliveryTickets() {
        List<DeliveryTicket> tickets = new ArrayList<>();
        String sql = "SELECT o.id, d.firstName AS dFName, d.lastName AS dLName, " +
                     "v.type AS vType, c.firstName AS cFName, c.lastName AS cLName, " +
                     "o.orderDate, o.deliveryTime, p.name AS pName, p.basePrice " +
                     "FROM Orders o " +
                     "JOIN Drivers d ON o.driverId = d.id " +
                     "JOIN Vehicles v ON o.vehicleId = v.id " +
                     "JOIN Clients c ON o.clientId = c.id " +
                     "JOIN OrderProduct op ON o.id = op.orderId " +
                     "JOIN Products p ON op.productId = p.id " +
                     "ORDER BY o.orderDate DESC";

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                DeliveryTicket ticket = new DeliveryTicket();
                ticket.setOrderId(rs.getInt("id"));
                
                String driver = (rs.getString("dFName") != null ? rs.getString("dFName") : "") + " " + 
                                (rs.getString("dLName") != null ? rs.getString("dLName") : "");
                ticket.setDriverName(driver.trim());
                
                ticket.setVehicleType(rs.getString("vType"));
                
                String client = (rs.getString("cFName") != null ? rs.getString("cFName") : "") + " " + 
                                (rs.getString("cLName") != null ? rs.getString("cLName") : "");
                ticket.setClientName(client.trim());
                
                ticket.setOrderDate(rs.getTimestamp("orderDate"));
                ticket.setDeliveryTime(rs.getTimestamp("deliveryTime"));
                ticket.setProductName(rs.getString("pName"));
                ticket.setBasePrice(rs.getDouble("basePrice"));
                
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la lecture des fiches de livraison : " + e.getMessage());
        }
        return tickets;
    }

    public void deliverOrder(int orderId) {
        String sql = "{CALL mark_order_delivered(?)}";
        
        try (Connection conn = DatabaseConnection.getInstance();
             CallableStatement stmt = conn.prepareCall(sql)) {
             
            stmt.setInt(1, orderId);
            stmt.execute();
            
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la mise à jour de la livraison : " + e.getMessage());
        }
    }
}