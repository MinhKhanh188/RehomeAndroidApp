package com.example.rehomemobileapp.network;

import com.example.rehomemobileapp.model.Conversation;
import com.example.rehomemobileapp.model.Message;
import com.example.rehomemobileapp.network.response.ConversationResponse;
import com.example.rehomemobileapp.network.response.MessageResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Field;
import retrofit2.http.Header;

public interface ChatApiService {

    // Tạo hoặc lấy cuộc hội thoại
    @POST(ApiConstants.JOIN_CONVERSATION)
    @FormUrlEncoded
    Call<Conversation> createOrGetConversation(
            @Header("Authorization") String token,
            @Field("participantId") String participantId
    );

    // Gửi tin nhắn
    @POST(ApiConstants.SEND_MESSAGE)
    @FormUrlEncoded
    Call<MessageResponse> sendMessage(
            @Header("Authorization") String token,
            @Field("conversationId") String conversationId,
            @Field("text") String text
    );

    // Lấy danh sách tin nhắn trong cuộc hội thoại
    @GET(ApiConstants.GET_MESSAGES + "/{conversationId}")
    Call<List<Message>> getMessages(
            @Header("Authorization") String token,
            @Path("conversationId") String conversationId
    );

    // Lấy danh sách cuộc hội thoại của người dùng
    @GET(ApiConstants.GET_USER_CONVERSATIONS)
    Call<List<Conversation>> getUserConversations(
            @Header("Authorization") String token
    );
}
