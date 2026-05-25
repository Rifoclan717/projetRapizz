package com.example.model;

public class Client {
    private int id;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String address;
    private double balance;

    public Client() {}

    public Client(int id, String email, String phoneNumber, String firstName, String lastName, String address, double balance) {
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.balance = balance;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}