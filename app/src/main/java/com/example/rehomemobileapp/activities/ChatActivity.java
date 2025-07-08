package com.example.rehomemobileapp.activities;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rehomemobileapp.adapter.ConversationListAdapter;
import com.example.rehomemobileapp.socket.SocketManager;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
public class ChatActivity extends AppCompatActivity implements SocketManager.SocketListener {
    private static final String TAG = "ChatActivity";

    private RecyclerView recyclerViewConversationList;
    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private ImageButton buttonSend;
    private LinearLayout layoutChatHeader;
    private LinearLayout layoutMessageInput;
    private TextView textViewUserAvatar;
    private TextView textViewUserName;

    private ConversationListAdapter conversationListAdapter;
    private MessageAdapter messageAdapter;
    private List<Conversation> conversationList;
    private List<Message> messageList;
    private Conversation currentConversation;
    private String currentUserId = "65d4a1b0c7e2b3f4a5b6c7d1"; // Placeholder - REPLACE WITH ACTUAL USER ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initViews();
        setupRecyclerViews();
        loadSampleData();
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
        recyclerViewConversationList = findViewById(R.id.recyclerViewChatList);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
        layoutChatHeader = findViewById(R.id.layoutChatHeader);
        layoutMessageInput = findViewById(R.id.layoutMessageInput);
        textViewUserAvatar = findViewById(R.id.textViewUserAvatar);
        textViewUserName = findViewById(R.id.textViewUserName);
    }

    private void setupRecyclerViews() {
        // Setup conversation list
        conversationList = new ArrayList<>();
        conversationListAdapter = new ConversationListAdapter(conversationList, new ConversationListAdapter.OnConversationClickListener() {
            @Override
            public void onConversationClick(Conversation conversation) {
                openConversation(conversation);
            }
        });

        recyclerViewConversationList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewConversationList.setAdapter(conversationListAdapter);

        // Setup messages
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);

        LinearLayoutManager messageLayoutManager = new LinearLayoutManager(this);
        messageLayoutManager.setStackFromEnd(true);
        recyclerViewMessages.setLayoutManager(messageLayoutManager);
        recyclerViewMessages.setAdapter(messageAdapter);
    }

    private void loadSampleData() {
        // Sample conversation data - simulating backend response
        // In real app, this would come from API call to getUserConversations()

        Conversation conversation1 = new Conversation();
        conversation1.setId("65d4a1b0c7e2b3f4a5b6c7d8");
        conversation1.setParticipants(Arrays.asList(currentUserId, "65d4a1b0c7e2b3f4a5b6c7d2"));
        conversation1.setCreatedAt(new Date());
        conversation1.setLastMessage("No message yet");

        Conversation conversation2 = new Conversation();
        conversation2.setId("65d4a1b0c7e2b3f4a5b6c7d9");
        conversation2.setParticipants(Arrays.asList(currentUserId, "65d4a1b0c7e2b3f4a5b6c7d3"));
        conversation2.setCreatedAt(new Date());
        conversation2.setLastMessage("No message yet");

        Conversation conversation3 = new Conversation();
        conversation3.setId("65d4a1b0c7e2b3f4a5b6c7e0");
        conversation3.setParticipants(Arrays.asList(currentUserId, "65d4a1b0c7e2b3f4a5b6c7d4"));
        conversation3.setCreatedAt(new Date());
        conversation3.setLastMessage("No message yet");

        conversationList.add(conversation1);
        conversationList.add(conversation2);
        conversationList.add(conversation3);

        // Process conversations for UI display (compute other participants)
        ConversationHelper.processConversationsForUI(conversationList, currentUserId);

        conversationListAdapter.notifyDataSetChanged();
    }

    private void setupClickListeners() {
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        editTextMessage.setOnEditorActionListener((v, actionId, event) -> {
            sendMessage();
            return true;
        });
    }

    private void openConversation(Conversation conversation) {
        // Validate that this is a proper 1-1 chat
        if (!ConversationHelper.isValidOneOnOneChat(conversation, currentUserId)) {
            Log.e(TAG, "Invalid 1-1 conversation: " + conversation.toString());
            return;
        }

        currentConversation = conversation;

        // Update header with other participant's info
        textViewUserAvatar.setText(conversation.getAvatar());
        textViewUserName.setText(conversation.getUserName());

        // Show chat interface
        layoutChatHeader.setVisibility(View.VISIBLE);
        layoutMessageInput.setVisibility(View.VISIBLE);

        // Join conversation on Socket.IO
        SocketManager.getInstance().emit("join_conversation", conversation.getId());

        // Load messages for this conversation
        loadMessagesForConversation(conversation.getId());
    }

    private void loadMessagesForConversation(String conversationId) {
        messageList.clear();

        // TODO: In real app, make API call to get messages
        // GET /api/conversations/{conversationId}/messages

        // Sample messages for demonstration
        if (conversationId.equals("65d4a1b0c7e2b3f4a5b6c7d8")) {
            Date now = new Date();
            messageList.add(new Message("msg1", conversationId, currentUserId, "Xin chào!", new Date(now.getTime() - 3600000), true, "", ""));
            messageList.add(new Message("msg2", conversationId, currentConversation.getOtherParticipantId(), "Chào bạn! Bạn khỏe không?", new Date(now.getTime() - 3000000), false, "", ""));
            messageList.add(new Message("msg3", conversationId, currentUserId, "Tôi khỏe, cảm ơn bạn!", new Date(now.getTime() - 1800000), true, "", ""));
        }

        messageAdapter.notifyDataSetChanged();
        if (messageList.size() > 0) {
            recyclerViewMessages.scrollToPosition(messageList.size() - 1);
        }
    }

    private void sendMessage() {
        String messageText = editTextMessage.getText().toString().trim();

        if (!TextUtils.isEmpty(messageText) && currentConversation != null) {
            JSONObject messageData = new JSONObject();
            try {
                messageData.put("conversationId", currentConversation.getId());
                messageData.put("senderId", currentUserId);
                messageData.put("text", messageText);
            } catch (JSONException e) {
                Log.e(TAG, "Error creating message JSON: " + e.getMessage());
                return;
            }

            // Send via Socket.IO
            SocketManager.getInstance().emit("send_message", messageData);

            // Add message to local list immediately for better UX
            Message newMessage = new Message(
                    String.valueOf(System.currentTimeMillis()), // Temp ID
                    currentConversation.getId(),
                    currentUserId,
                    messageText,
                    new Date(), // Current time
                    true, // Sent by current user
                    "",
                    ""
            );

            messageList.add(newMessage);
            messageAdapter.notifyItemInserted(messageList.size() - 1);
            recyclerViewMessages.scrollToPosition(messageList.size() - 1);

            // Update last message in conversation list
            currentConversation.setLastMessage(messageText);
            currentConversation.setLastMessageTime(new Date());
            conversationListAdapter.notifyDataSetChanged();

            editTextMessage.setText("");
        }
    }

    @Override
    public void onConnect() {
        Log.d(TAG, "Socket Connected");
        runOnUiThread(() -> {
            // Optional: Show connection status
        });
    }

    @Override
    public void onDisconnect() {
        Log.d(TAG, "Socket Disconnected");
        runOnUiThread(() -> {
            // Optional: Show disconnection status
        });
    }

    @Override
    public void onReceiveMessage(Message message) {
        Log.d(TAG, "Received message: " + message.getText());
        runOnUiThread(() -> {
            if (currentConversation != null && message.getConversationId().equals(currentConversation.getId())) {
                // Check if message is already in list to avoid duplicates
                boolean messageExists = false;
                for (Message m : messageList) {
                    if (m.getId() != null && m.getId().equals(message.getId())) {
                        messageExists = true;
                        break;
                    }
                }

                if (!messageExists) {
                    // Determine if the received message is from the current user or other participant
                    message.setSent(message.getSenderId().equals(currentUserId));
                    messageList.add(message);
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    recyclerViewMessages.scrollToPosition(messageList.size() - 1);

                    // Update last message in conversation list
                    currentConversation.setLastMessage(message.getText());
                    currentConversation.setLastMessageTime(message.getSentAt());
                    conversationListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onConnectError(String error) {
        Log.e(TAG, "Socket Connection Error: " + error);
        runOnUiThread(() -> {
            // Optional: Show error message to user
        });
    }
}
