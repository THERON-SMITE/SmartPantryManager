package com.example.smartpantrymanager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.smartpantrymanager.database.RecipeDAO;
import com.example.smartpantrymanager.models.Recipe;
import com.example.smartpantrymanager.models.RecipeIngredient;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView recipeNameTextView;
    private TextView ingredientsTextView;
    private TextView methodTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_recipe_detail);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                            );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );

        // Back button
        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> finish());

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

        for (RecipeIngredient ingredient : ingredients) {

            ingredientText
                    .append(ingredient.getRequiredQuantity())
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