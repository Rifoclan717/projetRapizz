package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Product;
import com.example.repositories.DatabaseConnection;

public class ProductDao {
    


  public List<Product> getMenu() {
        List<Product> products = new ArrayList<>();

        String sql = "SELECT p.id, p.name, p.basePrice, GROUP_CONCAT(i.name SEPARATOR ', ') AS ingredientList " +
                     "FROM Products p " +
                     "LEFT JOIN ProductIngredient pi ON p.id = pi.productId " + 
                     "LEFT JOIN Ingredients i ON pi.ingredientId = i.id " +
                     "GROUP BY p.id, p.name, p.basePrice";

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setBasePrice(rs.getDouble("basePrice"));
                
                String listeIngredients = rs.getString("ingredientList");
                product.setIngredients(listeIngredients != null ? listeIngredients : "Aucun ingrédient");

                products.add(product);
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la lecture du menu : " + e.getMessage());
        }

        return products;
    }
}