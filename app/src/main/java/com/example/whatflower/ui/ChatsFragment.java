package com.example.whatflower.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatflower.R;
import com.example.whatflower.config.AppData;
import com.example.whatflower.config.DatabaseConfig;
import com.example.whatflower.ui.chats.AddFriendActivity;
import com.example.whatflower.ui.chats.ChatsActivity;
import com.example.whatflower.ui.chats.Conversation;
import com.example.whatflower.ui.chats.ConversationAdapter;
import com.example.whatflower.ui.chats.RecyclerItemClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private ChatsViewModel mViewModel;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerViewConversations;
    private ConversationAdapter adapter;
    private List<Conversation> conversationList;
    private String TAG = "ChatsFragment";
    private AppData appData;

    public static ChatsFragment newInstance() {
        return new ChatsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChatsViewModel.class);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.iv_chats_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"iv_chats_add");
                startAddFriendFragment();
            }
        });

        appData = AppData.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference(DatabaseConfig.CONTACTS);
        recyclerViewConversations = view.findViewById(R.id.chats_recycler);
        conversationList = new ArrayList<>();
        adapter = new ConversationAdapter(conversationList);
        recyclerViewConversations.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewConversations.setAdapter(adapter);




        recyclerViewConversations.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                recyclerViewConversations, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Open chat activity
                Log.i("ChatsFragment","position "+ position);
                Intent intent = new Intent(getActivity(), ChatsActivity.class);
                Conversation conversation =  conversationList.get(position);
                Gson gson = new Gson();
                intent.putExtra("ChatsItem", gson.toJson(conversation));
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                // Do something on long click if needed
            }
        }));

        if (appData.getUserBean() != null){
            setData();
        }

    }

    private void setData(){
        DatabaseReference conversationsRef = FirebaseDatabase.getInstance().getReference("conversations").child(appData.getUserBean().getAccount());
        conversationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                conversationList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.i("setData", dataSnapshot.getValue().toString());
                    Conversation conversation = dataSnapshot.getValue(Conversation.class);
//                    conversation.setUserName(dataSnapshot.getValue().toString());
                    conversationList.add(conversation);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (conversationList.isEmpty()){
            if (appData.getLogin()){
                setData();
            }
        }
    }

    private void startAddFriendFragment(){

        if (appData.getLogin()){
            Intent intent = new Intent(getContext(), AddFriendActivity.class);
            startActivity(intent);
        }else {
            Toast.makeText(getActivity(), " Please Login", Toast.LENGTH_SHORT).show();
        }


    }
}