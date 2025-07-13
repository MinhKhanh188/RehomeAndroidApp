package com.example.rehomemobileapp.network.response;

import com.example.rehomemobileapp.model.User;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    private User user;
    private String token;

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }
}
