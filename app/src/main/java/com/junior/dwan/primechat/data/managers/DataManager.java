package com.junior.dwan.primechat.data.managers;

import com.junior.dwan.primechat.data.network.RestService;
import com.junior.dwan.primechat.data.network.ServiceGenerator;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by Might on 17.11.2016.
 */

public class DataManager {
    private static DataManager INSTANCE = null;
    PreferencesManager mPreferencesManager;
    private ArrayList<UserChatInfo> mUserChatInfoList;
    private RestService mRestService;


    public DataManager() {
        mUserChatInfoList = new ArrayList<UserChatInfo>();
        this.mPreferencesManager = new PreferencesManager();
        this.mRestService = ServiceGenerator.createService(RestService.class);
    }

    public static DataManager getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public ArrayList<UserChatInfo> getUserChatInfoList() {
        return mUserChatInfoList;
    }


    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    //region=======Network=======
    public Call<ResponseBody> getloginInfo(String login, String passwords) {
        return mRestService.getLoginInfo(login, passwords);
    }

    public Call<ResponseBody> createUser(String action, String email, String passwords) {
        return mRestService.createUser(action, email, passwords);
    }

    public Call<ResponseBody> sendMessageToServer(String action, String author, String date, String text) {
        return mRestService.sendMessageToServer(action, author, date, text);
    }

    public Call<ResponseBody> getChatMessages(String login, String passwords) {
        return mRestService.getChatMessages(login, passwords);
    }

    //endregion
}
