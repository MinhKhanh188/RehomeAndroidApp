package com.example.rehomemobileapp.helpers;

import android.content.Context;
import android.util.Log;

import com.example.rehomemobileapp.data.SessionManager;
import com.example.rehomemobileapp.model.Conversation;
import com.example.rehomemobileapp.model.Message;
import com.example.rehomemobileapp.network.ApiClient;
import com.example.rehomemobileapp.network.ChatApiClient;
import com.example.rehomemobileapp.network.ChatApiService;
import com.example.rehomemobileapp.network.response.ConversationResponse;
import com.example.rehomemobileapp.network.response.MessageResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatHelper {
    private static final String TAG = "ChatHelper";
    private Context context;
    private ChatApiService chatApiService;

    public ChatHelper(Context context) {
        this.context = context;
        this.chatApiService = ChatApiClient.getChatApiService();
    }

    public interface ChatCallback<T> {
        void onSuccess(T result);
        void onError(String error);
    }

    public void createOrGetConversation(String participantId, final ChatCallback<Conversation> callback) {
        String token = SessionManager.getAuthToken(context);
        Log.d(TAG, "createOrGetConversation: Token = " + (token != null ? token : "NULL"));
        if (token == null) {
            callback.onError("Token không có sẵn");
            return;
        }

        chatApiService.createOrGetConversation(token, participantId).enqueue(new Callback<Conversation>() {
            @Override
            public void onResponse(Call<Conversation> call, Response<Conversation> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());

                } else {
                    String error = "Không thể tạo cuộc hội thoại: " + response.code();
                    if (response.errorBody() != null) {
                        try {
                            error += ": " + response.errorBody().string();
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing error body: " + e.getMessage());
                        }
                    }
                    Log.e(TAG, "createOrGetConversation: Error = " + error);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<Conversation> call, Throwable t) {
                Log.e(TAG, "createOrGetConversation: Network Error = " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void sendMessage(String conversationId, String text, final ChatCallback<Message> callback) {
        String token = SessionManager.getAuthToken(context);
        Log.d(TAG, "sendMessage: Token = " + (token != null ? token : "NULL"));
        if (token == null) {
            callback.onError("Token không có sẵn");
            return;
        }

        chatApiService.sendMessage(token, conversationId, text).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                Log.d(TAG, "sendMessage: URL = " + call.request().url());
                Log.d(TAG, "sendMessage: Response Code = " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "sendMessage: Response Body = " + response.body().toString());
                    callback.onSuccess(response.body().getData());
                } else {
                    String error = "Không thể gửi tin nhắn: " + response.code();
                    if (response.errorBody() != null) {
                        try {
                            error += ": " + response.errorBody().string();
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing error body: " + e.getMessage());
                        }
                    }
                    Log.e(TAG, "sendMessage: Error = " + error);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.e(TAG, "sendMessage: Network Error = " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void getMessages(String conversationId, final ChatCallback<List<Message>> callback) {
        String token = SessionManager.getAuthToken(context);
        Log.d(TAG, "getMessages: Token = " + (token != null ? token : "NULL"));
        if (token == null) {
            callback.onError("Token không có sẵn");
            return;
        }

        chatApiService.getMessages(token, conversationId).enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                Log.d(TAG, "getMessages: URL = " + call.request().url());
                Log.d(TAG, "getMessages: Response Code = " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "getMessages: Response Body = " + response.body().toString());
                    callback.onSuccess(response.body());
                } else {
                    String error = "Không thể tải tin nhắn: " + response.code();
                    if (response.errorBody() != null) {
                        try {
                            error += ": " + response.errorBody().string();
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing error body: " + e.getMessage());
                        }
                    }
                    Log.e(TAG, "getMessages: Error = " + error);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Log.e(TAG, "getMessages: Network Error = " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void getUserConversations(final ChatCallback<List<Conversation>> callback) {
        String token = SessionManager.getAuthToken(context);
        Log.d(TAG, "getUserConversations: Token = " + (token != null ? token : "NULL"));
        if (token == null) {
            callback.onError("Token không có sẵn");
            return;
        }

        chatApiService.getUserConversations(token).enqueue(new Callback<List<Conversation>>() {
            @Override
            public void onResponse(Call<List<Conversation>> call, Response<List<Conversation>> response) {
                Log.d(TAG, "getUserConversations: URL = " + call.request().url());
                Log.d(TAG, "getUserConversations: Response Code = " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String error = "Không thể tải danh sách cuộc hội thoại: " + response.code();
                    if (response.errorBody() != null) {
                        try {
                            error += ": " + response.errorBody().string();
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing error body: " + e.getMessage());
                        }
                    }
                    Log.e(TAG, "getUserConversations: Error = " + error);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<List<Conversation>> call, Throwable t) {
                Log.e(TAG, "getUserConversations: Network Error = " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

}
