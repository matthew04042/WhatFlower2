package com.example.whatflower.config;

import com.example.whatflower.bean.UserBean;

public class AppData {

    private static AppData instance;
    private UserBean userBean;
    private Boolean isLogin = false;

    private AppData() {}
    
    public static synchronized AppData getInstance() {
        if (instance == null){
            instance = new AppData();
        }
        return instance;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public Boolean getLogin() {
        return isLogin;
    }

    public void setLogin(Boolean login) {
        isLogin = login;
    }
}
