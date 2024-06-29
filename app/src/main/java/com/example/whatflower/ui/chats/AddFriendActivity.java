package com.example.whatflower.ui.chats;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.whatflower.R;
import com.example.whatflower.bean.UserBean;
import com.example.whatflower.config.AppData;
import com.example.whatflower.config.DatabaseConfig;
import com.example.whatflower.ui.view.CharAvatarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AddFriendActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private DatabaseReference mFriendDatabase;
    private EditText searchEditText ;
    private TextView tvUserName;
    private TextView tvAccount;
    private RelativeLayout rlResult;
    private CharAvatarView charAvatarView;
    private AppData appData;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        mDatabase = FirebaseDatabase.getInstance().getReference(DatabaseConfig.USERS);
        mFriendDatabase = FirebaseDatabase.getInstance().getReference(DatabaseConfig.ADD_FRIENDS);

        tvUserName = findViewById(R.id.tv_add_username);
        charAvatarView = findViewById(R.id.ca_add_user);
        tvAccount = findViewById(R.id.tv_add_account);
        searchEditText = findViewById(R.id.et_search);
        searchEditText.setText("13200001001");
        findViewById(R.id.iv_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = searchEditText.getText().toString();
                findUser(s);
            }
        });

        rlResult = findViewById(R.id.rl_add_userinfo);
        rlResult.setOnClickListener(view -> {
            showAddFriendDialog();
        });
        findViewById(R.id.rl_back).setOnClickListener( v -> {
            finish();
        });
    }

    private void findUser(String username){

        Log.i("user-findUser", username);

        mDatabase.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserBean userBean = snapshot.getValue(UserBean.class);
                Gson gson = new Gson();
                Log.i("user-findUser2", gson.toJson(userBean));
                if (userBean != null){
                    rlResult.setVisibility(View.VISIBLE);
                    charAvatarView.setText(userBean.getUsername().substring(0,1));
                    tvUserName.setText(userBean.getUsername());
                    tvAccount.setText(userBean.getAccount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showAddFriendDialog() {

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_friend, null);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);


        TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
        TextView dialogFriendName = dialogView.findViewById(R.id.dialog_username);
        EditText dialogInput = dialogView.findViewById(R.id.dialog_input);
        Button dialogButton = dialogView.findViewById(R.id.dialog_button);

        dialogFriendName.setText(tvUserName.getText().toString());


        AlertDialog alertDialog = builder.create();
        alertDialog.show();


        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inputText = dialogInput.getText().toString();

                dialogTitle.setText("Input Text: " + inputText);


                sendAddFriendApply(appData.getUserBean().getUsername(),tvUserName.getText().toString(), inputText);

                alertDialog.dismiss();
            }
        });

    }

    private void sendAddFriendApply(String user, String friend, String inputText) {
        Log.i("sendAddFriendApply", "" + user + " - " + friend);
        mFriendDatabase.child(user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isApply = false;
                Object o = snapshot.getValue();
                Gson gson = new Gson();
                String jsonString = gson.toJson(o);
                JsonArray jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();
                if (jsonArray != null){
                    Log.i("sendAddFriendApply", jsonArray.toString());
                    for (int i= 0; i < jsonArray.size(); i++){
                        JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                        if (friend.equals(jsonObject.get("friendName").getAsString())){
                            isApply = true;
                        }
                    }
                }else {
                    Log.i("sendAddFriendApply", "null" );
                    jsonArray = new JsonArray();
                }


                if (isApply){
                    Toast.makeText(getApplicationContext(), "Add friend quest", Toast.LENGTH_SHORT).show();
                }else {
                    JsonObject jsonObject1 = new JsonObject();
                    jsonObject1.addProperty("friendName", tvUserName.getText().toString());
                    jsonObject1.addProperty("friendAccount", friend);
                    jsonObject1.addProperty("inputText", inputText);
                    jsonObject1.addProperty("status", 0);
                    jsonArray.add(jsonObject1);
                    mFriendDatabase.child(user).setValue(jsonArray);
                    Toast.makeText(getApplicationContext(), " Request sent", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}