package com.example.rehomemobileapp;

import android.app.Application;
import android.content.Context;

import com.example.rehomemobileapp.network.ChatApiClient;

public class MyApp extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }
}

