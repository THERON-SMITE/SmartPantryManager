package com.example.smartpantrymanager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartpantrymanager.adapters.RecipeAdapter;
import com.example.smartpantrymanager.database.PantryDAO;
import com.example.smartpantrymanager.database.RecipeDAO;
import com.example.smartpantrymanager.models.PantryItem;
import com.example.smartpantrymanager.models.Recipe;
import com.example.smartpantrymanager.models.RecipeIngredient;
import com.example.smartpantrymanager.services.RecipeMatcher;
import java.util.List;

public class SuggestedRecipesActivity
        extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView noRecipesMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(
                R.layout.activity_suggested_recipes
        );

        // Find the screen controls
        recyclerView =
                findViewById(
                        R.id.suggestedRecipesRecyclerView
                );

        noRecipesMessage =
                findViewById(
                        R.id.noRecipesMessage
                );

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        loadSuggestedRecipes();
    }

    private void loadSuggestedRecipes() {

        // Load pantry and recipe data
        PantryDAO pantryDAO =
                new PantryDAO(this);

        RecipeDAO recipeDAO =
                new RecipeDAO(this);

        List<PantryItem> pantryItems =
                pantryDAO.getAllPantryItems();

        List<Recipe> recipes =
                recipeDAO.getAllRecipes();

        // Run the strict matching logic
        RecipeMatcher matcher =
                new RecipeMatcher();

        List<Recipe> availableRecipes =
                matcher.getAvailableRecipes(
                        recipes,
                        recipeDAO,
                        pantryItems
                );

        if (availableRecipes.isEmpty()) {

            // Show feedback when nothing matches
            noRecipesMessage.setVisibility(
                    TextView.VISIBLE
            );

            recyclerView.setVisibility(
                    RecyclerView.GONE
            );

        } else {

            // Display the matching recipes
            noRecipesMessage.setVisibility(
                    TextView.GONE
            );

            recyclerView.setVisibility(
                    RecyclerView.VISIBLE
            );

            RecipeAdapter adapter =
                    new RecipeAdapter(
                            availableRecipes,
                            recipe -> {

                                Intent intent =
                                        new Intent(
                                                SuggestedRecipesActivity.this,
                                                RecipeDetailActivity.class
                                        );

                                intent.putExtra(
                                        "recipeId",
                                        recipe.getId()
                                );

                                startActivity(intent);
                            }
                    );

            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Refresh recipes when returning to the screen
        loadSuggestedRecipes();
    }
}