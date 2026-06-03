package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.model.DeliveryGuy;
import com.example.repositories.DatabaseConnection;

public class DeliveryGuyDao {

    public List<DeliveryGuy> getDeliveryGuys() {

        List<DeliveryGuy> deliveryGuys = new ArrayList<>();

        String sql = "SELECT id, firstName, lastName, canBike, canDrive FROM Drivers";

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                DeliveryGuy deliveryGuy = new DeliveryGuy();

                deliveryGuy.setId(rs.getInt("id"));
                deliveryGuy.setFirstName(rs.getString("firstName"));
                deliveryGuy.setLastName(rs.getString("lastName"));

                deliveryGuy.setCanBike(rs.getBoolean("canBike"));
                deliveryGuy.setCanDrive(rs.getBoolean("canDrive"));

                deliveryGuys.add(deliveryGuy);
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la lecture des delivery guys : " + e.getMessage());
        }

        return deliveryGuys;
    }

    public DeliveryGuy getFreeDriver() {
        String sql = "SELECT id, firstName, lastName, canBike, canDrive FROM Drivers " +
                     "WHERE id NOT IN (SELECT driverId FROM Orders WHERE deliveryTime IS NULL) " +
                     "LIMIT 1";

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                DeliveryGuy deliveryGuy = new DeliveryGuy();
                deliveryGuy.setId(rs.getInt("id"));
                deliveryGuy.setFirstName(rs.getString("firstName"));
                deliveryGuy.setLastName(rs.getString("lastName"));
                deliveryGuy.setCanBike(rs.getBoolean("canBike"));
                deliveryGuy.setCanDrive(rs.getBoolean("canDrive"));
                return deliveryGuy;
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la recherche du livreur libre : " + e.getMessage());
        }

        return null;
    }

    public static class DriverRankRow {
        private final int driverId;
        private final String driverName;
        private final int onTimeDeliveries;
        private final int lateDeliveries;
        private final int deliveredTotal;
        private final double onTimePct;
        private final String mostUsedPlateNumber;

        public DriverRankRow(int driverId, String driverName, int onTimeDeliveries, int lateDeliveries, int deliveredTotal, double onTimePct, String mostUsedPlateNumber) {
            this.driverId = driverId;
            this.driverName = driverName;
            this.onTimeDeliveries = onTimeDeliveries;
            this.lateDeliveries = lateDeliveries;
            this.deliveredTotal = deliveredTotal;
            this.onTimePct = onTimePct;
            this.mostUsedPlateNumber = mostUsedPlateNumber;
        }

        public int getDriverId() { return driverId; }
        public String getDriverName() { return driverName; }
        public int getOnTimeDeliveries() { return onTimeDeliveries; }
        public int getLateDeliveries() { return lateDeliveries; }
        public int getDeliveredTotal() { return deliveredTotal; }
        public double getOnTimePct() { return onTimePct; }
        public String getMostUsedPlateNumber() { return mostUsedPlateNumber; }
    }

    public enum DriverRankingSort {
        ON_TIME_PCT,
        ON_TIME_COUNT,
        DELIVERED_TOTAL
    }

    public List<DriverRankRow> getDriverRanking(DriverRankingSort sort) {
        String orderBy;
        switch (sort) {
            case ON_TIME_COUNT:
                orderBy = "on_time DESC, delivered_total DESC, on_time_pct DESC, driver_name ASC";
                break;
            case DELIVERED_TOTAL:
                orderBy = "delivered_total DESC, on_time_pct DESC, on_time DESC, driver_name ASC";
                break;
            case ON_TIME_PCT:
            default:
                orderBy = "on_time_pct DESC, delivered_total DESC, on_time DESC, driver_name ASC";
                break;
        }

        String sql =
            "SELECT d.id, CONCAT(d.firstName, ' ', d.lastName) AS driver_name, " +
            "       COALESCE(SUM(CASE " +
            "           WHEN o.deliveryTime IS NOT NULL " +
            "            AND o.orderDate IS NOT NULL " +
            "            AND TIMESTAMPDIFF(MINUTE, o.orderDate, o.deliveryTime) <= 30 " +
            "           THEN 1 ELSE 0 END), 0) AS on_time, " +
            "       COALESCE(SUM(CASE " +
            "           WHEN o.deliveryTime IS NOT NULL " +
            "            AND o.orderDate IS NOT NULL " +
            "            AND TIMESTAMPDIFF(MINUTE, o.orderDate, o.deliveryTime) > 30 " +
            "           THEN 1 ELSE 0 END), 0) AS late, " +
            "       COALESCE(SUM(CASE WHEN o.deliveryTime IS NOT NULL THEN 1 ELSE 0 END), 0) AS delivered_total, " +
            "       CASE " +
            "           WHEN COALESCE(SUM(CASE WHEN o.deliveryTime IS NOT NULL THEN 1 ELSE 0 END), 0) = 0 THEN 0 " +
            "           ELSE ROUND(100.0 * " +
            "               COALESCE(SUM(CASE " +
            "                   WHEN o.deliveryTime IS NOT NULL " +
            "                    AND o.orderDate IS NOT NULL " +
            "                    AND TIMESTAMPDIFF(MINUTE, o.orderDate, o.deliveryTime) <= 30 " +
            "                   THEN 1 ELSE 0 END), 0) " +
            "               / COALESCE(SUM(CASE WHEN o.deliveryTime IS NOT NULL THEN 1 ELSE 0 END), 0), " +
            "           2) " +
            "       END AS on_time_pct, " +
            "       COALESCE( " +
            "           (SELECT v2.plateNumber " +
            "            FROM Orders o2 " +
            "            JOIN Vehicles v2 ON v2.id = o2.vehicleId " +
            "            WHERE o2.driverId = d.id " +
            "              AND o2.deliveryTime IS NOT NULL " +
            "            GROUP BY v2.id, v2.plateNumber " +
            "            ORDER BY COUNT(*) DESC, v2.plateNumber ASC " +
            "            LIMIT 1), " +
            "           '—' " +
            "       ) AS most_used_plate " +
            "FROM Drivers d " +
            "LEFT JOIN Orders o ON o.driverId = d.id " +
            "GROUP BY d.id, d.firstName, d.lastName " +
            "ORDER BY " + orderBy;

        List<DriverRankRow> rows = new ArrayList<>();

        try (Connection c = DatabaseConnection.getInstance();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("driver_name");
                int onTime = rs.getInt("on_time");
                int late = rs.getInt("late");
                int total = rs.getInt("delivered_total");
                double pct = rs.getDouble("on_time_pct");
                String plate = rs.getString("most_used_plate");
                rows.add(new DriverRankRow(id, name, onTime, late, total, pct, plate));
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du chargement du classement des livreurs", e);
        }

        return rows;
    }
}