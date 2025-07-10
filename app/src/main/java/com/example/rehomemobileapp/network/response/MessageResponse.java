package com.example.rehomemobileapp.network.response;

import com.example.rehomemobileapp.model.Message;

public class MessageResponse {
    private boolean success;
    private String message;
    private Message data;

    public MessageResponse() {
    }

    public MessageResponse(boolean success, String message, Message data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Message getData() {
        return data;
    }

    public void setData(Message data) {
        this.data = data;
    }
}
