package com.example.whatflower.ui.chats;

public class Conversation {
    private String chatId;
    private String userName;
    private String userAccount;
    private String lastMessage;
    private long lastSendTime;

    private String userNameAvatar;

    public Conversation() {
    }

    public Conversation(String userName, String lastMessage) {
        this.userName = userName;
        this.lastMessage = lastMessage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getLastSendTime() {
        return lastSendTime;
    }

    public void setLastSendTime(long lastSendTime) {
        this.lastSendTime = lastSendTime;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserNameAvatar() {
        return userNameAvatar;
    }

    public void setUserNameAvatar(String userNameAvatar) {
        this.userNameAvatar = userNameAvatar;
    }
}
