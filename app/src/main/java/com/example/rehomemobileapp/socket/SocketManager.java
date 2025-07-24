package com.example.rehomemobileapp.socket;

import android.util.Log;

import com.example.rehomemobileapp.activities.ChatActivity;
import com.example.rehomemobileapp.model.Message;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketManager {

    private static final String TAG = "SocketManager";
    private static final String SOCKET_URL = "https://rehome.id.vn";

    private static SocketManager instance;
    private Socket socket;
    private static SocketListener listener;

    private SocketManager() {
        try {
            IO.Options options = new IO.Options();
            options.reconnection = true;
            socket = IO.socket(SOCKET_URL, options);

            // ✅ Listen only ONCE for receive_message
            socket.on("receive_message", args -> {
                Log.d(TAG, "📨 [receive_message] socket triggered");

                if (listener != null && args.length > 0) {
                    Log.d(TAG, "🎯 Listener is: " + listener.getClass().getSimpleName());
                    Log.d(TAG, "🧾 Raw socket data: " + args[0]);

                    try {
                        JSONObject json = (JSONObject) args[0];
                        Message message = new Gson().fromJson(json.toString(), Message.class);
                        Log.d(TAG, "📥 Parsed message: " + message.getText() + " from " + message.getSenderId());

                        listener.onReceiveMessage(message);
                        Log.d(TAG, "✅ onReceiveMessage called");
                    } catch (Exception e) {
                        Log.e(TAG, "❌ Error parsing message: " + e.getMessage());
                    }
                } else {
                    Log.w(TAG, "⚠️ No listener or empty args");
                }
            });

        } catch (URISyntaxException e) {
            Log.e(TAG, "❌ Socket URI error: " + e.getMessage());
        }
    }

    public static synchronized SocketManager getInstance() {
        if (instance == null) {
            instance = new SocketManager();
        }
        return instance;
    }

    public void connect() {
        if (socket != null && !socket.connected()) {
            Log.d(TAG, "🔌 Connecting socket...");
            socket.connect();

            socket.on(Socket.EVENT_CONNECT, args -> {
                Log.d(TAG, "✅ Socket connected");

                if (listener != null) {
                    Log.d(TAG, "📲 onConnect called for listener: " + listener.getClass().getSimpleName());
                    listener.onConnect();
                }

                // Rejoin conversation if listener is ChatActivity
                if (listener instanceof ChatActivity) {
                    ChatActivity activity = (ChatActivity) listener;
                    String conversationId = activity.getCurrentConversationId();
                    if (conversationId != null) {
                        emit("join_conversation", conversationId);
                        Log.d(TAG, "📡 Re-joining conversation: " + conversationId);
                    }
                }
            });

            socket.on(Socket.EVENT_DISCONNECT, args -> {
                Log.d(TAG, "❎ Socket disconnected");
                if (listener != null) listener.onDisconnect();
            });

            socket.on(Socket.EVENT_CONNECT_ERROR, args -> {
                Log.e(TAG, "❌ Socket connect error: " + (args.length > 0 ? args[0].toString() : "Unknown"));
                if (listener != null) {
                    listener.onConnectError(args.length > 0 ? args[0].toString() : "Unknown");
                }
            });

        } else {
            Log.d(TAG, "⚠️ Socket already connected or null");
        }
    }

    public void disconnect() {
        if (socket != null) {
            Log.d(TAG, "🔌 Disconnecting socket");
            socket.disconnect();
        }
    }

    public void emit(String event, Object data) {
        if (socket != null && socket.connected()) {
            Log.d(TAG, "📤 Emitting event '" + event + "' with data: " + data.toString());
            socket.emit(event, data);
        } else {
            Log.w(TAG, "⚠️ Cannot emit, socket not connected");
        }
    }

    public static void setListener(SocketListener newListener) {
        Log.d(TAG, "🎧 Setting new listener: " + (newListener != null ? newListener.getClass().getSimpleName() : "null"));
        listener = newListener;
    }

    public static void clearListener() {
        Log.d(TAG, "🧹 Clearing listener");
        listener = null;
    }

    public interface SocketListener {
        void onConnect();
        void onDisconnect();
        void onReceiveMessage(Message message);
        void onConnectError(String error);
    }
}
