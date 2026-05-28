package com.example.views;

import com.example.dao.ProductDao;
import com.example.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class MenuWindow extends JFrame {

    public MenuWindow() {
        setTitle("RaPizz");
        setSize(500, 300);
        setLocationRelativeTo(null); // permet de centrer la fenetre

        String[] colonnes = {"id", "type de pizza", "prix pizza"};
        DefaultTableModel model = new DefaultTableModel(colonnes, 0);

        ProductDao productDao = new ProductDao();
        List<Product> pizzas = productDao.getMenu();


        for (Product p : pizzas) {
            model.addRow(new Object[]{ p.getId(), p.getName(), p.getBasePrice() });
        }

        JTable table = new JTable(model);
        
        add(new JScrollPane(table)); 
    }
}