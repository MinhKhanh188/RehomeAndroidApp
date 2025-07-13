    package com.example.rehomemobileapp.network.response;

    import com.example.rehomemobileapp.model.Conversation;

    public class ConversationResponse {
        private boolean success;
        private String message;
        private Conversation data;

        public ConversationResponse() {
        }

        public ConversationResponse(boolean success, String message, Conversation data) {
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

        public Conversation getData() {
            return data;
        }

        public void setData(Conversation data) {
            this.data = data;
        }
    }
