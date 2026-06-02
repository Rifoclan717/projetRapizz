package com.example.model;

public class OrderProduct {
    private int orderId;
    private int productId;
    private int quantity;
    private String size;

    public OrderProduct() {}

    public OrderProduct(int orderId, int productId, int quantity, String size) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.size = size;
    }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
}