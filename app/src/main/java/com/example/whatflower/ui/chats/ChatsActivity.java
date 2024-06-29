package com.example.whatflower.ui.chats;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatflower.R;
import com.example.whatflower.config.AppData;
import com.example.whatflower.ui.chats.adapter.ChatAdapter;
import com.example.whatflower.ui.chats.bean.ChatMessage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {

    private ChatHelper chatHelper;
    private List<ChatMessage> chatMessages = new ArrayList<>();
    private ChatAdapter chatAdapter;
    private EditText messageInput;
    private AppData appData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        appData = AppData.getInstance();

        Intent intent = getIntent();
        String chatsItem = intent.getStringExtra("ChatsItem");

        Log.i("ChatsActivity","userData - "+chatsItem);

        JsonObject jsonObject = JsonParser.parseString(chatsItem).getAsJsonObject();

        String chatId = jsonObject.get("chatId").getAsString();
        String userAccount = jsonObject.get("userAccount").getAsString();
        String userName = jsonObject.get("userName").getAsString();

        TextView title = findViewById(R.id.tv_title);
        title.setText(userName);

        RecyclerView recyclerView = findViewById(R.id.rv_chat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(chatMessages);
        recyclerView.setAdapter(chatAdapter);

        chatHelper = new ChatHelper();


        chatHelper.getChatRef(chatId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatMessages.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);
                    chatMessages.add(chatMessage);
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("ChatActivity", "loadPost:onCancelled", databaseError.toException());
            }
        });

        messageInput = findViewById(R.id.et_chat_input);

        findViewById(R.id.iv_chat_send).setOnClickListener( v -> {
            Editable text = messageInput.getText();
            chatHelper.sendMessage(
                    chatId,
                    appData.getUserBean().getAccount(),
                    appData.getUserBean().getUsername(),
                    userAccount,text.toString());

            messageInput.setText("");
        });

        findViewById(R.id.rl_back).setOnClickListener( v -> {
            finish();
        });
    }
}