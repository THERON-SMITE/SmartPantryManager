package com.example.smartpantrymanager.services;
import com.example.smartpantrymanager.models.PantryItem;
import com.example.smartpantrymanager.models.Recipe;
import com.example.smartpantrymanager.models.RecipeIngredient;
import com.example.smartpantrymanager.database.RecipeDAO;
import java.util.List;
import java.util.ArrayList;

public class RecipeMatcher {

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
        return recipeIngredient.trim()
                .equalsIgnoreCase(pantryIngredient.trim());
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