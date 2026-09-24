package com.example.smartpantrymanager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartpantrymanager.adapters.PantryAdapter;
import com.example.smartpantrymanager.database.PantryDAO;
import com.example.smartpantrymanager.models.PantryItem;
import java.util.List;

public class PantryActivity extends AppCompatActivity {

    private RecyclerView pantryRecyclerView;
    private PantryDAO pantryDAO;
    private PantryAdapter pantryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        // Connect the RecyclerView to the layout
        pantryRecyclerView = findViewById(R.id.pantryRecyclerView);

        // Create the database access object
        pantryDAO = new PantryDAO(this);

        // Set the list layout
        pantryRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        Button addIngredientButton =
                findViewById(R.id.addIngredientButton);

        addIngredientButton.setOnClickListener(v -> {

            // Open the screen for adding an ingredient
            Intent intent = new Intent(
                    PantryActivity.this,
                    AddEditIngredientActivity.class
            );

            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Reload the pantry whenever the screen becomes active
        loadPantryItems();
    }

    private void loadPantryItems() {

        // Get the pantry items from SQLite
        List<PantryItem> pantryItems =
                pantryDAO.getAllPantryItems();

        // Create the adapter with the database results
        pantryAdapter = new PantryAdapter(
                this,
                pantryItems
        );

        // Display the pantry items
        pantryRecyclerView.setAdapter(pantryAdapter);
    }
}