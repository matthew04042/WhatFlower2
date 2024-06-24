package com.example.whatflower.ui.chats.bean;

public class FriendBean {
    private String userId;
    private String friendId;
    private String userName;
    private String friendName;

    private String userNameAvatar;
    private String friendNameAvatar;

    public FriendBean() {
        // Default constructor required for calls to DataSnapshot.getValue(Friend.class)
    }

    public FriendBean(String userId, String userName, String friendId, String friendName) {
        this.userId = userId;
        this.friendId = friendId;
        this.userName = userName;
        this.friendName = friendName;
    }

    public String getUserId() {
        return userId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getUserNameAvatar() {
        return userNameAvatar;
    }

    public void setUserNameAvatar(String userNameAvatar) {
        this.userNameAvatar = userNameAvatar;
    }

    public String getFriendNameAvatar() {
        return friendNameAvatar;
    }

    public void setFriendNameAvatar(String friendNameAvatar) {
        this.friendNameAvatar = friendNameAvatar;
    }
}
