package com.example.rehomemobileapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rehomemobileapp.activities.LoginActivity;
import com.example.rehomemobileapp.data.SessionManager;
import com.example.rehomemobileapp.model.Category;
import com.example.rehomemobileapp.model.Province;
import com.example.rehomemobileapp.network.ApiClient;
import com.example.rehomemobileapp.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApiService apiService = ApiClient.getApiService();

        Call<List<Category>> categoryCall = apiService.getAllCategories();
        Call<List<Province>> provinceCall = apiService.getAllProvinces();

        // Fetch categories first
        categoryCall.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SessionManager.saveCategories(SplashActivity.this, response.body());

                    // Fetch provinces after categories
                    provinceCall.enqueue(new Callback<List<Province>>() {
                        @Override
                        public void onResponse(Call<List<Province>> call, Response<List<Province>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                SessionManager.saveProvinces(SplashActivity.this, response.body());
                            }
                            launchNextScreen(); // continue either way
                        }

                        @Override
                        public void onFailure(Call<List<Province>> call, Throwable t) {
                            launchNextScreen();
                        }
                    });
                } else {
                    launchNextScreen();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                launchNextScreen();
            }
        });
    }

    private void launchNextScreen() {
        String token = SessionManager.getAuthToken(this);
        Intent intent = new Intent(this, token != null && !token.isEmpty() ? MainActivity.class : LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
