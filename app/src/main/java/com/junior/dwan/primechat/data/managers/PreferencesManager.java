package com.junior.dwan.primechat.data.managers;

import android.content.SharedPreferences;

import com.junior.dwan.primechat.utils.ConstantManagers;
import com.junior.dwan.primechat.utils.PrimeChatApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Might on 18.11.2016.
 */

public class PreferencesManager {
    private SharedPreferences mSharedPreferences;


    public PreferencesManager() {
        mSharedPreferences = PrimeChatApplication.getSharedPreferences();
    }

    public void saveUserProfileData(String login, String pass) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManagers.PREFERENCES_LOGIN, login);
        editor.putString(ConstantManagers.PREFERENCES_PASS, pass);
        editor.apply();
    }

    public List<String> loadUserProfileData() {
        List<String> userInfo = new ArrayList<>();
        userInfo.add(mSharedPreferences.getString(ConstantManagers.PREFERENCES_LOGIN, null));
        userInfo.add(mSharedPreferences.getString(ConstantManagers.PREFERENCES_PASS, null));
        return userInfo;
    }

    public void saveUserChatForCheckNewMess(String login, String date, String text) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManagers.PREFERENCES_CHECK_LOGIN, login);
        editor.putString(ConstantManagers.PREFERENCES_CHECK_DATE, date);
        editor.putString(ConstantManagers.PREFERENCES_CHECK_TEXT, text);
        editor.apply();
    }

    public List<String> loadUserChatForCheckNewMess() {
        List<String> userInfoChat = new ArrayList<>();
        userInfoChat.add(mSharedPreferences.getString(ConstantManagers.PREFERENCES_CHECK_LOGIN, null));
        userInfoChat.add(mSharedPreferences.getString(ConstantManagers.PREFERENCES_CHECK_DATE, null));
        userInfoChat.add(mSharedPreferences.getString(ConstantManagers.PREFERENCES_CHECK_TEXT, null));
        return userInfoChat;
    }

}
