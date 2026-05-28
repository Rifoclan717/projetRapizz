package com.example.model;

public class DeliveryGuy {
    private int id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private boolean canBike;
    private boolean canDrive;

    
    public DeliveryGuy() {}

    public DeliveryGuy(int id, String firstName, String lastName, String phoneNumber, boolean canBike, boolean canDrive) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.canBike = canBike;
        this.canDrive = canDrive;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public boolean isCanBike() {
    return canBike;
    }
    public void setCanBike(boolean canBike) {
        this.canBike = canBike;
    }
    public boolean isCanDrive() {
        return canDrive;
    }
    public void setCanDrive(boolean canDrive) {
        this.canDrive = canDrive;
}

}