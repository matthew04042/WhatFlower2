package com.example.whatflower.ui.chats.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatflower.R;
import com.example.whatflower.ui.chats.bean.ChatMessage;
import com.example.whatflower.ui.view.CharAvatarView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatMessage> chatMessages;

    public ChatAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);
        holder.senderTextView.setText(chatMessage.getName());
        holder.messageTextView.setText(chatMessage.getMessage());
        holder.charAvatarView.setText(chatMessage.getName().substring(0,1));
//        holder.timestampTextView.setText(String.valueOf(chatMessage.getTimestamp())); // 简单地显示时间戳
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView senderTextView;
        TextView messageTextView;
        TextView timestampTextView;
        CharAvatarView charAvatarView;

        ChatViewHolder(View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.senderTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
//            timestampTextView = itemView.findViewById(R.id.timestampTextView);
            charAvatarView = itemView.findViewById(R.id.ca_chat_user);
        }
    }
}