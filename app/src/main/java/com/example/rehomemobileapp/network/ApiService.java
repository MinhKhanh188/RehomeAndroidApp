package com.example.rehomemobileapp.network;

import com.example.rehomemobileapp.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/api/posts/province")
    Call<List<Post>> getPostsByProvince(
            @Query("province") String province
    );
}
