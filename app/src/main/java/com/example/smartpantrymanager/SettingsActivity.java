package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch expiryAlertsSwitch;
    private Spinner unitSystemSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        // Find the settings controls
        expiryAlertsSwitch =
                findViewById(R.id.expiryAlertsSwitch);

        unitSystemSpinner =
                findViewById(R.id.unitSystemSpinner);

        // Create the unit system options
        String[] unitSystems = {
                "Metric",
                "Imperial"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        unitSystems
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        unitSystemSpinner.setAdapter(adapter);
    }
}