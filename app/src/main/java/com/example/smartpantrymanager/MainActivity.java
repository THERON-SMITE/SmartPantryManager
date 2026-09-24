package com.example.smartpantrymanager;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.smartpantrymanager.database.DatabaseHelper;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    }
}