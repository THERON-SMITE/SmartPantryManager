package com.example.smartpantrymanager.models;

public class RecipeIngredient {
    private int id;
    private int recipeId;
    private String ingredientName;
    private double requiredQuantity;
    private String unit;

    public RecipeIngredient() {
    }

    public RecipeIngredient(
            int id,
            int recipeId,
            String ingredientName,
            double requiredQuantity,
            String unit) {

        this.id = id;
        this.recipeId = recipeId;
        this.ingredientName = ingredientName;
        this.requiredQuantity = requiredQuantity;
        this.unit = unit;
    }

    public RecipeIngredient(
            int recipeId,
            String ingredientName,
            double requiredQuantity,
            String unit) {

        this.recipeId = recipeId;
        this.ingredientName = ingredientName;
        this.requiredQuantity = requiredQuantity;
        this.unit = unit;
    }

    // Return the ingredient ID
    public int getId() {
        return id;
    }

    // Set the ingredient ID
    public void setId(int id) {
        this.id = id;
    }

    // Return the recipe ID
    public int getRecipeId() {
        return recipeId;
    }

    // Set the recipe ID
    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    // Return the ingredient name
    public String getIngredientName() {
        return ingredientName;
    }

    // Set the ingredient name
    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    // Return the required quantity
    public double getRequiredQuantity() {
        return requiredQuantity;
    }

    // Set the required quantity
    public void setRequiredQuantity(double requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }

    // Return the unit
    public String getUnit() {
        return unit;
    }

    // Set the unit
    public void setUnit(String unit) {
        this.unit = unit;
    }
}
