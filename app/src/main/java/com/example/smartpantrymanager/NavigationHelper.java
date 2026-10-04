package com.example.smartpantrymanager;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.Menu;

// Bottom navigation bar that is shared by every screen
public class NavigationHelper {

    // Used by screens that are not one of the three tabs
    public static final int NO_TAB = 0;

    public static void setup(AppCompatActivity activity, int currentItemId) {

        BottomNavigationView bottomNavigation =
                activity.findViewById(R.id.bottomNavigation);

        if (currentItemId == NO_TAB) {
            // This screen is not a tab, so no item is highlighted
            Menu menu = bottomNavigation.getMenu();
            menu.setGroupCheckable(0, true, false);

            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setChecked(false);
            }

            menu.setGroupCheckable(0, true, true);
        } else {
            // Highlight the tab that this screen belongs to
            bottomNavigation.setSelectedItemId(currentItemId);
        }

        bottomNavigation.setOnItemSelectedListener(item -> {

            int selectedId = item.getItemId();

            // Determine which screen the tapped tab opens
            Class<?> destination;

            if (selectedId == R.id.nav_recipes) {
                destination = SuggestedRecipesActivity.class;
            } else if (selectedId == R.id.nav_settings) {
                destination = SettingsActivity.class;
            } else {
                destination = PantryActivity.class;
            }

            // Already on that screen, so there is nothing to open
            if (activity.getClass() == destination) {
                return true;
            }

            // Open the screen with an Intent. If it is already open in the background, return to it and close the screens above it.
            Intent intent = new Intent(activity, destination);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivity(intent);

            // Keep this screen's own tab highlighted
            return false;
        });
    }
}