package com.example.whatflower.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatflower.R;
import com.example.whatflower.config.AppData;
import com.example.whatflower.config.DatabaseConfig;
import com.example.whatflower.config.ToastUtils;
import com.example.whatflower.ui.chats.Conversation;
import com.example.whatflower.ui.chats.RecyclerItemClickListener;
import com.example.whatflower.ui.picture.PictureAdapter;
import com.example.whatflower.ui.picture.PictureBean;
import com.example.whatflower.ui.view.WebActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class PictureFragment extends Fragment {

    private DatabaseReference mDatabase;
    private RecyclerView recyclerViewConversations;
    private PictureAdapter adapter;
    private List<PictureBean> pictureBeanList;
    private List<Conversation> conversationList;
    private String TAG = "PictureFragment";
    private AppData appData;
    private Map<String,String> accountMap;

    private Spinner spinner;
    ArrayAdapter<String> spinnerAdapter;
    String[] items;

    public PictureFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_picture, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appData = AppData.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference(DatabaseConfig.CONTACTS);
        recyclerViewConversations = view.findViewById(R.id.rv_list);
        pictureBeanList = new ArrayList<>();
        conversationList = new ArrayList<>();
        adapter = new PictureAdapter(getContext(), pictureBeanList);
        recyclerViewConversations.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewConversations.setAdapter(adapter);

        accountMap = new HashMap<>();
        spinner = view.findViewById(R.id.spinner_picture);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                String friendAccount = accountMap.get(selectedItem);
                ToastUtils.showToast(getActivity(),"User: " + selectedItem);

                setFriendData(friendAccount);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        recyclerViewConversations.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                recyclerViewConversations, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Open chat activity
                Log.i("ChatsFragment","position "+ position);
                Intent intent = new Intent(getActivity(), WebActivity.class);
                PictureBean pictureBean =  pictureBeanList.get(position);
                Gson gson = new Gson();
                intent.putExtra("pictureItem", gson.toJson(pictureBean));
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));

        if (appData.getUserBean() != null){
            setData();
        }

    }

    private void setSpinner(List<String> friends){
        spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, friends);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    private void setData(){
        if (conversationList.size() == 0){
            getFriendData();
        }

        if (pictureBeanList.size() == 0){
            DatabaseReference conversationsRef = FirebaseDatabase.getInstance().getReference("pictures").child(appData.getUserBean().getAccount());
            conversationsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    pictureBeanList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Log.i("setData", dataSnapshot.getValue().toString());
                        PictureBean pictureBean = dataSnapshot.getValue(PictureBean.class);
                        pictureBeanList.add(pictureBean);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }

    private void getFriendData(){
        DatabaseReference conversationsRef = FirebaseDatabase.getInstance().getReference("conversations").child(appData.getUserBean().getAccount());
        conversationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                conversationList.clear();
                Map<String, String> map = new HashMap<>();
                List<String> friends = new ArrayList<>();
                map.put(appData.getUserBean().getUsername(),appData.getUserBean().getAccount());
                friends.add(appData.getUserBean().getUsername());
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.i("setData", dataSnapshot.getValue().toString());
                    Conversation conversation = dataSnapshot.getValue(Conversation.class);
//                    conversation.setUserName(dataSnapshot.getValue().toString());
                    conversationList.add(conversation);
                    friends.add(conversation.getUserName());
                    map.put(conversation.getUserName(),conversation.getUserAccount());
                }
                setSpinner(friends);
                accountMap = map;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setFriendData(String account){
        DatabaseReference conversationsRef = FirebaseDatabase.getInstance().getReference("pictures").child(account);
        conversationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pictureBeanList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.i("setData", dataSnapshot.getValue().toString());
                    PictureBean pictureBean = dataSnapshot.getValue(PictureBean.class);
                    pictureBeanList.add(pictureBean);
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
        if (pictureBeanList.isEmpty()){
            if (appData.getLogin()){
                setData();
            }
        }
    }

}