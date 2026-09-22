package com.example.smartpantrymanager.models;

public class Recipe {
    private int id;
    private String name;
    private String instructions;

    public Recipe() {
    }

    public Recipe(int id, String name, String instructions) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
    }

    public Recipe(String name, String instructions) {
        this.name = name;
        this.instructions = instructions;
    }

    // Return the recipe ID
    public int getId() {
        return id;
    }

    // Set the recipe ID
    public void setId(int id) {
        this.id = id;
    }

    // Return the recipe name
    public String getName() {
        return name;
    }

    // Set the recipe name
    public void setName(String name) {
        this.name = name;
    }

    // Return the recipe instructions
    public String getInstructions() {
        return instructions;
    }

    // Set the recipe instructions
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
