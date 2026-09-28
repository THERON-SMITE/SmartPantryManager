package com.example.smartpantrymanager.services;
import com.example.smartpantrymanager.models.PantryItem;
import com.example.smartpantrymanager.models.Recipe;
import com.example.smartpantrymanager.models.RecipeIngredient;
import com.example.smartpantrymanager.database.RecipeDAO;
import java.util.List;
import java.util.ArrayList;

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
        } else if (name.endsWith("es")) {
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
                    if (pantryItem.getQuantity()
                            >= requiredIngredient.getRequiredQuantity()) {

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

        // Compare units without case differences
        return recipeUnit.trim()
                .equalsIgnoreCase(pantryUnit.trim());
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
}