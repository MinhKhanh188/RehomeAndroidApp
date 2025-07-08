package com.example.rehomemobileapp.socket;
import com.example.rehomemobileapp.model.Message;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class SocketManager {

    private Socket mSocket;
    private static SocketManager instance;
    private SocketListener listener;

    public interface SocketListener {
        void onConnect();
        void onDisconnect();
        void onReceiveMessage(Message message);
        void onConnectError(String error);
    }

    private SocketManager() {
        try {
            // Replace with your backend URL
            mSocket = IO.socket("http://localhost:5000"); // This needs to be the actual backend URL
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized SocketManager getInstance() {
        if (instance == null) {
            instance = new SocketManager();
        }
        return instance;
    }

    public void setListener(SocketListener listener) {
        this.listener = listener;
    }

    public void connect() {
        mSocket.connect();
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on("receive_message", onReceiveMessage);
    }

    public void disconnect() {
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off("receive_message", onReceiveMessage);
    }

    public void emit(String event, Object... args) {
        mSocket.emit(event, args);
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (listener != null) {
                listener.onConnect();
            }
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (listener != null) {
                listener.onDisconnect();
            }
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (listener != null) {
                listener.onConnectError(args[0].toString());
            }
        }
    };

    private Emitter.Listener onReceiveMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            try {
                String id = data.getString("_id");
                String conversationId = data.getString("conversationId");
                String senderId = data.getString("senderId");
                String text = data.getString("text");
                long timestamp = data.getLong("createdAt"); // Assuming createdAt is timestamp

                // Determine if the message is sent by current user (this logic needs to be refined based on actual user ID)
                boolean isSent = false; // Placeholder, needs actual user ID comparison

                Message message = new Message(id, text, timestamp, isSent, senderId, "", "");
                if (listener != null) {
                    listener.onReceiveMessage(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public Socket getSocket() {
        return mSocket;
    }
}
