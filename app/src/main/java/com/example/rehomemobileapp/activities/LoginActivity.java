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
import com.example.rehomemobileapp.utils.JwtUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;



public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
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

        // Setup Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // From Firebase
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Set onClick listener
        findViewById(R.id.buttonGoogle).setOnClickListener(view -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
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
                    SessionManager.saveUserId(LoginActivity.this, JwtUtils.getUserIdFromToken(token));

                    Log.d("DEBUG", "User id = " + JwtUtils.getUserIdFromToken(token));

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
    private void loginWithGoogle(String idToken) {
        progressBar.setVisibility(View.VISIBLE);
        ApiClient.getApiService().loginWithGoogle(idToken).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    User user = response.body().getUser();

                    SessionManager.saveAuthToken(LoginActivity.this, token);
                    SessionManager.saveUser(LoginActivity.this, user);
                    SessionManager.saveUserId(LoginActivity.this, JwtUtils.getUserIdFromToken(token));

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "null";
                        Log.e("GoogleLogin", "Login failed: HTTP " + response.code() + ", error: " + errorBody);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, "Đăng nhập bằng Google thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("GoogleLogin", "Connection error: " + t.getMessage(), t);
                Toast.makeText(LoginActivity.this, "Lỗi kết nối khi đăng nhập Google", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("GoogleLogin", "GoogleSignInAccount: " + account);

                if (account != null) {
                    String idToken = account.getIdToken();
                    Log.d("GoogleLogin", "ID Token: " + idToken);

                    if (idToken != null) {
                        loginWithGoogle(idToken);
                    } else {
                        Toast.makeText(this, "ID Token null", Toast.LENGTH_SHORT).show();
                        Log.e("GoogleLogin", "ID Token is null. Check GoogleSignInOptions config.");
                    }
                }
            } catch (ApiException e) {
                Log.e("GoogleLogin", "Google sign-in failed", e);
                Toast.makeText(this, "Google login failed", Toast.LENGTH_SHORT).show();
            }
        }
    }



}
