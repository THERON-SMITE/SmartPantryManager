package com.example.smartpantrymanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartpantrymanager.AddEditIngredientActivity;
import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.database.PantryDAO;
import com.example.smartpantrymanager.models.PantryItem;
import java.util.List;
import android.content.res.ColorStateList;
import androidx.core.content.ContextCompat;
import com.example.smartpantrymanager.SettingsActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private final List<PantryItem> pantryItems;
    private final Context context;
    private final PantryDAO pantryDAO;
    private final boolean expiryAlertsEnabled;

    public PantryAdapter(Context context, List<PantryItem> pantryItems) {
        this.context = context;
        this.pantryItems = pantryItems;
        this.pantryDAO = new PantryDAO(context);

        // Read the setting chosen on the Settings screen
        expiryAlertsEnabled = context
                .getSharedPreferences(SettingsActivity.PREFS_NAME,
                        Context.MODE_PRIVATE)
                .getBoolean(SettingsActivity.KEY_EXPIRY_ALERTS, true);
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

        // Display the ingredient name
        holder.ingredientNameText.setText(item.getName());

        // Display the quantity without unnecessary decimal places
        DecimalFormat quantityFormat =
                new DecimalFormat("0.##");

        holder.ingredientQuantityText.setText(
                quantityFormat.format(item.getQuantity())
                        + " "
                        + item.getUnit()
        );

        holder.ingredientExpiryText.setTextColor(holder.defaultExpiryColour);

        String expiryDate = item.getExpiryDate();

        if (expiryDate == null || expiryDate.isEmpty()) {
            holder.ingredientExpiryText.setText("No expiry date");
        } else {
            long daysLeft = daysUntil(expiryDate);

            if (expiryAlertsEnabled && daysLeft < 0) {
                // Already expired: red
                holder.ingredientExpiryText.setText("Expired: " + expiryDate);
                holder.ingredientExpiryText.setTextColor(
                        ContextCompat.getColor(context, R.color.expiry_warning));

            } else if (expiryAlertsEnabled && daysLeft <= 3) {
                // Expires within 3 days: amber
                holder.ingredientExpiryText.setText("Expiring soon: " + expiryDate);
                holder.ingredientExpiryText.setTextColor(
                        ContextCompat.getColor(context, R.color.expiry_soon));

            } else {
                holder.ingredientExpiryText.setText("Expires: " + expiryDate);
            }
        }

        holder.editButton.setOnClickListener(v -> {

            // Open the edit screen for this pantry item
            Intent intent = new Intent(
                    context,
                    AddEditIngredientActivity.class
            );

            intent.putExtra(
                    "pantryItemId",
                    item.getId()
            );

            context.startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(v -> {

            // Get the current adapter position
            int adapterPosition =
                    holder.getBindingAdapterPosition();

            if (adapterPosition != RecyclerView.NO_POSITION) {

                PantryItem selectedItem =
                        pantryItems.get(adapterPosition);

                // Ask the user to confirm the deletion
                new AlertDialog.Builder(context)
                        .setTitle("Delete Ingredient")
                        .setMessage(
                                "Are you sure you want to delete "
                                        + selectedItem.getName()
                                        + "?"
                        )
                        .setNegativeButton(
                                "Cancel",
                                null
                        )
                        .setPositiveButton(
                                "Delete",
                                (dialog, which) -> {

                                    // Delete the selected pantry item
                                    int result =
                                            pantryDAO.deletePantryItem(
                                                    selectedItem.getId()
                                            );

                                    if (result > 0) {

                                        pantryItems.remove(
                                                adapterPosition
                                        );

                                        notifyItemRemoved(
                                                adapterPosition
                                        );

                                        Toast.makeText(
                                                context,
                                                "Ingredient deleted",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    }
                                }
                        )
                        .show();
            }
        });
    }

    // Number of days from today until the expiry date (yyyy-MM-dd)
    private long daysUntil(String expiryDate) {
        try {
            Date expiry = new SimpleDateFormat("yyyy-MM-dd", Locale.US)
                    .parse(expiryDate);

            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            long difference = expiry.getTime() - today.getTimeInMillis();
            return Math.round(difference / (24.0 * 60 * 60 * 1000));

        } catch (Exception e) {
            // An unreadable date is never flagged
            return Long.MAX_VALUE;
        }
    }

    @Override
    public int getItemCount() {
        return pantryItems.size();
    }

    public static class PantryViewHolder
            extends RecyclerView.ViewHolder {

        TextView ingredientNameText;
        TextView ingredientQuantityText;
        TextView ingredientExpiryText;

        Button editButton;
        Button deleteButton;
        ColorStateList defaultExpiryColour;

        public PantryViewHolder(
                @NonNull View itemView) {

            super(itemView);

            // Connect the layout views
            ingredientNameText =
                    itemView.findViewById(
                            R.id.ingredientNameText
                    );

            ingredientQuantityText =
                    itemView.findViewById(
                            R.id.ingredientQuantityText
                    );

            ingredientExpiryText =
                    itemView.findViewById(
                            R.id.ingredientExpiryText
                    );

            editButton =
                    itemView.findViewById(
                            R.id.editButton
                    );

            deleteButton =
                    itemView.findViewById(
                            R.id.deleteButton
                    );

            // Remember the normal text colour so it can be restored
            defaultExpiryColour = ingredientExpiryText.getTextColors();
        }
    }
}