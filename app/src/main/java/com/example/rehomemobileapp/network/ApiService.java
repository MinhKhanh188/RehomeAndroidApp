package com.example.rehomemobileapp.network;

import com.example.rehomemobileapp.model.Post;
import com.example.rehomemobileapp.network.response.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.POST;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Field;

public interface ApiService {
    @GET("/api/posts/province")
    Call<List<Post>> getPostsByProvince(
            @Query("province") String province
    );

    @POST(ApiConstants.LOGIN)
    @FormUrlEncoded
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

}
