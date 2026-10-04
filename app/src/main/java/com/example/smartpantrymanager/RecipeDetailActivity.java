package com.example.smartpantrymanager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartpantrymanager.database.RecipeDAO;
import com.example.smartpantrymanager.models.Recipe;
import com.example.smartpantrymanager.models.RecipeIngredient;
import java.util.List;
import java.text.DecimalFormat;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView recipeNameTextView;
    private TextView ingredientsTextView;
    private TextView methodTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_recipe_detail);

        // Bottom navigation bar
        NavigationHelper.setup(this, NavigationHelper.NO_TAB);

        // Find the recipe detail controls
        recipeNameTextView =
                findViewById(R.id.recipeNameTextView);

        ingredientsTextView =
                findViewById(R.id.ingredientsTextView);

        methodTextView =
                findViewById(R.id.methodTextView);

        // Load the selected recipe
        loadRecipeDetails();
    }

    private void loadRecipeDetails() {

        // Get the recipe ID sent from Suggested Recipes
        int recipeId =
                getIntent().getIntExtra(
                        "recipeId",
                        -1
                );

        // Check that a valid recipe ID was received
        if (recipeId == -1) {
            recipeNameTextView.setText(
                    "Recipe not found"
            );

            return;
        }

        // Create the recipe database object
        RecipeDAO recipeDAO =
                new RecipeDAO(this);

        // Get the selected recipe
        Recipe recipe =
                recipeDAO.getRecipeById(recipeId);

        if (recipe == null) {
            recipeNameTextView.setText(
                    "Recipe not found"
            );

            return;
        }

        // Display the recipe name
        recipeNameTextView.setText(
                recipe.getName()
        );

        // Get the ingredients for this recipe
        List<RecipeIngredient> ingredients =
                recipeDAO.getIngredientsForRecipe(
                        recipeId
                );

        // Build the ingredient list
        StringBuilder ingredientText =
                new StringBuilder();

        // Display quantities without unnecessary decimal places
        DecimalFormat quantityFormat =
                new DecimalFormat("0.##");

        for (RecipeIngredient ingredient : ingredients) {

            ingredientText
                    .append(quantityFormat.format(ingredient.getRequiredQuantity()))
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append(" ")
                    .append(ingredient.getIngredientName())
                    .append("\n");
        }

        ingredientsTextView.setText(
                ingredientText.toString()
        );

        // Display the recipe method
        methodTextView.setText(
                recipe.getInstructions()
        );
    }
}