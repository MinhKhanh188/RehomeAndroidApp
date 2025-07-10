package com.example.rehomemobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.example.rehomemobileapp.activities.ChatActivity;
import com.example.rehomemobileapp.activities.ChatListActivity;
import com.example.rehomemobileapp.activities.LoginActivity;
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
        TextView textName = headerView.findViewById(R.id.textViewUserName);
        TextView textVerified = headerView.findViewById(R.id.textViewVerified);

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
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_market, R.id.nav_slideshow, R.id.nav_chat)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_chat) {
                Intent intent = new Intent(MainActivity.this, ChatListActivity.class);
                startActivity(intent);
                drawer.closeDrawers(); // Close the drawer after selection
                return true;
            } else if (id == R.id.nav_logout) {
                // Xóa session và chuyển về màn hình đăng nhập
                com.example.rehomemobileapp.data.SessionManager.clearSession(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa tất cả các activity trước đó
                startActivity(intent);
                finish();
                drawer.closeDrawers();
                return true;
            } else {
                return NavigationUI.onNavDestinationSelected(item, navController);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
