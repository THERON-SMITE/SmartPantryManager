package com.example.smartpantrymanager.adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartpantrymanager.AddEditIngredientActivity;
import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.database.PantryDAO;
import com.example.smartpantrymanager.models.PantryItem;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private final List<PantryItem> pantryItems;
    private final Context context;
    private final PantryDAO pantryDAO;

    public PantryAdapter(Context context, List<PantryItem> pantryItems) {
        this.context = context;
        this.pantryItems = pantryItems;
        this.pantryDAO = new PantryDAO(context);
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        // Create a view for each pantry item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);

        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull PantryViewHolder holder,
            int position) {

        // Get the pantry item for this row
        PantryItem item = pantryItems.get(position);

        holder.ingredientNameText.setText(item.getName());

        holder.ingredientQuantityText.setText(
                item.getQuantity() + " " + item.getUnit()
        );

        if (item.getExpiryDate() == null || item.getExpiryDate().isEmpty()) {

            holder.ingredientExpiryText.setText("No expiry date");

        } else {

            holder.ingredientExpiryText.setText(
                    "Expires: " + item.getExpiryDate()
            );
        }

        holder.editButton.setOnClickListener(v -> {

            // Open the edit screen for this pantry item
            Intent intent = new Intent(
                    context,
                    AddEditIngredientActivity.class
            );

            intent.putExtra("pantryItemId", item.getId());

            context.startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(v -> {

            // Delete the selected pantry item
            int result = pantryDAO.deletePantryItem(item.getId());

            if (result > 0) {

                pantryItems.remove(position);
                notifyItemRemoved(position);

                Toast.makeText(
                        context,
                        "Ingredient deleted",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return pantryItems.size();
    }

    public static class PantryViewHolder extends RecyclerView.ViewHolder {

        TextView ingredientNameText;
        TextView ingredientQuantityText;
        TextView ingredientExpiryText;

        Button editButton;
        Button deleteButton;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);

            // Connect the layout views
            ingredientNameText =
                    itemView.findViewById(R.id.ingredientNameText);

            ingredientQuantityText =
                    itemView.findViewById(R.id.ingredientQuantityText);

            ingredientExpiryText =
                    itemView.findViewById(R.id.ingredientExpiryText);

            editButton =
                    itemView.findViewById(R.id.editButton);

            deleteButton =
                    itemView.findViewById(R.id.deleteButton);
        }
    }
}