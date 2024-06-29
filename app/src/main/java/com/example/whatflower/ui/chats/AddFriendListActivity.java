package com.example.whatflower.ui.chats;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatflower.R;
import com.example.whatflower.config.AppData;
import com.example.whatflower.config.DatabaseConfig;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class AddFriendListActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private DatabaseReference mContactsDatabase;
    private AppData appData;
    private RecyclerView addFriendView;
    private AddFriendAdapter adapter;
    private List<JsonObject> addFriendList;
    private ChatHelper chatHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_list);

        addFriendView = findViewById(R.id.add_friend_recycler);

        mDatabase = FirebaseDatabase.getInstance().getReference(DatabaseConfig.ADD_FRIENDS);
        mContactsDatabase = FirebaseDatabase.getInstance().getReference(DatabaseConfig.CONTACTS);
        appData = AppData.getInstance();
        addFriendList = new ArrayList<>();
        chatHelper = new ChatHelper();

        mDatabase.child(appData.getUserBean().getAccount()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object o = snapshot.getValue();
                if (o != null){
                    Gson gson = new Gson();
                    Log.i("sendAddFriendApply",  gson.toJson(o));
                    JsonArray jsonArray = JsonParser.parseString(gson.toJson(o)).getAsJsonArray();
                    Log.i("sendAddFriendApply",  jsonArray.toString());
                    addFriendList.clear();

                    for (int i = 0; i < jsonArray.size(); i++){
                        JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                        addFriendList.add(jsonObject);
                    }

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        adapter = new AddFriendAdapter(addFriendList);
        addFriendView.setLayoutManager(new LinearLayoutManager(this));
        addFriendView.setAdapter(adapter);


        addFriendView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                addFriendView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                JsonObject item = addFriendList.get(position);


                showInfoDialog(item);
            }



            @Override
            public void onLongItemClick(View view, int position) {
                // Do something on long click if needed
            }
        }));

        findViewById(R.id.rl_back).setOnClickListener(v -> {
            finish();
        });
    }

    private void showInfoDialog(JsonObject item) {

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_friend_info, null);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);


        TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
        TextView dialogFriendName = dialogView.findViewById(R.id.dialog_username);
        TextView dialogInput = dialogView.findViewById(R.id.dialog_input);
        Button dialogButton = dialogView.findViewById(R.id.dialog_button_ok);
        Button dialogButtonCancel = dialogView.findViewById(R.id.dialog_button_cancel);

        dialogFriendName.setText(item.get("friendName").getAsString());
        dialogInput.setText(item.get("inputText").getAsString());


        AlertDialog alertDialog = builder.create();
        alertDialog.show();


        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createContacts(item);


                alertDialog.dismiss();
            }
        });
        dialogButtonCancel.setOnClickListener( v -> {

            alertDialog.dismiss();
        });
    }

    private void createContacts(JsonObject item){


        String account = appData.getUserBean().getAccount();
        String name = appData.getUserBean().getUsername();
        String friendAccount = item.get("friendAccount").getAsString();
        String friendName = item.get("friendName").getAsString();
        chatHelper.addFriend(account,name, friendAccount, friendName);


        addFriendList.remove(item);
        mDatabase.child(account).setValue(addFriendList);
        adapter.notifyDataSetChanged();

    }
}