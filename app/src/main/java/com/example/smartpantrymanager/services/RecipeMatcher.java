package com.example.smartpantrymanager.services;
import com.example.smartpantrymanager.models.PantryItem;
import com.example.smartpantrymanager.models.Recipe;
import com.example.smartpantrymanager.models.RecipeIngredient;
import com.example.smartpantrymanager.database.RecipeDAO;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class RecipeMatcher {

    private String normaliseIngredientName(
            String ingredientName) {

        // Standardise the ingredient name
        String name =
                ingredientName.trim().toLowerCase();

        // Handle common plural forms
        if (name.endsWith("ies")) {
            name = name.substring(
                    0,
                    name.length() - 3
            ) + "y";
        } else if (name.endsWith("oes") || name.endsWith("ches") || name.endsWith("shes") || name.endsWith("xes")) {
            name = name.substring(
                    0,
                    name.length() - 2
            );
        } else if (name.endsWith("s")) {
            name = name.substring(
                    0,
                    name.length() - 1
            );
        }

        // Singular words ending in "ie" get the same form as their plural
        if (name.endsWith("ie")) {
            name = name.substring(
                    0,
                    name.length() - 2
            ) + "y";
        }

        return name;
    }

    public boolean canMakeRecipe(
            Recipe recipe,
            List<RecipeIngredient> recipeIngredients,
            List<PantryItem> pantryItems) {

        // Check every ingredient required by the recipe
        for (RecipeIngredient requiredIngredient : recipeIngredients) {

            boolean ingredientMatched = false;

            // Look for the required ingredient in the pantry
            for (PantryItem pantryItem : pantryItems) {

                if (ingredientsMatch(
                        requiredIngredient.getIngredientName(),
                        pantryItem.getName())) {

                    // Check that the units are compatible
                    if (!unitsMatch(
                            requiredIngredient.getUnit(),
                            pantryItem.getUnit())) {

                        continue;
                    }

                    // Check that enough quantity is available
                    if (toBaseQuantity(
                            pantryItem.getQuantity(),
                            pantryItem.getUnit())
                            >= toBaseQuantity(
                            requiredIngredient.getRequiredQuantity(),
                            requiredIngredient.getUnit())) {

                        ingredientMatched = true;
                        break;
                    }
                }
            }

            // One missing ingredient means the recipe cannot be made
            if (!ingredientMatched) {
                return false;
            }
        }

        // All required ingredients were available
        return true;
    }

    private boolean ingredientsMatch(
            String recipeIngredient,
            String pantryIngredient) {

        // Compare ingredient names without case differences
        String recipeName =
                normaliseIngredientName(recipeIngredient);

        String pantryName =
                normaliseIngredientName(pantryIngredient);

        return recipeName.equals(pantryName);
    }

    private boolean unitsMatch(
            String recipeUnit,
            String pantryUnit) {

        // Units match when they measure the same thing
        return baseUnit(recipeUnit)
                .equals(baseUnit(pantryUnit));
    }

    // Weight units become "g" and volume units become "ml"
    private String baseUnit(String unit) {

        String cleaned = unit.trim().toLowerCase();

        switch (cleaned) {
            case "g":
            case "oz":
            case "lb":
                return "g";

            case "ml":
            case "teaspoon":
            case "tablespoon":
            case "cups":
                return "ml";

            default:
                // whole, slices, pack and can only match themselves
                return cleaned;
        }
    }

    // Convert a quantity to grams or millilitres so it can be compared
    private double toBaseQuantity(double quantity, String unit) {

        switch (unit.trim().toLowerCase()) {
            case "oz":
                return quantity * 28.35;
            case "lb":
                return quantity * 453.6;
            case "teaspoon":
                return quantity * 5;
            case "tablespoon":
                return quantity * 15;
            case "cups":
                return quantity * 250;
            default:
                // g, ml, whole and slices are already in their base unit
                return quantity;
        }
    }

    public List<Recipe> getAvailableRecipes(
            List<Recipe> recipes,
            RecipeDAO recipeDAO,
            List<PantryItem> pantryItems) {

        List<Recipe> availableRecipes =
                new ArrayList<>();

        // Check each recipe against the pantry
        for (Recipe recipe : recipes) {

            List<RecipeIngredient> ingredients =
                    recipeDAO.getIngredientsForRecipe(
                            recipe.getId());

            if (canMakeRecipe(
                    recipe,
                    ingredients,
                    pantryItems)) {

                availableRecipes.add(recipe);
            }
        }

        return availableRecipes;
    }

    // Recipes missing exactly one ingredient
    public Map<Recipe, RecipeIngredient> getAlmostThereRecipes(
            List<Recipe> recipes,
            RecipeDAO recipeDAO,
            List<PantryItem> pantryItems) {

        Map<Recipe, RecipeIngredient> almostThere =
                new LinkedHashMap<>();

        for (Recipe recipe : recipes) {

            List<RecipeIngredient> ingredients =
                    recipeDAO.getIngredientsForRecipe(
                            recipe.getId());

            // Recipes that already qualify belong in the strict list
            if (canMakeRecipe(recipe, ingredients, pantryItems)) {
                continue;
            }

            for (RecipeIngredient candidate : ingredients) {

                List<RecipeIngredient> others =
                        new ArrayList<>(ingredients);
                others.remove(candidate);

                if (canMakeRecipe(recipe, others, pantryItems)) {
                    almostThere.put(recipe, candidate);
                    break;
                }
            }
        }

        return almostThere;
    }
}