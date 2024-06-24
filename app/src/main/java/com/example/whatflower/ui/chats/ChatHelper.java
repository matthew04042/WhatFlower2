package com.example.whatflower.ui.chats;

import android.util.Log;

import com.example.whatflower.ui.chats.bean.ChatMessage;
import com.example.whatflower.ui.chats.bean.FriendBean;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ChatHelper {

    private DatabaseReference database;

    public ChatHelper() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        database = db.getReference();
    }

    public void addFriend(String userId, String userName, String friendId, String friendName) {

        FriendBean friend1 = new FriendBean(userId, userName, friendId, friendName);
        FriendBean friend2 = new FriendBean(friendId, friendName, userId, userName);


        database.child("friends").child(userId).child(friendId).setValue(friend1)
                .addOnSuccessListener(aVoid -> Log.d("ChatHelper", "Friend added successfully"))
                .addOnFailureListener(e -> Log.w("ChatHelper", "Error adding friend", e));


        database.child("friends").child(friendId).child(userId).setValue(friend2)
                .addOnSuccessListener(aVoid -> Log.d("ChatHelper", "Friend added successfully"))
                .addOnFailureListener(e -> Log.w("ChatHelper", "Error adding friend", e));


        String chatId = generateChatId(userId, friendId);
        long createTime = System.currentTimeMillis();
        Conversation my = new Conversation();
        my.setChatId(chatId);
        my.setUserAccount(friendId);
        my.setUserName(friendName);
        my.setLastMessage("New Friend");
        my.setLastSendTime(createTime);

        Conversation friend = new Conversation();
        friend.setChatId(chatId);
        friend.setUserName(userName);
        friend.setUserAccount(userId);
        friend.setLastMessage("New Friend");
        friend.setLastSendTime(createTime);

        database.child("conversations").child(userId).child(friendId).setValue(my);
        database.child("conversations").child(friendId).child(userId).setValue(friend);


    }

    private void updateConversationList(String chatId, String sender, String receiver, String message, long timestamp) {
        Map<String, Object> conversationUpdate = new HashMap<>();
        conversationUpdate.put("lastMessage", message);
        conversationUpdate.put("lastSendTime", timestamp);

        database.child("conversations").child(sender).child(receiver).updateChildren(conversationUpdate);
        database.child("conversations").child(receiver).child(sender).updateChildren(conversationUpdate);
    }

    public DatabaseReference getChatRef(String chatId) {
        return database.child("messages").child(chatId);
    }

    private String generateChatId(String userId1, String userId2) {
        if (userId1.compareTo(userId2) < 0) {
            return userId1 + "_" + userId2;
        } else {
            return userId2 + "_" + userId1;
        }
    }

    public void sendMessage(String chatId, String sender, String senderName, String receiver,  String message) {
        long timestamp = System.currentTimeMillis();
        ChatMessage chatMessage = new ChatMessage(sender, senderName, message, timestamp);

        database.child("messages").child(chatId).push().setValue(chatMessage)
                .addOnSuccessListener(aVoid -> Log.d("ChatHelper", "Message successfully sent!"))
                .addOnFailureListener(e -> Log.w("ChatHelper", "Error sending message", e));


        updateConversationList(chatId, sender, receiver, message, timestamp);
    }
}
