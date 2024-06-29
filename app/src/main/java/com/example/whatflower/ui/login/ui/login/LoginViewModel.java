package com.example.whatflower.ui.login.ui.login;

import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.whatflower.R;
import com.example.whatflower.bean.UserBean;
import com.example.whatflower.config.AppData;
import com.example.whatflower.config.DatabaseConfig;
import com.example.whatflower.ui.login.data.LoginRepository;
import com.example.whatflower.ui.login.data.Result;
import com.example.whatflower.ui.login.data.model.LoggedInUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;


public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;
    private DatabaseReference mDatabase ;
    private AppData appData ;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
        this.appData = AppData.getInstance();
        this.mDatabase = FirebaseDatabase.getInstance().getReference(DatabaseConfig.USERS);

    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {

        Result<LoggedInUser> result = loginRepository.login(username, password);

        mDatabase.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserBean userBean = snapshot.getValue(UserBean.class);
                Gson gson = new Gson();
                Log.i("user-login", gson.toJson(userBean));
                if (userBean == null){
                    loginResult.setValue(new LoginResult(R.string.login_failed));
                }else {
                    if (userBean.getPassword().equals(password)){
                        appData.setUserBean(userBean);
                        appData.setLogin(true);
                        loginResult.setValue(new LoginResult(new LoggedInUserView("Login Success")));
                    }else {
                        loginResult.setValue(new LoginResult(R.string.login_failed));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void register(String account, String name, String password) {

        mDatabase.child(account).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserBean userBean = snapshot.getValue(UserBean.class);
                Log.i("user-register",""+ userBean);
                if (userBean == null){
                    UserBean user = new UserBean(name,account,password);
                    mDatabase.child(account).setValue(user);
                    appData.setUserBean(user);
                    appData.setLogin(true);
                    loginResult.setValue(new LoginResult(new LoggedInUserView("Register Success")));
                }else {
                    loginResult.setValue(new LoginResult(R.string.login_failed));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if(username.contains(".")){
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}