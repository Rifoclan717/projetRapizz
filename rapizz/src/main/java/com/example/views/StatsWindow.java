package com.example.views;

import java.awt.BorderLayout;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.example.dao.StatsDao;

public class StatsWindow extends JFrame {

    private final StatsDao statsDao = new StatsDao();
    private final DecimalFormat money = new DecimalFormat("0.00");
    private final DecimalFormat dec = new DecimalFormat("0.0");

    public StatsWindow() {
        setTitle("RaPizz - Statistiques");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(textArea);
        panel.add(scroll, BorderLayout.CENTER);
        add(panel);

        loadStats(textArea);
    }

    private void loadStats(JTextArea t) {
        t.append("=== CHIFFRE D'AFFAIRES ===\n");
        t.append("Total encaissé : " + money.format(statsDao.getTotalRevenue()) + " €\n\n");

        t.append("=== VÉHICULES ===\n");
        List<String> unused = statsDao.getUnusedVehicles();
        if (unused.isEmpty()) {
            t.append("Tous les véhicules ont servi au moins une fois.\n");
        } else {
            t.append("Véhicules n'ayant jamais servi :\n");
            for (String v : unused) t.append(" - " + v + "\n");
        }
        t.append("\n");

        t.append("=== PRODUITS & INGRÉDIENTS ===\n");
        t.append("Pizza la plus demandée : " + statsDao.getMostDemandedPizza() + "\n");
        t.append("Pizza la moins demandée : " + statsDao.getLeastDemandedPizza() + "\n");
        t.append("Ingrédient favori : " + statsDao.getFavoriteIngredient() + "\n\n");

        t.append("=== CLIENTS ===\n");
        t.append("Meilleur client : " + statsDao.getBestClient() + "\n");
        t.append("Moyenne des commandes par client : " + dec.format(statsDao.getAverageOrders()) + "\n");
        
        t.append("\nClients au-dessus de la moyenne :\n");
        List<String> aboveAvg = statsDao.getClientsAboveAverage();
        for (String c : aboveAvg) t.append(" - " + c + "\n");

        t.append("\nNombre de commandes par client :\n");
        List<String> perClient = statsDao.getOrdersPerClient();
        for (String c : perClient) t.append(" - " + c + "\n");
    }
}