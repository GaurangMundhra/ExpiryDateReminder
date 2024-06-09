package com.anish.expirydatereminder;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddRecipeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText recipeNameEditText;
    private EditText recipeIngredientsEditText;
    private EditText recipePreparationEditText;
    private Button addImageButton;
    private Button shareRecipeButton;
    private Button postRecipeButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        recipeNameEditText = findViewById(R.id.recipe_name);
        recipeIngredientsEditText = findViewById(R.id.recipe_ingredients);
        recipePreparationEditText = findViewById(R.id.recipe_preparation);
        addImageButton = findViewById(R.id.add_image_button);
        shareRecipeButton = findViewById(R.id.share_recipe_button);
        postRecipeButton = findViewById(R.id.post_recipe_button);

        // Handle click events
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add image functionality
                addImage();
            }
        });

        shareRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Share recipe functionality
                shareRecipe();
            }
        });

        postRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Post recipe functionality
                postRecipe();
            }
        });
    }

    private void addImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            // Do something with the selected image URI
            // For example, you can set it to an ImageView
            // imageView.setImageURI(imageUri);
            Toast.makeText(getApplicationContext(), "Image added", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareRecipe() {
        // Implement share recipe functionality here
        // Get recipe data
        String recipeName = recipeNameEditText.getText().toString();
        String recipeIngredients = recipeIngredientsEditText.getText().toString();
        String recipePreparation = recipePreparationEditText.getText().toString();
        String recipeText = "Recipe: " + recipeName + "\nIngredients: " + recipeIngredients + "\nPreparation: " + recipePreparation;

        // Create the share intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, recipeText);

        // Start the activity for sharing
        startActivity(Intent.createChooser(shareIntent, "Share Recipe"));
    }

    // Inside postRecipe() method
    private void postRecipe() {
        // Get recipe data
        String recipeName = recipeNameEditText.getText().toString();
        String recipeIngredients = recipeIngredientsEditText.getText().toString();
        String recipePreparation = recipePreparationEditText.getText().toString();

        // Store recipe in database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, recipeName);
        values.put(DatabaseHelper.COLUMN_INGREDIENTS, recipeIngredients);
        values.put(DatabaseHelper.COLUMN_PREPARATION, recipePreparation);
        db.insert(DatabaseHelper.TABLE_RECIPES, null, values);
        db.close();

        // Show toast message
        Toast.makeText(getApplicationContext(), "Recipe is posted", Toast.LENGTH_SHORT).show();

        // Navigate to MainRecipesActivity
        Intent mainIntent = new Intent(AddRecipeActivity.this, MainRecipesActivity.class);
        startActivity(mainIntent);
        finish(); // Finish AddRecipeActivity
    }
}