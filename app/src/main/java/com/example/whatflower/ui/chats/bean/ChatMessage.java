package com.example.whatflower.ui.chats.bean;

public class ChatMessage {
    private String sender;
    private String name;
    private String message;
    private long timestamp;

    public ChatMessage() {
        // Default constructor required for calls to DataSnapshot.getValue(ChatMessage.class)
    }

    public ChatMessage(String sender, String name, String message, long timestamp) {
        this.sender = sender;
        this.name = name;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getName() {
        return name;
    }
}
