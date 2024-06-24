package com.example.whatflower.ui.chats;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatflower.R;
import com.example.whatflower.ui.view.CharAvatarView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


import java.util.List;

public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.AddFriendViewHolder> {

    private List<JsonObject> conversationList;

    public AddFriendAdapter(List<JsonObject> conversationList) {
        this.conversationList = conversationList;
    }

    @NonNull
    @Override
    public AddFriendAdapter.AddFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_add_friend, parent, false);
        return new AddFriendAdapter.AddFriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddFriendAdapter.AddFriendViewHolder holder, int position) {
        JsonObject conversation = conversationList.get(position);
        String friendName = conversation.get("friendName").getAsString();
        String friendAccount = conversation.get("friendAccount").getAsString();
        if (friendName != null) {
            String a = friendName.substring(0, 1);
            holder.charAvatarView.setText(a);
            holder.textViewUser.setText(friendName);
            holder.textViewLastMessage.setText(friendAccount);
        }

    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }
    public static class AddFriendViewHolder extends RecyclerView.ViewHolder {
        CharAvatarView charAvatarView;
        TextView textViewUser;
        TextView textViewLastMessage;

        public AddFriendViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUser = itemView.findViewById(R.id.textViewUser);
            charAvatarView = itemView.findViewById(R.id.ca_item_user);
            textViewLastMessage = itemView.findViewById(R.id.textViewLastMessage);
        }
    }
}