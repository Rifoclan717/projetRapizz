package com.example.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("RaPizz - Tableau de Bord");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLocationRelativeTo(null);
        
        setLayout(new GridLayout(4, 1, 10, 10));

        JLabel lblTitle = new JLabel("Gestion de la Pizzeria", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitle);

        JButton btnMenu = new JButton("Voir la carte des Pizzas");
        JButton btnClients = new JButton("Voir la liste des Clients");
        JButton btnAddClient = new JButton("Ajouter un nouveau Client");

        add(btnMenu);
        add(btnClients);
        add(btnAddClient);


        btnMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuWindow().setVisible(true);
            }
        });

        btnClients.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ClientWindow().setVisible(true);
            }
        });

        btnAddClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddClientWindow().setVisible(true);
            }
        });
    }
}