package com.example.smartpantrymanager;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartpantrymanager.database.PantryDAO;
import com.example.smartpantrymanager.models.PantryItem;
import java.util.Calendar;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText ingredientNameEditText;
    private EditText quantityEditText;
    private EditText unitEditText;
    private EditText expiryDateEditText;

    private PantryDAO pantryDAO;

    private int pantryItemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        // Connect the form fields to the layout
        ingredientNameEditText = findViewById(R.id.ingredientNameEditText);
        quantityEditText = findViewById(R.id.quantityEditText);
        unitEditText = findViewById(R.id.unitEditText);
        expiryDateEditText = findViewById(R.id.expiryDateEditText);

        Button saveIngredientButton = findViewById(R.id.saveIngredientButton);
        Button cancelButton = findViewById(R.id.cancelButton);

        TextView formTitle = findViewById(R.id.formTitle);

        // Create the database access object
        pantryDAO = new PantryDAO(this);

        // Check if an existing item is being edited
        pantryItemId = getIntent().getIntExtra("pantryItemId", -1);

        if (pantryItemId != -1) {

            // Change the screen title when editing
            formTitle.setText("Edit Ingredient");

            // Load the existing pantry item
            loadPantryItem();
        }

        // Open the date picker when the expiry field is selected
        expiryDateEditText.setOnClickListener(v -> showDatePicker());

        // Save the pantry item
        saveIngredientButton.setOnClickListener(v -> saveIngredient());

        // Return to the pantry screen
        cancelButton.setOnClickListener(v -> finish());
    }

    private void loadPantryItem() {

        // Get the selected pantry item from SQLite
        PantryItem item = pantryDAO.getPantryItemById(pantryItemId);

        if (item != null) {

            ingredientNameEditText.setText(item.getName());
            quantityEditText.setText(String.valueOf(item.getQuantity()));
            unitEditText.setText(item.getUnit());
            expiryDateEditText.setText(item.getExpiryDate());
        }
    }

    private void saveIngredient() {

        // Get the values entered by the user
        String name = ingredientNameEditText.getText().toString().trim();
        String quantityText = quantityEditText.getText().toString().trim();
        String unit = unitEditText.getText().toString().trim();
        String expiryDate = expiryDateEditText.getText().toString().trim();

        // Check that the ingredient name was entered
        if (name.isEmpty()) {
            ingredientNameEditText.setError("Enter an ingredient name");
            ingredientNameEditText.requestFocus();
            return;
        }

        // Check that the quantity was entered
        if (quantityText.isEmpty()) {
            quantityEditText.setError("Enter a quantity");
            quantityEditText.requestFocus();
            return;
        }

        // Check that the unit was entered
        if (unit.isEmpty()) {
            unitEditText.setError("Enter a unit");
            unitEditText.requestFocus();
            return;
        }

        double quantity;

        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {

            // Show an error when the quantity is not a valid number
            quantityEditText.setError("Enter a valid quantity");
            quantityEditText.requestFocus();
            return;
        }

        // Quantity must be greater than zero
        if (quantity <= 0) {
            quantityEditText.setError("Quantity must be greater than zero");
            quantityEditText.requestFocus();
            return;
        }

        if (pantryItemId == -1) {

            // Create a new pantry item
            PantryItem item = new PantryItem(
                    name,
                    quantity,
                    unit,
                    expiryDate
            );

            long result = pantryDAO.insertPantryItem(item);

            if (result != -1) {
                Toast.makeText(
                        this,
                        "Ingredient added successfully",
                        Toast.LENGTH_SHORT
                ).show();

                finish();
            } else {
                Toast.makeText(
                        this,
                        "Could not add ingredient",
                        Toast.LENGTH_SHORT
                ).show();
            }

        } else {

            // Update the existing pantry item
            PantryItem item = new PantryItem(
                    pantryItemId,
                    name,
                    quantity,
                    unit,
                    expiryDate
            );

            int result = pantryDAO.updatePantryItem(item);

            if (result > 0) {
                Toast.makeText(
                        this,
                        "Ingredient updated successfully",
                        Toast.LENGTH_SHORT
                ).show();

                finish();
            } else {
                Toast.makeText(
                        this,
                        "Could not update ingredient",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    private void showDatePicker() {

        // Get the current date
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {

                    // Format the selected date
                    String date = String.format(
                            "%04d-%02d-%02d",
                            selectedYear,
                            selectedMonth + 1,
                            selectedDay
                    );

                    expiryDateEditText.setText(date);
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }
}