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

public class RecipeAdapter
        extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    private final List<Recipe> recipes;
    private final OnRecipeClickListener listener;

    public RecipeAdapter(
            List<Recipe> recipes,
            OnRecipeClickListener listener) {

        this.recipes = recipes;
        this.listener = listener;
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

        // Display the recipe name
        holder.recipeName.setText(recipe.getName());

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