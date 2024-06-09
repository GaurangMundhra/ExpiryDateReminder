package com.anish.expirydatereminder;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewRecipesActivity extends AppCompatActivity {

    private ListView listView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipes);

        listView = findViewById(R.id.listView);
        dbHelper = new DatabaseHelper(this);

        // Display recipes
        displayRecipes();
    }

    private void displayRecipes() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_RECIPES,
                null,
                null,
                null,
                null,
                null,
                null
        );

        ArrayList<String> recipesList = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME);
                if (columnIndex != -1) {
                    do {
                        String recipeName = cursor.getString(columnIndex);
                        recipesList.add(recipeName);
                    } while (cursor.moveToNext());
                } else {
                    // Handle the case where the column doesn't exist
                    // You might want to log an error or take appropriate action
                }
            }
            cursor.close();
        }
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recipesList);
        listView.setAdapter(adapter);
    }
}
