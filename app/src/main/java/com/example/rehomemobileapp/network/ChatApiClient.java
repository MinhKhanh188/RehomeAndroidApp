package com.example.rehomemobileapp.network;

import android.content.Context;

import com.example.rehomemobileapp.MyApp;

import com.example.rehomemobileapp.data.SessionManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatApiClient {
    private static Retrofit retrofit = null;

    public static ChatApiService getChatApiService() {
        if (retrofit == null) {
            // Create a logging interceptor
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY); // Đảm bảo là BODY

            // Create an OkHttpClient and add the interceptor
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();
                            Request.Builder requestBuilder = original.newBuilder();

                            // Add Authorization header if token exists
                            String authToken = SessionManager.getAuthToken(MyApp.getAppContext());
                            if (authToken != null && !authToken.isEmpty()) {
                                requestBuilder.header("Authorization", "Bearer " + authToken);
                            }

                            Request request = requestBuilder.build();
                            return chain.proceed(request);
                        }
                    })
                    .addInterceptor(logging) // Add logging interceptor
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiConstants.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ChatApiService.class);
    }
}