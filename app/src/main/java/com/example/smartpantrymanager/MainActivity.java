package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartpantrymanager.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Open the database and create the tables if needed
        DatabaseHelper databaseHelper =
                new DatabaseHelper(this);

        databaseHelper.getWritableDatabase();

        // Open the pantry screen
        Intent intent =
                new Intent(
                        MainActivity.this,
                        PantryActivity.class
                );

        startActivity(intent);

        // Close the launch screen so the back button does not return to it
        finish();
    }
}