package com.anish.expirydatereminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();

        // Setup the navigation drawer
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Ensure ActionBar is not null before modifying
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ImageButton menuButton = findViewById(R.id.menu_icon);
        menuButton.setOnClickListener(v -> toggleDrawer());



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.nav_categories) {
                    startActivity(new Intent(DashboardActivity.this, ListItem.class));
                } else if (id == R.id.nav_recipes) {
                    startActivity(new Intent(DashboardActivity.this, MainRecipesActivity.class));
                } else if (id == R.id.nav_ngos) {
                    startActivity(new Intent(DashboardActivity.this, NGOActivity.class));
                } else if (id == R.id.nav_stores) {
                    startActivity(new Intent(DashboardActivity.this, StoresActivity.class));
                } else if (id == R.id.nav_contact_us) {
                    startActivity(new Intent(DashboardActivity.this, ContactFormActivity.class));
                }
                else if (id == R.id.nav_logout) {
                   logout();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        // Set up card click listeners
        LinearLayout categoriesCard = findViewById(R.id.card_categories);
        LinearLayout recipesCard = findViewById(R.id.card_recipes);
        LinearLayout ngosCard = findViewById(R.id.card_ngos);
        LinearLayout storesCard = findViewById(R.id.card_stores);

        categoriesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, ListItem.class));
            }
        });

        recipesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, MainRecipesActivity.class));
            }
        });

        ngosCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, NGOActivity.class));
            }
        });

        storesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, StoresActivity.class));
            }
        });

        // Set up the reviews section
        ViewPager2 viewPagerReviews = findViewById(R.id.view_pager_reviews);
        List<Review> reviewList = getReviewList();
        ReviewPagerAdapter reviewPagerAdapter = new ReviewPagerAdapter(reviewList);
        viewPagerReviews.setAdapter(reviewPagerAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<Review> getReviewList() {
        List<Review> reviews = new ArrayList<>();
        // Populate this list with data
        reviews.add(new Review("User 1","This is a review text", R.drawable.user_icon ));
        reviews.add(new Review("User 2","This is another review text",R.drawable.user_icon  ));
        // Add more reviews as needed
        return reviews;
    }

    private void toggleDrawer() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START);
        } else {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void logout() {
        mAuth.signOut();
        Intent intent = new Intent(DashboardActivity.this, Login.class);
        startActivity(intent);
        Toast.makeText(DashboardActivity.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}
