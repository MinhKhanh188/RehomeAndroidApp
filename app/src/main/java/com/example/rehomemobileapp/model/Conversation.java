package com.example.rehomemobileapp.model;

import java.util.Date;
import java.util.List;

public class Conversation {
    private String id;
    private List<String> participants;
    private Date createdAt;
    private String displayName;
    private String avatar;
    private String lastMessage;
    private Date lastMessageTime;

    private String otherParticipantId;
    private String otherParticipantName;
    private String otherParticipantAvatar;

    public Conversation() {
    }

    public Conversation(String id, List<String> participants, Date createdAt) {
        this.id = id;
        this.participants = participants;
        this.createdAt = createdAt;
    }
    public Conversation(String id, String displayName, String avatar, String lastMessage, long lastMessageTime) {
        this.id = id;
        this.displayName = displayName;
        this.avatar = avatar;
        this.lastMessage = lastMessage;
        this.lastMessageTime = new Date(lastMessageTime);
    }
    public Conversation(String id, String displayName, String avatar, String lastMessage, Date lastMessageTime) {
        this.id = id;
        this.displayName = displayName;
        this.avatar = avatar;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }






    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getUserName() {
        return displayName != null ? displayName : otherParticipantName;
    }

    public long getLastMessageTime() {
        return lastMessageTime != null ? lastMessageTime.getTime() : 0;
    }


    public void setLastMessageTime(Date lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    /**
     * Compute the other participant ID for 1-1 chat
     * @param currentUserId The current user's ID
     */
    public void computeOtherParticipant(String currentUserId) {
        if (participants != null && participants.size() == 2) {
            for (String participantId : participants) {
                if (!participantId.equals(currentUserId)) {
                    this.otherParticipantId = participantId;
                    break;
                }
            }
        }
    }

    /**
     * Check if this is a valid 1-1 conversation
     * @return true if conversation has exactly 2 participants
     */
    public boolean isOneOnOneChat() {
        return participants != null && participants.size() == 2;
    }

    public String getOtherParticipantId() {
        return otherParticipantId;
    }

    public void setOtherParticipantId(String otherParticipantId) {
        this.otherParticipantId = otherParticipantId;
    }

    public String getOtherParticipantName() {
        return otherParticipantName;
    }

    public void setOtherParticipantName(String otherParticipantName) {
        this.otherParticipantName = otherParticipantName;
    }

    public String getOtherParticipantAvatar() {
        return otherParticipantAvatar;
    }

    public void setOtherParticipantAvatar(String otherParticipantAvatar) {
        this.otherParticipantAvatar = otherParticipantAvatar;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "id='" + id + '\'' +
                ", participants=" + participants +
                ", createdAt=" + createdAt +
                ", displayName='" + displayName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", lastMessageTime=" + lastMessageTime +
                ", otherParticipantId='" + otherParticipantId + '\'' +
                ", otherParticipantName='" + otherParticipantName + '\'' +
                ", otherParticipantAvatar='" + otherParticipantAvatar + '\'' +
                '}';
    }
}
