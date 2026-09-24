package com.example.smartpantrymanager.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.smartpantrymanager.models.PantryItem;
import java.util.ArrayList;
import java.util.List;

public class PantryDAO {
    private final DatabaseHelper databaseHelper;

    public PantryDAO(Context context) {
        // Create the database helper
        databaseHelper = new DatabaseHelper(context);
    }

    public long insertPantryItem(PantryItem item) {

        // Open the database for writing
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        // Add the pantry item values
        values.put("name", item.getName());
        values.put("quantity", item.getQuantity());
        values.put("unit", item.getUnit());
        values.put("expiryDate", item.getExpiryDate());

        // Insert the item into the pantry table
        long result = db.insert(DatabaseHelper.TABLE_PANTRY, null, values);

        db.close();

        return result;
    }

    public List<PantryItem> getAllPantryItems() {

        List<PantryItem> pantryItems = new ArrayList<>();

        // Open the database for reading
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_PANTRY,
                null,
                null,
                null,
                null,
                null,
                "name ASC"
        );

        // Read each pantry item from the database
        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            double quantity = cursor.getDouble(cursor.getColumnIndexOrThrow("quantity"));
            String unit = cursor.getString(cursor.getColumnIndexOrThrow("unit"));
            String expiryDate = cursor.getString(cursor.getColumnIndexOrThrow("expiryDate"));

            PantryItem item = new PantryItem(
                    id,
                    name,
                    quantity,
                    unit,
                    expiryDate
            );

            pantryItems.add(item);
        }

        cursor.close();
        db.close();

        return pantryItems;
    }

    public int updatePantryItem(PantryItem item) {

        // Open the database for writing
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        // Update the pantry item values
        values.put("name", item.getName());
        values.put("quantity", item.getQuantity());
        values.put("unit", item.getUnit());
        values.put("expiryDate", item.getExpiryDate());

        int result = db.update(
                DatabaseHelper.TABLE_PANTRY,
                values,
                "id = ?",
                new String[]{String.valueOf(item.getId())}
        );

        db.close();

        return result;
    }

    public int deletePantryItem(int id) {

        // Open the database for writing
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Delete the selected pantry item
        int result = db.delete(
                DatabaseHelper.TABLE_PANTRY,
                "id = ?",
                new String[]{String.valueOf(id)}
        );

        db.close();

        return result;
    }

    public PantryItem getPantryItemById(int id) {

        // Open the database for reading
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_PANTRY,
                null,
                "id = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        PantryItem item = null;

        // Read the selected pantry item
        if (cursor.moveToFirst()) {

            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            double quantity = cursor.getDouble(cursor.getColumnIndexOrThrow("quantity"));
            String unit = cursor.getString(cursor.getColumnIndexOrThrow("unit"));
            String expiryDate = cursor.getString(cursor.getColumnIndexOrThrow("expiryDate"));

            item = new PantryItem(
                    id,
                    name,
                    quantity,
                    unit,
                    expiryDate
            );
        }

        cursor.close();
        db.close();

        return item;
    }
}
