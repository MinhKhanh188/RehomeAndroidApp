package com.example.rehomemobileapp.network;

import com.example.rehomemobileapp.model.Category;
import com.example.rehomemobileapp.model.Post;
import com.example.rehomemobileapp.model.Province;
import com.example.rehomemobileapp.network.request.StatusUpdateRequest;
import com.example.rehomemobileapp.network.response.LoginResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.POST;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Field;

public interface ApiService {

    @GET(ApiConstants.GET_ALL_CATEGORY)
    Call<List<Category>> getAllCategories();

    @GET(ApiConstants.GET_ALL_PROVINCE)
    Call<List<Province>> getAllProvinces();

    @GET(ApiConstants.GET_POST_BY_PROVINCE)
    Call<List<Post>> getPostsByProvince(
            @Query("province") String province
    );

    // Login
    @POST(ApiConstants.LOGIN)
    @FormUrlEncoded
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

    // Register
    @POST("/api/users/register")
    @FormUrlEncoded
    Call<LoginResponse> register(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password
    );

    @GET(ApiConstants.GET_POST_DETAIL_BY_ID + "/{id}")
    Call<Post> getPostDetail(@Path("id") String id);

    // ✅ Get current user posts
    @GET(ApiConstants.GET_USER_POSTS)
    Call<List<Post>> getUserPosts(
            @Header("Authorization") String authHeader
    );

    // ✅ Delete a post by ID
    @DELETE(ApiConstants.DELETE_MY_POST + "{id}")
    Call<Void> deletePost(
            @Path("id") String id,
            @Header("Authorization") String authHeader
    );

    @PUT(ApiConstants.CHANGE_POST_STATUS + "{id}")
    Call<Void> updatePostStatus(
            @Path("id") String postId,
            @Body StatusUpdateRequest request,
            @Header("Authorization") String authHeader
    );

    @Multipart
    @POST(ApiConstants.CREATE_POST)
    Call<Void> uploadPost(
            @Header("Authorization") String token,
            @Part("name") RequestBody name,
            @Part("description") RequestBody description,
            @Part("price") RequestBody price,
            @Part("originalPrice") RequestBody originalPrice,
            @Part("address") RequestBody address,
            @Part("categoryId") RequestBody categoryId,
            @Part("province") RequestBody province,
            @Part("productStatus") RequestBody productStatus, // ✅ use the correct field name
            @Part List<MultipartBody.Part> images
    );



}
