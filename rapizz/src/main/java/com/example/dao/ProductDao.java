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

                String sql = "SELECT id, name, basePrice FROM Products";

                try (Connection conn = DatabaseConnection.getInstance();
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {

                        Product product = new Product();

                        product.setId(rs.getInt("id"));
                        product.setName(rs.getString("name"));
                        product.setBasePrice(rs.getDouble("basePrice"));
                        products.add(product);
                    }

                } catch (SQLException e) {
                    System.err.println("Erreur SQL lors de la lecture des delivery guys : " + e.getMessage());
                }

                return products;
            }
}
