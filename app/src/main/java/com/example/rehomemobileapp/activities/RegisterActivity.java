package com.example.rehomemobileapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class RegisterActivity extends AppCompatActivity {

    private EditText nameInput, emailInput, passwordInput, confirmPasswordInput;
    private Button registerButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameInput = findViewById(R.id.registerEditTextName);
        emailInput = findViewById(R.id.registerEditTextEmail);
        passwordInput = findViewById(R.id.registerEditTextPassword);
        confirmPasswordInput = findViewById(R.id.registerEditTextConfirmPassword);
        registerButton = findViewById(R.id.buttonRegister);
        progressBar = new ProgressBar(this);
        progressBar.setVisibility(View.GONE);

        registerButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền tất cả các trường", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            register(name, email, password);
        });
    }

    private void register(String name, String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        ApiClient.getApiService().register(name, email, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    User user = response.body().getUser();

                    SessionManager.saveAuthToken(RegisterActivity.this, token);
                    SessionManager.saveUser(RegisterActivity.this, user);

                    // Navigate to main
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
