package com.example.views;

import com.example.model.Client;
import com.example.dao.ClientDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ClientWindow extends JFrame {

    public ClientWindow() {
        setTitle("RaPizz - Liste des Clients");
        setSize(700, 300);
        setLocationRelativeTo(null);

        String[] colonnes = {"id", "name", "lastname", "phone", "balance"};
        DefaultTableModel model = new DefaultTableModel(colonnes, 0);

        ClientDao dao = new ClientDao();
        List<Client> clients = dao.getClients();

        for (Client c : clients) {
            model.addRow(new Object[]{ 
                c.getId(), 
                c.getFirstName(), 
                c.getLastName(), 
                c.getPhoneNumber(), 
                c.getBalance() 
            });
        }

        JTable table = new JTable(model);
        add(new JScrollPane(table));
    }
}