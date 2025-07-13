package com.example.rehomemobileapp.network;

import android.content.Context;

import com.example.rehomemobileapp.MyApp;

import com.example.rehomemobileapp.data.SessionManager;
import com.example.rehomemobileapp.helpers.ConversationDeserializer;
import com.example.rehomemobileapp.model.Conversation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder();

                        String authToken = SessionManager.getAuthToken(MyApp.getAppContext());
                        if (authToken != null && !authToken.isEmpty()) {
                            requestBuilder.header("Authorization", "Bearer " + authToken);
                        }

                        return chain.proceed(requestBuilder.build());
                    })
                    .addInterceptor(logging)
                    .build();

            // Register your custom deserializer here 🔥
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Conversation.class, new ConversationDeserializer())
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiConstants.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit.create(ChatApiService.class);
    }
}
