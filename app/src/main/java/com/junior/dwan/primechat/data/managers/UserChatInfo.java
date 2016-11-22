package com.junior.dwan.primechat.data.managers;

import java.util.UUID;

/**
 * Created by Might on 18.11.2016.
 */

public class UserChatInfo {
    private String mLogin;
    private String mDate;
    private String text;
    private UUID mId;

    public UserChatInfo() {
        mId = UUID.randomUUID();
    }

    public UUID getId() {
        return mId;
    }

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String login) {
        mLogin = login;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
