package com.example.rehomemobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.example.rehomemobileapp.activities.ProfileActivity;
import com.example.rehomemobileapp.activities.UploadPostActivity;
import com.example.rehomemobileapp.data.SessionManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rehomemobileapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Load user info from SessionManager
        NavigationView navigationView = binding.navView;
        View headerView = navigationView.getHeaderView(0);
        View headerContainer = headerView.findViewById(R.id.nav_header_container);
        TextView textName = headerView.findViewById(R.id.textViewUserName);
        TextView textVerified = headerView.findViewById(R.id.textViewVerified);

        // Add click listener to the header container
        if (headerContainer != null) {
            headerContainer.setOnClickListener(v -> {
                DrawerLayout drawer = binding.drawerLayout;
                drawer.closeDrawers();
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            });
        } else {
            // If headerContainer is null, set click listener on the whole headerView
            headerView.setOnClickListener(v -> {
                DrawerLayout drawer = binding.drawerLayout;
                drawer.closeDrawers();
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            });
        }

        String name = SessionManager.getUserName(this);
        boolean isVerified = SessionManager.getIsVerified(this);

        textName.setText(name);
        if (isVerified) {
            textVerified.setVisibility(View.GONE);
        } else {
            textVerified.setVisibility(View.VISIBLE);
            textVerified.setText("Người dùng chưa xác thực");
        }

        // Setup toolbar and nav
        setSupportActionBar(binding.appBarMain.toolbar);

        // 🟠 Mail icon FAB click
        binding.appBarMain.fab.setOnClickListener(view -> {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .setAnchorView(R.id.fab).show();
        });

        // 🟢 Plus icon overlay click → Open UploadPostActivity
        findViewById(R.id.icon_add_overlay).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, UploadPostActivity.class);
            startActivity(intent);
        });

        DrawerLayout drawer = binding.drawerLayout;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_market, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
