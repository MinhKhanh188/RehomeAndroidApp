package com.example.rehomemobileapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rehomemobileapp.activities.LoginActivity;
import com.example.rehomemobileapp.data.SessionManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String token = SessionManager.getAuthToken(this);

        if (token != null && !token.isEmpty()) {
            // Already logged in
            startActivity(new Intent(this, MainActivity.class));
        } else {
            // Not logged in
            startActivity(new Intent(this, LoginActivity.class));
        }

        finish();
    }
}
