package com.example.model;

import java.sql.Timestamp;

public class DeliveryTicket {
    private int orderId;
    private String driverName;
    private String vehicleType;
    private String clientName;
    private Timestamp orderDate;
    private Timestamp deliveryTime;
    private String productName;
    private double basePrice;

    public DeliveryTicket() {}

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public Timestamp getOrderDate() { return orderDate; }
    public void setOrderDate(Timestamp orderDate) { this.orderDate = orderDate; }

    public Timestamp getDeliveryTime() { return deliveryTime; }
    public void setDeliveryTime(Timestamp deliveryTime) { this.deliveryTime = deliveryTime; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }
}