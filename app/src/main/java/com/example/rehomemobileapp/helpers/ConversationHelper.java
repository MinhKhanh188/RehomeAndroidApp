package com.example.rehomemobileapp.helpers;

import android.util.Log;

import com.example.rehomemobileapp.model.Conversation;
import com.example.rehomemobileapp.model.Participant;
import com.example.rehomemobileapp.utils.AvatarUtils;

import java.util.List;

/**
 * Helper class to handle conversation-related logic for 1-1 chats
 */
public class ConversationHelper {

    /**
     * Process a list of conversations from backend to prepare them for UI display
     * This method computes the other participant for each 1-1 conversation
     *
     * @param conversations List of conversations from backend
     * @param currentUserId Current user's ID
     * @return Processed list ready for UI
     */
    public static void processConversationsForUI(List<Conversation> conversations, String currentUserId) {
        for (Conversation conversation : conversations) {
            if (conversation.isOneOnOneChat()) {
                Log.d("ChatHelper", "Current User ID: " + currentUserId);
                conversation.computeOtherParticipant(currentUserId);
                Log.d("ChatHelper", "Other Participant Name: " + conversation.getOtherParticipantName());

                // Set display name and avatar based on computed other participant
                if (conversation.getOtherParticipantName() != null) {
                    conversation.setDisplayName(conversation.getOtherParticipantName());
                    conversation.setAvatar(AvatarUtils.generateAvatarInitials(conversation.getOtherParticipantName()));
                }
            }
        }
    }

    /**
     * Create or get conversation with another user (for starting new chats)
     * This corresponds to the createOrGetConversation API endpoint
     *
     * @param currentUserId Current user's ID
     * @param otherUserId Other user's ID
     * @return Conversation object representing the conversation
     */
    public static Conversation createOneOnOneConversation(String currentUserId, String otherUserId) {
        // TODO: In real app, make API call to backend
        // POST /api/conversations/create-or-get
        // Body: { "participantId": otherUserId }

        // For demo, create a placeholder conversation
        Conversation conversation = new Conversation();
        conversation.setId("temp_" + System.currentTimeMillis()); // Temporary ID
        // In real app, this would be set by backend response

        return conversation;
    }

    /**
     * Check if a conversation is a valid 1-1 chat
     *
     * @param conversation The conversation to check
     * @param currentUserId Current user's ID
     * @return true if it's a valid 1-1 chat involving current user
     */
    public static boolean isValidOneOnOneChat(Conversation conversation, String currentUserId) {
        if (!conversation.isOneOnOneChat()) {
            return false;
        }

        List<Participant> participants = conversation.getParticipants();
        for (Participant participant : participants) {
            if (participant.get_id().equals(currentUserId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generate placeholder name for demo purposes
     * In real app, this would be replaced by API call to get user info
     */
    private static String generatePlaceholderName(String userId) {
        // Simple hash-based name generation for demo
        int hash = Math.abs(userId.hashCode());
        String[] firstNames = {"Phạm Minh", "Kiều", "Lê Mỹ", "Nguyễn Văn", "Trần Thị", "Hoàng"};
        String[] lastNames = {"Tùng", "Khanh", "Lệ", "An", "Bình", "Chi"};

        String firstName = firstNames[hash % firstNames.length];
        String lastName = lastNames[(hash / firstNames.length) % lastNames.length];

        return firstName + " " + lastName;
    }
}