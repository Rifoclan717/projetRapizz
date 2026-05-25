package com.example.model;

import java.time.LocalDateTime;

public class Order {
    private int id;
    private LocalDateTime deliveryDate;
    private double cost;
    private int deliveryTime;
    private int clientId;
    private int deliveryGuyId;
    private int vehicleId;

    public Order() {}

    public Order(int id, LocalDateTime deliveryDate, double cost, int deliveryTime, int clientId, int deliveryGuyId, int vehicleId) {
        this.id = id;
        this.deliveryDate = deliveryDate;
        this.cost = cost;
        this.deliveryTime = deliveryTime;
        this.clientId = clientId;
        this.deliveryGuyId = deliveryGuyId;
        this.vehicleId = vehicleId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDateTime getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(LocalDateTime deliveryDate) { this.deliveryDate = deliveryDate; }

    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }

    public int getDeliveryTime() { return deliveryTime; }
    public void setDeliveryTime(int deliveryTime) { this.deliveryTime = deliveryTime; }

    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    public int getDeliveryGuyId() { return deliveryGuyId; }
    public void setDeliveryGuyId(int deliveryGuyId) { this.deliveryGuyId = deliveryGuyId; }

    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }
}