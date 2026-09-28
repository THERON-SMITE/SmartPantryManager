package com.example.smartpantrymanager;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.smartpantrymanager.database.DatabaseHelper;
import android.content.Intent;

import com.example.smartpantrymanager.database.PantryDAO;
import com.example.smartpantrymanager.database.RecipeDAO;
import com.example.smartpantrymanager.models.PantryItem;
import com.example.smartpantrymanager.models.Recipe;
import com.example.smartpantrymanager.services.RecipeMatcher;
import com.example.smartpantrymanager.models.RecipeIngredient;
import android.widget.Toast;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Open the database and create the tables if needed
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.getWritableDatabase();

        // Open the pantry screen
        Intent intent = new Intent(
                MainActivity.this,
                PantryActivity.class
        );

        startActivity(intent);

        // Close the temporary main screen
        finish();

        RecipeDAO recipeDAO = new RecipeDAO(this);
        PantryDAO pantryDAO = new PantryDAO(this);

        List<Recipe> recipes =
                recipeDAO.getAllRecipes();

        List<PantryItem> pantryItems =
                pantryDAO.getAllPantryItems();

        RecipeMatcher recipeMatcher =
                new RecipeMatcher();

        int availableCount = 0;

        for (Recipe recipe : recipes) {

            List<RecipeIngredient> ingredients =
                    recipeDAO.getIngredientsForRecipe(
                            recipe.getId());

            if (recipeMatcher.canMakeRecipe(
                    recipe,
                    ingredients,
                    pantryItems)) {

                availableCount++;
            }
        }

        Toast.makeText(
                this,
                "Available recipes: " + availableCount,
                Toast.LENGTH_LONG
        ).show();
    }
}