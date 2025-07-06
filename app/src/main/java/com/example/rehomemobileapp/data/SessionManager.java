package com.example.rehomemobileapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.rehomemobileapp.model.User;

public class SessionManager {
    public static void saveAuthToken(Context context, String token) {
        SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        prefs.edit().putString("auth_token", token).apply();
    }

    public static String getAuthToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        return prefs.getString("auth_token", null);
    }

    public static void saveUser(Context context, User user) {
        SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        prefs.edit().putString("user_name", user.getName()).apply();
    }

}
