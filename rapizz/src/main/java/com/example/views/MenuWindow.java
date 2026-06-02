package com.example.views;

import com.example.dao.ProductDao;
import com.example.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class MenuWindow extends JFrame {

    public MenuWindow() {
        setTitle("RaPizz - Carte des Pizzas");
        setSize(750, 400); 
        setLocationRelativeTo(null); 

        String[] colonnes = {"ID", "Type de pizza", "Prix pizza", "Ingrédients"};
        DefaultTableModel model = new DefaultTableModel(colonnes, 0);

        ProductDao productDao = new ProductDao();
        List<Product> pizzas = productDao.getMenu();

        for (Product p : pizzas) {
            model.addRow(new Object[]{ 
                p.getId(), 
                p.getName(), 
                p.getBasePrice() + " €", 
                p.getIngredients()
            });
        }

        JTable table = new JTable(model);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Nom
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Prix
        table.getColumnModel().getColumn(3).setPreferredWidth(450); // Ingrédients

        add(new JScrollPane(table)); 
    }
}