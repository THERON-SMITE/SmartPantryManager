package com.example.smartpantrymanager.models;

public class PantryItem {
    private int id;
    private String name;
    private double quantity;
    private String unit;
    private String expiryDate;

    public PantryItem() {
    }

    public PantryItem(int id, String name, double quantity, String unit, String expiryDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    public PantryItem(String name, double quantity, String unit, String expiryDate) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    // Return the pantry item ID
    public int getId() {
        return id;
    }

    // Set the pantry item ID
    public void setId(int id) {
        this.id = id;
    }

    // Return the ingredient name
    public String getName() {
        return name;
    }

    // Set the ingredient name
    public void setName(String name) {
        this.name = name;
    }

    // Return the quantity
    public double getQuantity() {
        return quantity;
    }

    // Set the quantity
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    // Return the unit
    public String getUnit() {
        return unit;
    }

    // Set the unit
    public void setUnit(String unit) {
        this.unit = unit;
    }

    // Return the expiry date
    public String getExpiryDate() {
        return expiryDate;
    }

    // Set the expiry date
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}