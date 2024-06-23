package com.example.whatflower.bean;

public class UserBean {

    public Integer id;
    public String username;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String account;
    public String password;

    public UserBean() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserBean(String username, String account, String password) {
        this.username = username;
        this.account = account;
        this.password = password;
    }
}