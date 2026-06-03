package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.repositories.DatabaseConnection;

public class StatsDao {

    public List<String> getUnusedVehicles() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT plateNumber, type FROM Vehicles WHERE id NOT IN (SELECT vehicleId FROM Orders)";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("plateNumber") + " (" + rs.getString("type") + ")");
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        return list;
    }

    public List<String> getOrdersPerClient() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT c.firstName, c.lastName, COUNT(o.id) as total " +
                     "FROM Clients c LEFT JOIN Orders o ON c.id = o.clientId " +
                     "GROUP BY c.id ORDER BY total DESC";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("firstName") + " " + rs.getString("lastName") + " : " + rs.getInt("total"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        return list;
    }

    public double getAverageOrders() {
        double avg = 0;
        String sql = "SELECT AVG(nb) as moyenne FROM (SELECT COUNT(o.id) as nb FROM Clients c LEFT JOIN Orders o ON c.id = o.clientId GROUP BY c.id) as sub";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                avg = rs.getDouble("moyenne");
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        return avg;
    }

    public List<String> getClientsAboveAverage() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT c.firstName, c.lastName, COUNT(o.id) as nb " +
                     "FROM Clients c LEFT JOIN Orders o ON c.id = o.clientId " +
                     "GROUP BY c.id HAVING nb > (" +
                     "SELECT AVG(nb2) FROM (SELECT COUNT(o2.id) as nb2 FROM Clients c2 LEFT JOIN Orders o2 ON c2.id = o2.clientId GROUP BY c2.id) as sub" +
                     ")";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("firstName") + " " + rs.getString("lastName") + " (" + rs.getInt("nb") + ")");
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        return list;
    }

    public String getBestClient() {
        String best = "Aucun";
        String sql = "SELECT c.firstName, c.lastName, COUNT(o.id) as total " +
                     "FROM Clients c JOIN Orders o ON c.id = o.clientId " +
                     "GROUP BY c.id ORDER BY total DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                best = rs.getString("firstName") + " " + rs.getString("lastName") + " (" + rs.getInt("total") + " commandes)";
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        return best;
    }

    public String getMostDemandedPizza() {
        String pizza = "Aucune";
        String sql = "SELECT p.name, SUM(op.quantity) as q FROM Products p " +
                     "JOIN OrderProduct op ON p.id = op.productId " +
                     "GROUP BY p.id ORDER BY q DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                pizza = rs.getString("name") + " (" + rs.getInt("q") + " fois)";
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        return pizza;
    }

    public String getLeastDemandedPizza() {
        String pizza = "Aucune";
        String sql = "SELECT p.name, SUM(op.quantity) as q FROM Products p " +
                     "JOIN OrderProduct op ON p.id = op.productId " +
                     "GROUP BY p.id ORDER BY q ASC LIMIT 1";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                pizza = rs.getString("name") + " (" + rs.getInt("q") + " fois)";
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        return pizza;
    }

    public String getFavoriteIngredient() {
        String ingredient = "Aucun";
        String sql = "SELECT i.name, SUM(op.quantity) as q FROM Ingredients i " +
                     "JOIN ProductIngredient pi ON i.id = pi.ingredientId " +
                     "JOIN OrderProduct op ON pi.productId = op.productId " +
                     "GROUP BY i.id ORDER BY q DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                ingredient = rs.getString("name") + " (présent dans " + rs.getInt("q") + " pizzas commandées)";
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        return ingredient;
    }

    public double getTotalRevenue() {
        double total = 0;
        String sql = "SELECT SUM(p.basePrice * op.quantity * " +
                     "CASE op.size WHEN 'naine' THEN 0.6667 WHEN 'ogresse' THEN 1.3333 ELSE 1 END) as CA " +
                     "FROM OrderProduct op " +
                     "JOIN Products p ON op.productId = p.id " +
                     "JOIN Orders o ON op.orderId = o.id " +
                     "WHERE o.tenthOfGift = FALSE " +
                     "AND (o.deliveryTime IS NULL OR TIMESTAMPDIFF(MINUTE, o.orderDate, o.deliveryTime) <= 30)";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                total = rs.getDouble("CA");
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        return total;
    }
}