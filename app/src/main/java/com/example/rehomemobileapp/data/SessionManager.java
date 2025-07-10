package com.example.rehomemobileapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.rehomemobileapp.model.User;

public class SessionManager {
    private static final String PREF_NAME = "app_prefs";
    private static final String KEY_AUTH_TOKEN = "auth_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";

    public static void saveAuthToken(Context context, String token) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_AUTH_TOKEN, token).apply();
    }

    public static String getAuthToken(Context context) {
        if (context == null) {
            return null;
        }
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_AUTH_TOKEN, null);
    }

    public static void saveUser(Context context, User user) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_ID, user.get_id());
        editor.commit();
    }

    public static String getUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USER_ID, null);
    }

    // Thêm phương thức để xóa session khi đăng xuất
    public static void clearSession(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().commit();
    }
}
