package com.example.smartpantrymanager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch expiryAlertsSwitch;
    private Spinner unitSystemSpinner;
    private SharedPreferences preferences;
    public static final String PREFS_NAME = "smart_pantry_settings";
    public static final String KEY_EXPIRY_ALERTS = "expiry_alerts";
    public static final String KEY_UNIT_SYSTEM = "unit_system";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_settings);

        // Find the settings controls
        expiryAlertsSwitch = findViewById(R.id.expiryAlertsSwitch);
        unitSystemSpinner = findViewById(R.id.unitSystemSpinner);

        // Open the saved settings
        preferences = getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
        );

        // Create the unit system options
        String[] unitSystems = {
                "Metric",
                "Imperial"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                unitSystems
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        unitSystemSpinner.setAdapter(adapter);

        // Load the saved expiry alert setting
        boolean expiryAlertsEnabled = preferences.getBoolean(
                KEY_EXPIRY_ALERTS,
                true
        );

        expiryAlertsSwitch.setChecked(expiryAlertsEnabled);

        // Load the saved unit system
        String savedUnitSystem = preferences.getString(
                KEY_UNIT_SYSTEM,
                "Metric"
        );

        if (savedUnitSystem.equals("Imperial")) {
            unitSystemSpinner.setSelection(1);
        } else {
            unitSystemSpinner.setSelection(0);
        }

        // Save the expiry alert setting when changed
        expiryAlertsSwitch.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    preferences.edit()
                            .putBoolean(
                                    KEY_EXPIRY_ALERTS,
                                    isChecked
                            )
                            .apply();
                }
        );

        // Save the unit system when changed
        unitSystemSpinner.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            android.widget.AdapterView<?> parent,
                            android.view.View view,
                            int position,
                            long id) {

                        String selectedUnitSystem =
                                unitSystems[position];

                        preferences.edit()
                                .putString(
                                        KEY_UNIT_SYSTEM,
                                        selectedUnitSystem
                                )
                                .apply();
                    }

                    @Override
                    public void onNothingSelected(
                            android.widget.AdapterView<?> parent) {
                        // Nothing selected
                    }
                }
        );

        // Bottom navigation bar
        NavigationHelper.setup(this, R.id.nav_settings);
    }
}