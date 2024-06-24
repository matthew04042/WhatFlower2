package com.example.whatflower.ui.chats;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatflower.R;
import com.example.whatflower.ui.view.CharAvatarView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {

    private List<Conversation> conversationList;

    public ConversationAdapter(List<Conversation> conversationList) {
        this.conversationList = conversationList;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_conversation, parent, false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        Conversation conversation = conversationList.get(position);

//        String lastMessage = (String) conversation.get("lastMessage");
//        String friendName = (String) conversation.get("friendName");
//        long timestamp = (long) conversation.get("timestamp");
        String lastMessage = conversation.getLastMessage();
        String friendName = conversation.getUserName();
        long timestamp = conversation.getLastSendTime();

        holder.textViewUser.setText(friendName);
        holder.textViewLastMessage.setText(lastMessage);
        holder.lastTime.setText(formatTimestamp(timestamp));
        holder.charAvatarView.setText(friendName.substring(0,1));
    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }

    // yyyy-MM-dd HH:mm
    private String formatTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    public static class ConversationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUser;
        TextView textViewLastMessage;
        TextView lastTime;
        CharAvatarView charAvatarView;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUser = itemView.findViewById(R.id.textViewUser);
            textViewLastMessage = itemView.findViewById(R.id.textViewLastMessage);
            lastTime = itemView.findViewById(R.id.tv_last_time);
            charAvatarView = itemView.findViewById(R.id.ca_conver_avatar);
        }
    }
}