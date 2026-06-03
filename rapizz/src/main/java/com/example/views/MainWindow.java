package com.example.views;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("RaPizz - Tableau de Bord");
        setSize(400, 400); // Taille augmentée pour le nouveau bouton
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLocationRelativeTo(null);
        
        setLayout(new GridLayout(8, 1, 10, 10)); // 8 lignes au lieu de 5

        JLabel lblTitle = new JLabel("Gestion de la Pizzeria", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitle);

        JButton btnMenu = new JButton("Voir la carte des Pizzas");
        JButton btnClients = new JButton("Voir la liste des Clients");
        JButton btnAddClient = new JButton("Ajouter un nouveau Client");
        JButton btnOrder = new JButton("Passer une commande");
        JButton btnTickets = new JButton("Voir les fiches de livraison");
        JButton btnDriverRank = new JButton("Voir le classement des livreurs");
        JButton btnStats = new JButton("Voir les statistiques"); // Nouveau bouton

        add(btnMenu);
        add(btnClients);
        add(btnAddClient);
        add(btnOrder);
        add(btnTickets);
        add(btnDriverRank);
        add(btnStats); // Ajout au layout

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

        btnOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new OrderWindow().setVisible(true);
            }
        });

        btnTickets.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DeliveryTicketsWindow().setVisible(true);
            }
        });

        btnDriverRank.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DriverRankingWindow().setVisible(true);
            }
        });

        btnStats.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StatsWindow().setVisible(true);
            }
        });
    }
}