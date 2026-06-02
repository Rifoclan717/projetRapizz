package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.model.DeliveryGuy;
import com.example.repositories.DatabaseConnection;

public class DeliveryGuyDao {

    public List<DeliveryGuy> getDeliveryGuys() {

        List<DeliveryGuy> deliveryGuys = new ArrayList<>();

        String sql = "SELECT id, firstName, lastName, canBike, canDrive FROM Drivers";

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                DeliveryGuy deliveryGuy = new DeliveryGuy();

                deliveryGuy.setId(rs.getInt("id"));
                deliveryGuy.setFirstName(rs.getString("firstName"));
                deliveryGuy.setLastName(rs.getString("lastName"));

                deliveryGuy.setCanBike(rs.getBoolean("canBike"));
                deliveryGuy.setCanDrive(rs.getBoolean("canDrive"));

                deliveryGuys.add(deliveryGuy);
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la lecture des delivery guys : " + e.getMessage());
        }

        return deliveryGuys;
    }


}