package com.example.whatflower.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.whatflower.R;
import com.example.whatflower.config.AppData;
import com.example.whatflower.config.DatabaseConfig;
import com.example.whatflower.ui.chats.AddFriendListActivity;
import com.example.whatflower.ui.login.ui.login.LoginActivity;
import com.example.whatflower.ui.view.CharAvatarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private AppData appData;

    private TextView name;
    private CharAvatarView userAvatar;
    private RelativeLayout logout;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference(DatabaseConfig.USERS);
        appData = AppData.getInstance();

        name = view.findViewById(R.id.tv_name);
        userAvatar = view.findViewById(R.id.ca_user);
        logout = view.findViewById(R.id.rl_logout);



        view.findViewById(R.id.rl_user).setOnClickListener( v -> {

            if (appData.getLogin()){
                name.setText(appData.getUserBean().getUsername());
                userAvatar.setText(appData.getUserBean().getUsername().substring(0,1));
            }else {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.rl_add_user).setOnClickListener(v -> {
            if (appData.getLogin()){
                Intent intent = new Intent(getActivity(), AddFriendListActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener( v -> {
            logoutDialog();
        });

    }

    private void logoutDialog(){
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_logout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);

        Button dialogButton = dialogView.findViewById(R.id.dialog_button_ok);
        Button dialogButtonCancel = dialogView.findViewById(R.id.dialog_button_cancel);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logout();
                alertDialog.dismiss();
            }
        });
        dialogButtonCancel.setOnClickListener( v -> {
            alertDialog.dismiss();
        });
    }

    private void logout(){
        appData.setLogin(false);
        appData.setUserBean(null);
        name.setText("Login/ Register");
        userAvatar.setText("A");
        logout.setVisibility(View.GONE);
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i("UserFragment","onResume "+ appData.getLogin());
        Gson gson = new Gson();
        Log.i("UserFragment","onResume "+ gson.toJson(appData.getUserBean()));
        if (appData.getLogin()){
            name.setText(appData.getUserBean().getUsername());
            String a = appData.getUserBean().getUsername().substring(0,1);
            userAvatar.setText(a);
            logout.setVisibility(View.VISIBLE);
        }
    }

}