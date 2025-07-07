package com.example.rehomemobileapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.rehomemobileapp.R;
import com.example.rehomemobileapp.adapter.ImageAdapter;
import com.example.rehomemobileapp.data.SessionManager;
import com.example.rehomemobileapp.model.Post;
import com.example.rehomemobileapp.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import android.content.Intent;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Locale;

import retrofit2.Response; // This is the main one!


public class PostDetailActivity extends AppCompatActivity {

    private RecyclerView productImagesRecycler;
    private TextView productTitle, productPrice, productDescription;
    private Button buttonChat;
    private ProgressBar progressBar;

    private String sellerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        productImagesRecycler = findViewById(R.id.productImagesRecycler);
        productTitle = findViewById(R.id.productTitle);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDescription);
        buttonChat = findViewById(R.id.buttonChat);

        String postId = getIntent().getStringExtra("POST_ID");

        fetchPostDetail(postId);

//        buttonChat.setOnClickListener(v -> startChat());
    }

    private void fetchPostDetail(String id) {
        String token = SessionManager.getAuthToken(this);
        ApiClient.getApiService().getPostDetail(id)
                .enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Post post = response.body();

                            productTitle.setText(post.getName());
                            productPrice.setText(
                                    String.format(Locale.getDefault(), "%,.0f VND", post.getPrice()));
                            productDescription.setText(post.getDescription());

                            // Load  image
                            if (post.getImages() != null && !post.getImages().isEmpty()) {
                                productImagesRecycler.setLayoutManager(
                                        new LinearLayoutManager(PostDetailActivity.this, LinearLayoutManager.HORIZONTAL, false)
                                );
                                ImageAdapter adapter = new ImageAdapter(post.getImages());
                                productImagesRecycler.setAdapter(adapter);
                            }


                            sellerId = post.getSellerId();
                        }
                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {
                        Toast.makeText(PostDetailActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                });
    }

//    private void startChat() {
//        if (sellerId == null) {
//            Toast.makeText(this, "Không có thông tin người bán", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String token = SessionManager.getAuthToken(this);
//
//        ApiClient.getApiService()
//                .joinConversation(new JoinConversationRequest(sellerId))
//                .enqueue(new Callback<ConversationResponse>() {
//                    @Override
//                    public void onResponse(Call<ConversationResponse> call, Response<ConversationResponse> response) {
//                        if (response.isSuccessful()) {
//                            String conversationId = response.body().getId();
//                            Intent intent = new Intent(PostDetailActivity.this, ChatActivity.class);
//                            intent.putExtra("CONVERSATION_ID", conversationId);
//                            startActivity(intent);
//                        } else {
//                            Toast.makeText(PostDetailActivity.this, "Không thể bắt đầu trò chuyện", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ConversationResponse> call, Throwable t) {
//                        Toast.makeText(PostDetailActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
}
