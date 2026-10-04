package com.example.smartpantrymanager.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.models.Recipe;
import java.util.List;
import java.util.Map;
import com.example.smartpantrymanager.models.RecipeIngredient;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

public class RecipeAdapter
        extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    private final List<Recipe> recipes;
    private final OnRecipeClickListener listener;
    private Map<Recipe, RecipeIngredient> missingIngredients;

    public RecipeAdapter(
            List<Recipe> recipes,
            OnRecipeClickListener listener) {

        this.recipes = recipes;
        this.listener = listener;
    }

    // Missing ingredient check
    public void setMissingIngredients(
            Map<Recipe, RecipeIngredient> missingIngredients) {
        this.missingIngredients = missingIngredients;
    }


    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_recipe,
                        parent,
                        false
                );

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecipeViewHolder holder,
            int position) {

        // Get the recipe for this card
        Recipe recipe = recipes.get(position);

        // Get the recipe name
        String name = recipe.getName();

        SpannableString text;

        // Almost there recipes show the missing ingredient on a second line
        if (missingIngredients != null
                && missingIngredients.containsKey(recipe)) {

            text = new SpannableString(
                    name
                            + "\nMissing: "
                            + missingIngredients.get(recipe).getIngredientName());

            // Make the missing ingredient line smaller than the recipe name
            text.setSpan(
                    new RelativeSizeSpan(0.7f),
                    name.length(),
                    text.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );

        } else {

            // Suggested recipes show the recipe name only
            text = new SpannableString(name);
        }

        // Make the recipe name bold
        text.setSpan(
                new StyleSpan(Typeface.BOLD),
                0,
                name.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        // Display the recipe name
        holder.recipeName.setText(text);

        // Open the recipe details when View is pressed
        holder.viewRecipeButton.setOnClickListener(
                view -> listener.onRecipeClick(recipe)
        );
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    static class RecipeViewHolder
            extends RecyclerView.ViewHolder {

        TextView recipeName;
        Button viewRecipeButton;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            // Connect the recipe name
            recipeName =
                    itemView.findViewById(
                            R.id.recipeName
                    );

            //Connect the View Button
            viewRecipeButton =
                    itemView.findViewById(
                            R.id.viewRecipeButton
                    );

        }
    }
}