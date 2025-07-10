package com.example.rehomemobileapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.rehomemobileapp.model.Category;
import com.example.rehomemobileapp.model.Province;
import com.example.rehomemobileapp.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

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
        prefs.edit()
                .putString("user_name", user.getName())
                .putBoolean("user_verified", user.isVerified())

                .apply();
    }

    public static String getUserName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        return prefs.getString("user_name", "Guest User");
    }

    public static boolean getIsVerified(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        return prefs.getBoolean("user_verified", false);
    }

    // In SessionManager.java

    public static void saveCategories(Context context, List<Category> categories) {
        SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        prefs.edit().putString("categories_json", gson.toJson(categories)).apply();
    }

    public static List<Category> getCategories(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        String json = prefs.getString("categories_json", "[]");
        Type type = new TypeToken<List<Category>>() {}.getType();
        return new Gson().fromJson(json, type);
    }

    public static void saveProvinces(Context context, List<Province> provinces) {
        SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        prefs.edit().putString("provinces_json", gson.toJson(provinces)).apply();
    }

    public static List<Province> getProvinces(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        String json = prefs.getString("provinces_json", "[]");
        Type type = new TypeToken<List<Province>>() {}.getType();
        return new Gson().fromJson(json, type);
    }

    public static void saveUserId(Context context, String userId) {
        SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        prefs.edit().putString("user_id", userId).apply();
    }

    public static String getUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        return prefs.getString("user_id", null);
    }

    public static void clearSession(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }


}
