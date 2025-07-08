package com.example.rehomemobileapp.activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rehomemobileapp.MainActivity;
import com.example.rehomemobileapp.R;
import com.example.rehomemobileapp.data.SessionManager;
import com.example.rehomemobileapp.model.User;
import com.example.rehomemobileapp.network.ApiClient;
import com.example.rehomemobileapp.network.response.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton;
    private ProgressBar progressBar;
    private TextView registerNowText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.loginEditTextEmail);
        passwordInput = findViewById(R.id.loginEditTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        progressBar = findViewById(R.id.loadingIndicator);
        registerNowText = findViewById(R.id.textViewRegisterNow);

        registerNowText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });


        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            login(email, password);
        });
    }

    private void login(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        ApiClient.getApiService().login(email, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    User user = response.body().getUser();
                    Log.d("DEBUG", "User name = " + user.getName());


                    // Save to SessionManager
                    SessionManager.saveAuthToken(LoginActivity.this, token);
                    SessionManager.saveUser(LoginActivity.this, user);

                    // Navigate to home
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
