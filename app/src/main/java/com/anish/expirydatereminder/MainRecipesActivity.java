package com.anish.expirydatereminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainRecipesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        Button addRecipeButton = findViewById(R.id.add_recipe_button);
        Button viewRecipesButton = findViewById(R.id.view_recipes_button);

        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainRecipesActivity.this, AddRecipeActivity.class);
                startActivity(intent);
            }
        });

        viewRecipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainRecipesActivity.this, ViewRecipesActivity.class);
                startActivity(intent);
            }
        });
    }
}

