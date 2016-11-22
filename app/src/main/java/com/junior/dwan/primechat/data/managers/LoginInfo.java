package com.junior.dwan.primechat.data.managers;

/**
 * Created by Might on 18.11.2016.
 */

public class LoginInfo {
    private String login;
    private String password;
    private boolean isValid;


    public boolean isValid() {
        if (login.length() > 0 && password.length() > 0)
            return true;
        else
            return false;
    }

    public LoginInfo() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String email) {
        this.login = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
