package com.example.rehomemobileapp.socket;

import android.util.Log;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;

public class SocketManager {

    private static final String TAG = "SocketManager";
    private static final String SOCKET_URL = "https://rehome.id.vn";

    private static SocketManager instance;
    private Socket socket;
    private SocketListener listener;

    private SocketManager() {
        try {
            IO.Options options = new IO.Options();
            options.reconnection = true;
            socket = IO.socket(SOCKET_URL, options);
        } catch (URISyntaxException e) {
            Log.e(TAG, "Socket URI error: " + e.getMessage());
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
            socket.connect();
            socket.on(Socket.EVENT_CONNECT, args -> {
                Log.d(TAG, "Socket connected");
                if (listener != null) listener.onConnect();
            });
            socket.on(Socket.EVENT_DISCONNECT, args -> {
                Log.d(TAG, "Socket disconnected");
                if (listener != null) listener.onDisconnect();
            });
            socket.on("receive_message", args -> {
                if (listener != null && args.length > 0) {
                    // You may need to parse args[0] into a Message object here
                    Log.d(TAG, "Received raw message: " + args[0]);
                    // TODO: Convert args[0] to Message model and call listener.onReceiveMessage(...)
                }
            });
            socket.on(Socket.EVENT_CONNECT_ERROR, args -> {
                if (listener != null) {
                    listener.onConnectError(args.length > 0 ? args[0].toString() : "Unknown");
                }
            });
        }
    }

    public void disconnect() {
        if (socket != null) {
            socket.disconnect();
        }
    }

    public void emit(String event, Object data) {
        if (socket != null && socket.connected()) {
            socket.emit(event, data);
        }
    }

    public void setListener(SocketListener listener) {
        this.listener = listener;
    }

    public interface SocketListener {
        void onConnect();
        void onDisconnect();
        void onReceiveMessage(com.example.rehomemobileapp.model.Message message);
        void onConnectError(String error);
    }
}
