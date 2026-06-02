package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.model.Vehicle;
import com.example.repositories.DatabaseConnection;

public class VehicleDao {

    public Vehicle getFreeVehicle() {
        String sql = "SELECT id, plateNumber, brand, model, type " +
                     "FROM Vehicles " +
                     "WHERE id NOT IN (SELECT vehicleId FROM Orders WHERE deliveryTime IS NULL) " +
                     "LIMIT 1";

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                Vehicle vehicle = new Vehicle();
                vehicle.setId(rs.getInt("id"));
                vehicle.setNumberPlate(rs.getString("plateNumber"));
                vehicle.setVehicleType(rs.getString("type"));
                return vehicle;
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la recherche de véhicule libre : " + e.getMessage());
        }

        return null; // Aucun véhicule libre
    }
}