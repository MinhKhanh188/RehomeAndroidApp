package com.example.rehomemobileapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rehomemobileapp.R;
import com.example.rehomemobileapp.adapter.MessageAdapter;
import com.example.rehomemobileapp.data.SessionManager;
import com.example.rehomemobileapp.helpers.ChatHelper;
import com.example.rehomemobileapp.model.Conversation;
import com.example.rehomemobileapp.model.Message;
import com.example.rehomemobileapp.socket.SocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements SocketManager.SocketListener {
    private static final String TAG = "ChatActivity";

    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private ImageButton buttonSend;
    private LinearLayout layoutChatHeader;
    private LinearLayout layoutMessageInput;
    private TextView textViewUserAvatar;
    private TextView textViewUserName;

    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private Conversation currentConversation;
    private String currentUserId;
    private ChatHelper chatHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initViews();
        initData();
        setupRecyclerView();
        setupClickListeners();

        SocketManager.getInstance().setListener(this);
        SocketManager.getInstance().connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketManager.getInstance().disconnect();
    }

    private void initViews() {
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
        layoutChatHeader = findViewById(R.id.layoutChatHeader);
        layoutMessageInput = findViewById(R.id.layoutMessageInput);
        textViewUserAvatar = findViewById(R.id.textViewUserAvatar);
        textViewUserName = findViewById(R.id.textViewUserName);

        // Handle back button
        findViewById(R.id.buttonBack).setOnClickListener(v -> finish());
    }

    private void initData() {
        currentUserId = SessionManager.getUserId(getApplicationContext());

        String debugAuthToken = SessionManager.getAuthToken(this);
        Log.d(TAG, "Debug: Auth Token from SessionManager: " + (debugAuthToken != null ? debugAuthToken : "NULL"));
        Log.d(TAG, "Debug: User ID from SessionManager: " + (currentUserId != null ? currentUserId : "NULL"));

        if (debugAuthToken == null || TextUtils.isEmpty(currentUserId)) {
            Toast.makeText(this, "Lỗi: Token hoặc User ID không có. Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        chatHelper = new ChatHelper(this);

        // Get conversation details from intent
        Intent intent = getIntent();
        String conversationId = intent.getStringExtra("conversationId");
        String otherParticipantId = intent.getStringExtra("otherParticipantId");
        String otherParticipantName = intent.getStringExtra("otherParticipantName");
        String otherParticipantAvatar = intent.getStringExtra("otherParticipantAvatar");

        if (conversationId == null || otherParticipantId == null) {
            Toast.makeText(this, "Lỗi: Không có thông tin cuộc hội thoại.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        currentConversation = new Conversation();
        currentConversation.setId(conversationId);
        currentConversation.setOtherParticipantId(otherParticipantId);
        currentConversation.setOtherParticipantName(otherParticipantName);
        currentConversation.setOtherParticipantAvatar(otherParticipantAvatar);
        currentConversation.setDisplayName(otherParticipantName);
        currentConversation.setAvatar(otherParticipantAvatar);

        // Update header with other participant's info
        textViewUserAvatar.setText(currentConversation.getAvatar());
        textViewUserName.setText(currentConversation.getUserName());

        // Show chat interface
        layoutChatHeader.setVisibility(View.VISIBLE);
        layoutMessageInput.setVisibility(View.VISIBLE);

        // Join conversation on Socket.IO
        SocketManager.getInstance().emit("join_conversation", currentConversation.getId());

        // Load messages for this conversation
        loadMessagesForConversation(currentConversation.getId());
    }

    private void setupRecyclerView() {
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);

        LinearLayoutManager messageLayoutManager = new LinearLayoutManager(this);
        messageLayoutManager.setStackFromEnd(true);
        recyclerViewMessages.setLayoutManager(messageLayoutManager);
        recyclerViewMessages.setAdapter(messageAdapter);
    }

    private void setupClickListeners() {
        buttonSend.setOnClickListener(v -> sendMessage());

        editTextMessage.setOnEditorActionListener((v, actionId, event) -> {
            sendMessage();
            return true;
        });
    }

    private void loadMessagesForConversation(String conversationId) {
        chatHelper.getMessages(conversationId, new ChatHelper.ChatCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> messages) {
                runOnUiThread(() -> {
                    messageList.clear();
                    for (Message message : messages) {
                        message.setSent(message.getSenderId().equals(currentUserId));
                    }
                    messageList.addAll(messages);
                    messageAdapter.notifyDataSetChanged();
                    if (messageList.size() > 0) {
                        recyclerViewMessages.scrollToPosition(messageList.size() - 1);
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Log.e(TAG, "Error loading messages: " + error);
                    Toast.makeText(ChatActivity.this, "Không thể tải tin nhắn", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void sendMessage() {
        String messageText = editTextMessage.getText().toString().trim();

        if (!TextUtils.isEmpty(messageText) && currentConversation != null) {
            Message newMessage = new Message(
                    String.valueOf(System.currentTimeMillis()),
                    currentConversation.getId(),
                    currentUserId,
                    messageText,
                    new Date(),
                    true,
                    "",
                    ""
            );

            messageList.add(newMessage);
            messageAdapter.notifyItemInserted(messageList.size() - 1);
            recyclerViewMessages.scrollToPosition(messageList.size() - 1);
            editTextMessage.setText("");

            chatHelper.sendMessage(currentConversation.getId(), messageText, new ChatHelper.ChatCallback<Message>() {
                @Override
                public void onSuccess(Message message) {
                    Log.d(TAG, "Message sent successfully via API");
                    runOnUiThread(() -> {
                        currentConversation.setLastMessage(messageText);
                        currentConversation.setLastMessageTime(new Date());
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        Log.e(TAG, "Error sending message via API: " + error);
                        Toast.makeText(ChatActivity.this, "Không thể gửi tin nhắn", Toast.LENGTH_SHORT).show();
                        if (messageList.size() > 0 && messageList.get(messageList.size() - 1).getId().equals(newMessage.getId())) {
                            messageList.remove(messageList.size() - 1);
                            messageAdapter.notifyItemRemoved(messageList.size());
                        }
                    });
                }
            });

            JSONObject messageData = new JSONObject();
            try {
                messageData.put("conversationId", currentConversation.getId());
                messageData.put("senderId", currentUserId);
                messageData.put("text", messageText);
                SocketManager.getInstance().emit("send_message", messageData);
            } catch (JSONException e) {
                Log.e(TAG, "Error creating message JSON: " + e.getMessage());
            }
        }
    }

    @Override
    public void onConnect() {
        Log.d(TAG, "Socket Connected");
    }

    @Override
    public void onDisconnect() {
        Log.d(TAG, "Socket Disconnected");
    }

    @Override
    public void onReceiveMessage(Message message) {
        Log.d(TAG, "Received message: " + message.getText());
        runOnUiThread(() -> {
            if (currentConversation != null && message.getConversationId().equals(currentConversation.getId())) {
                boolean messageExists = false;
                for (Message m : messageList) {
                    if (m.getId() != null && m.getId().equals(message.getId())) {
                        messageExists = true;
                        break;
                    }
                }

                if (!messageExists) {
                    message.setSent(message.getSenderId().equals(currentUserId));
                    messageList.add(message);
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    recyclerViewMessages.scrollToPosition(messageList.size() - 1);

                    currentConversation.setLastMessage(message.getText());
                    currentConversation.setLastMessageTime(message.getSentAt());
                }
            }
        });
    }

    @Override
    public void onConnectError(String error) {
        Log.e(TAG, "Socket Connection Error: " + error);
        runOnUiThread(() -> {
            Toast.makeText(this, "Lỗi kết nối real-time", Toast.LENGTH_SHORT).show();
        });
    }

    public void startConversationWithUser(String participantId) {
        chatHelper.createOrGetConversation(participantId, new ChatHelper.ChatCallback<Conversation>() {
            @Override
            public void onSuccess(Conversation conversation) {
                runOnUiThread(() -> {
                    // This method is now handled by ChatListActivity, so this part is simplified
                    // to just open the conversation if it's called directly.
                    // In a real scenario, this might be triggered from a user profile, etc.
                    currentConversation = conversation;
                    textViewUserAvatar.setText(currentConversation.getAvatar());
                    textViewUserName.setText(currentConversation.getUserName());
                    layoutChatHeader.setVisibility(View.VISIBLE);
                    layoutMessageInput.setVisibility(View.VISIBLE);
                    SocketManager.getInstance().emit("join_conversation", currentConversation.getId());
                    loadMessagesForConversation(currentConversation.getId());
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Log.e(TAG, "Error creating conversation: " + error);
                    Toast.makeText(ChatActivity.this, "Không thể tạo cuộc hội thoại", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}


