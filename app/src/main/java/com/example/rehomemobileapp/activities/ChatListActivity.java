package com.example.rehomemobileapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rehomemobileapp.R;
import com.example.rehomemobileapp.adapter.ConversationListAdapter;
import com.example.rehomemobileapp.data.SessionManager;
import com.example.rehomemobileapp.helpers.ChatHelper;
import com.example.rehomemobileapp.helpers.ConversationHelper;
import com.example.rehomemobileapp.model.Conversation;

import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity {

    private static final String TAG = "ChatListActivity";

    private RecyclerView recyclerViewConversationList;
    private ConversationListAdapter conversationListAdapter;
    private List<Conversation> conversationList;
    private String currentUserId;
    private ChatHelper chatHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list); // Create this layout file

        initViews();
        initData();
        setupRecyclerView();
        loadUserConversations();
    }

    private void initViews() {
        recyclerViewConversationList = findViewById(R.id.recyclerViewChatList);
    }

    private void initData() {
        currentUserId = SessionManager.getUserId(getApplicationContext());

        String debugAuthToken = SessionManager.getAuthToken(getApplicationContext());
        Log.d(TAG, "Debug: Auth Token from SessionManager: " + (debugAuthToken != null ? debugAuthToken : "NULL"));
        Log.d(TAG, "Debug: User ID from SessionManager: " + (currentUserId != null ? currentUserId : "NULL"));

        if (debugAuthToken == null || TextUtils.isEmpty(currentUserId)) {
            Toast.makeText(this, "Lỗi: Token hoặc User ID không có. Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            // Redirect to LoginActivity if session is invalid
            Intent intent = new Intent(ChatListActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        chatHelper = new ChatHelper(this);
    }

    private void setupRecyclerView() {
        conversationList = new ArrayList<>();
        conversationListAdapter = new ConversationListAdapter(conversationList, new ConversationListAdapter.OnConversationClickListener() {
            @Override
            public void onConversationClick(Conversation conversation) {
                openChatActivity(conversation);
            }
        });

        recyclerViewConversationList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewConversationList.setAdapter(conversationListAdapter);
    }

    private void loadUserConversations() {
        chatHelper.getUserConversations(new ChatHelper.ChatCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                runOnUiThread(() -> {
                    conversationList.clear();
                    conversationList.addAll(conversations);

                    // Process conversations for UI display
                    ConversationHelper.processConversationsForUI(conversationList, currentUserId);

                    conversationListAdapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Log.e(TAG, "Error loading conversations: " + error);
                    Toast.makeText(ChatListActivity.this, "Không thể tải danh sách cuộc hội thoại", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void openChatActivity(Conversation conversation) {
        // Pass conversation details to ChatActivity
        Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
        intent.putExtra("conversationId", conversation.getId());
        intent.putExtra("otherParticipantId", conversation.getOtherParticipantId());
        intent.putExtra("otherParticipantName", conversation.getOtherParticipantName());
        intent.putExtra("otherParticipantAvatar", conversation.getOtherParticipantAvatar());
        startActivity(intent);
    }
}