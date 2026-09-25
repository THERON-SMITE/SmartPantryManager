package com.example.smartpantrymanager.database;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.smartpantrymanager.models.Recipe;
import com.example.smartpantrymanager.models.RecipeIngredient;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAO {

    private final DatabaseHelper databaseHelper;

    public RecipeDAO(Context context) {
        // Create the database helper
        databaseHelper = new DatabaseHelper(context);
    }

    public List<Recipe> getAllRecipes() {

        List<Recipe> recipes = new ArrayList<>();

        // Open the database for reading
        SQLiteDatabase db =
                databaseHelper.getReadableDatabase();

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_RECIPES,
                null,
                null,
                null,
                null,
                null,
                "name ASC"
        );

        // Read all recipes from the database
        while (cursor.moveToNext()) {

            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id")
            );

            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow("name")
            );

            String instructions = cursor.getString(
                    cursor.getColumnIndexOrThrow("instructions")
            );

            recipes.add(
                    new Recipe(
                            id,
                            name,
                            instructions
                    )
            );
        }

        cursor.close();
        db.close();

        return recipes;
    }

    public Recipe getRecipeById(int recipeId) {

        // Open the database for reading
        SQLiteDatabase db =
                databaseHelper.getReadableDatabase();

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_RECIPES,
                null,
                "id = ?",
                new String[]{
                        String.valueOf(recipeId)
                },
                null,
                null,
                null
        );

        Recipe recipe = null;

        // Read the selected recipe
        if (cursor.moveToFirst()) {

            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow("name")
            );

            String instructions = cursor.getString(
                    cursor.getColumnIndexOrThrow("instructions")
            );

            recipe = new Recipe(
                    recipeId,
                    name,
                    instructions
            );
        }

        cursor.close();
        db.close();

        return recipe;
    }

    public List<RecipeIngredient> getIngredientsForRecipe(
            int recipeId) {

        List<RecipeIngredient> ingredients =
                new ArrayList<>();

        // Open the database for reading
        SQLiteDatabase db =
                databaseHelper.getReadableDatabase();

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_RECIPE_INGREDIENTS,
                null,
                "recipeId = ?",
                new String[]{
                        String.valueOf(recipeId)
                },
                null,
                null,
                "ingredientName ASC"
        );

        // Read all ingredients for the recipe
        while (cursor.moveToNext()) {

            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id")
            );

            String ingredientName = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            "ingredientName"
                    )
            );

            double requiredQuantity = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(
                            "requiredQuantity"
                    )
            );

            String unit = cursor.getString(
                    cursor.getColumnIndexOrThrow("unit")
            );

            ingredients.add(
                    new RecipeIngredient(
                            id,
                            recipeId,
                            ingredientName,
                            requiredQuantity,
                            unit
                    )
            );
        }

        cursor.close();
        db.close();

        return ingredients;
    }
}