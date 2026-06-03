package com.example.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.example.dao.DeliveryGuyDao;
import com.example.dao.DeliveryGuyDao.DriverRankRow;
import com.example.dao.DeliveryGuyDao.DriverRankingSort;

public class DriverRankingWindow extends JFrame {

    private final DeliveryGuyDao deliveryGuyDao = new DeliveryGuyDao();

    private final DefaultTableModel model;
    private final JTable table;

    private final JLabel statusLabel = new JLabel(" ", SwingConstants.LEFT);
    private final JComboBox<SortItem> sortCombo = new JComboBox<>();
    private final JButton refreshBtn = new JButton("Rafraîchir");
    private final JButton closeBtn = new JButton("Fermer");

    private final DecimalFormat pctFmt = new DecimalFormat("0.00");

    public DriverRankingWindow() {
        setTitle("Classement des livreurs (ponctualité)");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(860, 480);
        setLocationRelativeTo(null);

        model = new DefaultTableModel(
            new Object[] { "Rang", "ID", "Livreur", "À l'heure", "En retard", "Total", "% à l'heure", "Plaque la plus utilisée" }, 0
        ) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
            @Override public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 0, 1, 3, 4, 5 -> Integer.class;
                    case 6 -> String.class;
                    default -> String.class;
                };
            }
        };

        table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);
        table.setAutoCreateRowSorter(true);

        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(0).setCellRenderer(right);
        table.getColumnModel().getColumn(1).setCellRenderer(right);
        table.getColumnModel().getColumn(3).setCellRenderer(right);
        table.getColumnModel().getColumn(4).setCellRenderer(right);
        table.getColumnModel().getColumn(5).setCellRenderer(right);

        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // Rang
        table.getColumnModel().getColumn(1).setPreferredWidth(60);  // ID
        table.getColumnModel().getColumn(2).setPreferredWidth(260); // Livreur
        table.getColumnModel().getColumn(3).setPreferredWidth(90);  // À l'heure
        table.getColumnModel().getColumn(4).setPreferredWidth(90);  // En retard
        table.getColumnModel().getColumn(5).setPreferredWidth(70);  // Total
        table.getColumnModel().getColumn(6).setPreferredWidth(90);  // %
        table.getColumnModel().getColumn(7).setPreferredWidth(200); // Plaque

        sortCombo.addItem(new SortItem("Trier par % à l'heure", DriverRankingSort.ON_TIME_PCT));
        sortCombo.addItem(new SortItem("Trier par nb. livraisons à l'heure", DriverRankingSort.ON_TIME_COUNT));
        sortCombo.addItem(new SortItem("Trier par total livraisons", DriverRankingSort.DELIVERED_TOTAL));
        sortCombo.addActionListener(e -> loadRankingAsync());

        refreshBtn.addActionListener(e -> loadRankingAsync());
        closeBtn.addActionListener(e -> dispose());

        JPanel topLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        topLeft.add(new JLabel("Classement:"));
        topLeft.add(sortCombo);

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        topRight.add(refreshBtn);
        topRight.add(closeBtn);

        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 6, 10));
        header.add(topLeft, BorderLayout.WEST);
        header.add(topRight, BorderLayout.EAST);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(BorderFactory.createEmptyBorder(6, 10, 10, 10));
        footer.add(statusLabel, BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

        loadRankingAsync();
    }

    private void setLoading(boolean loading) {
        refreshBtn.setEnabled(!loading);
        sortCombo.setEnabled(!loading);
        statusLabel.setText(loading ? "Chargement..." : " ");
    }

    private void loadRankingAsync() {
        setLoading(true);
        model.setRowCount(0);

        SortItem sortItem = (SortItem) sortCombo.getSelectedItem();
        DriverRankingSort sort = (sortItem != null) ? sortItem.sort : DriverRankingSort.ON_TIME_PCT;

        new SwingWorker<List<DriverRankRow>, Void>() {
            @Override
            protected List<DriverRankRow> doInBackground() {
                return deliveryGuyDao.getDriverRanking(sort);
            }

            @Override
            protected void done() {
                try {
                    List<DriverRankRow> rows = get();
                    int rank = 1;
                    for (DriverRankRow r : rows) {
                        model.addRow(new Object[] {
                            rank++,
                            r.getDriverId(),
                            r.getDriverName(),
                            r.getOnTimeDeliveries(),
                            r.getLateDeliveries(),
                            r.getDeliveredTotal(),
                            pctFmt.format(r.getOnTimePct()) + " %",
                            r.getMostUsedPlateNumber() != null ? r.getMostUsedPlateNumber() : "N/A"
                        });
                    }
                    statusLabel.setText(rows.size() + " livreur(s) affiché(s).");
                } catch (Exception ex) {
                    statusLabel.setText("Erreur de chargement.");
                    JOptionPane.showMessageDialog(
                        DriverRankingWindow.this,
                        "Impossible de charger le classement.\n" + ex.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE
                    );
                } finally {
                    setLoading(false);
                }
            }
        }.execute();
    }

    private static final class SortItem {
        final String label;
        final DriverRankingSort sort;

        SortItem(String label, DriverRankingSort sort) {
            this.label = label;
            this.sort = sort;
        }

        @Override public String toString() { return label; }
    }
}