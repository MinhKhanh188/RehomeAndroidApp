package com.example.rehomemobileapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Message {
    private String id;
    private String conversationId;
    private String senderId;
    private String text;
    private Date sentAt;
    private boolean isSent;
    private String senderName;
    private String senderAvatar;
    private String type = "text";
    @SerializedName("post")
    private Post post;

    public Message() {
    }

    public Message(String id, String text, Date sentAt, boolean isSent) {
        this.id = id;
        this.text = text;
        this.sentAt = sentAt;
        this.isSent = isSent;
    }

    public Message(String id, String text, long timestamp, boolean isSent) {
        this.id = id;
        this.text = text;
        this.sentAt = new Date(timestamp);
        this.isSent = isSent;
    }

    public Message(String id, String text, long timestamp, boolean isSent, String senderId) {
        this.id = id;
        this.text = text;
        this.sentAt = new Date(timestamp);
        this.isSent = isSent;
        this.senderId = senderId;
    }

    public Message(String id, String text, long timestamp, boolean isSent, String senderId, String senderName, String senderAvatar) {
        this.id = id;
        this.text = text;
        this.sentAt = new Date(timestamp);
        this.isSent = isSent;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderAvatar = senderAvatar;
    }

    public Message(String id, String conversationId, String senderId, String text, Date sentAt, boolean isSent) {
        this.id = id;
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.text = text;
        this.sentAt = sentAt;
        this.isSent = isSent;
    }

    public Message(String id, String conversationId, String senderId, String text, Date sentAt, boolean isSent, String senderName, String senderAvatar) {
        this.id = id;
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.text = text;
        this.sentAt = sentAt;
        this.isSent = isSent;
        this.senderName = senderName;
        this.senderAvatar = senderAvatar;
        this.type = "text";  // default
    }

    // ✨ Add getter and setter for post
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    // Getters & setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getSentAt() {
        return sentAt;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public String getType() {
        return type;
    }


    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderAvatar() {
        return senderAvatar;
    }

    public void setSenderAvatar(String senderAvatar) {
        this.senderAvatar = senderAvatar;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return text;
    }

    public long getTimestamp() {
        return sentAt != null ? sentAt.getTime() : 0;
    }
}
