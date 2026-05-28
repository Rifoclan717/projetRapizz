package com.example.dao;


import com.example.model.Client;
import com.example.repositories.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDao {

    public List<Client> getClients() {
        List<Client> clients = new ArrayList<>();
        
       
        String sql = "SELECT id, email, phoneNumber, firstName, lastName, address, balance FROM clients";

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Client client = new Client();
                
                client.setId(rs.getInt("id"));
                client.setEmail(rs.getString("email"));
                client.setPhoneNumber(rs.getString("phoneNumber"));
                client.setFirstName(rs.getString("firstName"));
                client.setLastName(rs.getString("lastName"));
                client.setAddress(rs.getString("address"));
                client.setBalance(rs.getDouble("balance")); 

                clients.add(client);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la lecture des clients : " + e.getMessage());
        }
        return clients;
    }


    public boolean add(Client client) {
        String sql = "INSERT INTO Clients (email, phoneNumber, firstName, lastName, address, password_hash, balance) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, client.getEmail());
            stmt.setString(2, client.getPhoneNumber());
            stmt.setString(3, client.getFirstName());
            stmt.setString(4, client.getLastName());
            stmt.setString(5, client.getAddress());
            stmt.setString(6, "password123");
            stmt.setDouble(7, client.getBalance());
            
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'ajout : " + e.getMessage());
            return false;
        }
    }

}