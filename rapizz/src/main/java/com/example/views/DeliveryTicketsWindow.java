package com.example.views;

import java.awt.BorderLayout;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane; // Ajout pour les pop-ups
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel; // Ajout pour la sélection
import javax.swing.table.DefaultTableModel;

import com.example.dao.OrderDao;
import com.example.model.DeliveryTicket;

public class DeliveryTicketsWindow extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private final OrderDao orderDao = new OrderDao();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final DecimalFormat money = new DecimalFormat("0.00");

    public DeliveryTicketsWindow() {
        setTitle("Fiches de Livraison");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] columns = {
            "ID Cmd", "Livreur", "Véhicule", "Client", "Date Commande", "Retard ?", "Pizza", "Prix Base"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        
        JButton btnDeliver = new JButton("Marquer comme livrée"); 
        JButton btnRefresh = new JButton("Actualiser");
        JButton btnClose = new JButton("Fermer");

        btnDeliver.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            
            // Si aucune ligne n'est sélectionnée
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une commande dans le tableau.", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // On récupère l'ID de la commande (colonne 0)
            int orderId = (int) tableModel.getValueAt(selectedRow, 0);
            
            // On appelle ta méthode DAO qui exécute la procédure stockée SQL
            orderDao.deliverOrder(orderId);
            
            JOptionPane.showMessageDialog(this, "La commande #" + orderId + " a bien été livrée !");
            
            // On rafraîchit le tableau pour voir les changements
            loadData();
        });

        btnRefresh.addActionListener(e -> loadData());
        btnClose.addActionListener(e -> dispose());

        bottomPanel.add(btnDeliver); // Ajout du bouton à l'interface
        bottomPanel.add(btnRefresh);
        bottomPanel.add(btnClose);
        
        add(bottomPanel, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<DeliveryTicket> tickets = orderDao.getDeliveryTickets();

        for (DeliveryTicket t : tickets) {
            String retardLabel = "En cours";
            
            // Si la commande est livrée, on calcule le retard
            if (t.getDeliveryTime() != null) {
                long diffMillis = t.getDeliveryTime().getTime() - t.getOrderDate().getTime();
                long diffMinutes = diffMillis / (60 * 1000);
                if (diffMinutes > 30) {
                    retardLabel = "Oui (" + diffMinutes + " min)";
                } else {
                    retardLabel = "Non (" + diffMinutes + " min)";
                }
            }

            tableModel.addRow(new Object[]{
                t.getOrderId(),
                t.getDriverName(),
                t.getVehicleType(),
                t.getClientName(),
                dateFormat.format(t.getOrderDate()),
                retardLabel,
                t.getProductName(),
                money.format(t.getBasePrice()) + " €"
            });
        }
    }
}